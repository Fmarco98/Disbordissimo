/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.client.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.onvoid.webrtc.*;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.audio.AudioOptions;
import dev.onvoid.webrtc.media.audio.AudioTrack;
import we.ytc.disbordissimo.common.TxUtils;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.NullLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <h1>WebRTC Client class</h1>
 * This class represents a webRTC client. <br>
 * The WebRTC communication uses the MCU architecture, in specific the MCU server is Janus. <br>
 * <br>
 * Object methods:<br>
 *  - "constructor"(..)<br>
 *  - start()<br>
 *  - stop()<br>
 *  - setLogger(..)<br>
 * <br>
 * <br>
 * Janus: https://github.com/meetecho/janus-gateway
 */
public class WebRTCClient implements WebSocket.Listener, PeerConnectionObserver {
    private static final Gson gson = new Gson();

    private long userID;
    private String username;
    private int roomID;
    private String roomPin;
    private String janusUrl;
    private String stunServer;
    private AudioOptions audioOptions;

    private Long sessionId;
    private Long handleId;
    private WebSocket webSocket;

    private PeerConnectionFactory factory;
    private RTCPeerConnection peerConnection;
    private ScheduledExecutorService pingScheduler;

    private Logger logger;

    /**
     * Constructor.
     *
     * @param userID
     *        Users ID
     * @param username
     *        Username
     * @param roomID
     *        Room ID
     * @param roomPin
     *        Room security pin
     * @param janusUrl
     *        Janus server URL
     * @param audioOptions
     *        {@link AudioOptions} to configure mic params.
     */
    public WebRTCClient(long userID ,String username, int roomID, String roomPin, String janusUrl,
                     AudioOptions audioOptions) {
        this(userID, username, roomID, roomPin, janusUrl, "stun:stun.l.google.com:19302", audioOptions);
    }

    /**
     * Constructor.
     *
     * @param userID
     *        Users ID
     * @param username
     *        Username
     * @param roomID
     *        Room ID
     * @param roomPin
     *        Room security pin
     * @param janusUrl
     *        Janus server URL
     * @param stunServer
     *        STUN server URL
     * @param audioOptions
     *        {@link AudioOptions} to configure mic params.
     */
    public WebRTCClient(long userID ,String username, int roomID, String roomPin, String janusUrl,
                     String stunServer, AudioOptions audioOptions) {
        this.userID = userID;
        this.username = username;
        this.roomID = roomID;
        this.roomPin = roomPin;
        this.janusUrl = janusUrl;
        this.stunServer = stunServer;
        this.audioOptions = audioOptions;

        logger = new NullLogger();
    }

    /**
     * Starts the WebRTC communication. It joins the room.
     */
    public void start() {
        HttpClient client = HttpClient.newHttpClient();
        this.webSocket = client.newWebSocketBuilder()
                .subprotocols("janus-protocol")
                .buildAsync(URI.create(janusUrl), this)
                .join();

        this.factory = new PeerConnectionFactory();

        createSession();
    }

    /**
     * Stops the WebRTC communication by quitting from the room and freeing the resources.
     */
    public void stop() {
        leaveRoom();

        try {
            if(!pingScheduler.isShutdown()) pingScheduler.shutdown();

            if (webSocket != null) {
                webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye").join();
            }
            if (peerConnection != null) {
                peerConnection.close();
                peerConnection = null;
            }
            if (factory != null) {
                factory.dispose();
                factory = null;
            }
        } catch (Exception e) {}
        System.gc();
    }

    /**
     * Sets the logger.
     *
     * @param logger
     *        {@link Logger}
     *
     * @return the object itself
     */
    public WebRTCClient setLogger(Logger logger) {
        this.logger = logger;

        return this;
    }

    private void createSession() {
        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "create");
        msg.addProperty("transaction", TxUtils.gen(userID, "session_create"));
        send(msg);
    }

    private void attachAudioBridge() {
        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "attach");
        msg.addProperty("session_id", this.sessionId);
        msg.addProperty("plugin", "janus.plugin.audiobridge");
        msg.addProperty("transaction", TxUtils.gen(userID, "audio-bridge_attach"));
        send(msg);
    }

    private void joinRoom() {
        JsonObject body = new JsonObject();
        body.addProperty("request", "join");
        body.addProperty("room", this.roomID);
        body.addProperty("display", this.username);
        body.addProperty("id", userID);
        body.addProperty("pin", roomPin);
//        body.addProperty("quality", 6);
        sendPluginMessage(body, TxUtils.gen(userID, "room_join"));
    }

    private void leaveRoom() {
        JsonObject body = new JsonObject();
        body.addProperty("request", "leave");
        sendPluginMessage(body, TxUtils.gen(userID, "room_leave"));
    }

    private void keepAlive() {
        pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            if (webSocket != null && sessionId != null) {
                JsonObject keepAlive = new JsonObject();
                keepAlive.addProperty("janus", "keepalive");
                keepAlive.addProperty("session_id", this.sessionId);
                keepAlive.addProperty("transaction", TxUtils.gen(userID, "keepalive"));
                send(keepAlive);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    private void sendOfferToJanus(RTCSessionDescription sdp) {
        JsonObject jsep = new JsonObject();
        jsep.addProperty("type", "offer");
        jsep.addProperty("sdp", sdp.sdp);

        JsonObject body = new JsonObject();
        body.addProperty("request", "configure");
        body.addProperty("muted", false);

        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "message");
        msg.addProperty("session_id", this.sessionId);
        msg.addProperty("handle_id", this.handleId);
        msg.add("body", body);
        msg.add("jsep", jsep);
        msg.addProperty("transaction", TxUtils.gen(userID, "webrtc_conf"));

        send(msg);
    }

    private void sendIceCandidateToJanus(dev.onvoid.webrtc.RTCIceCandidate candidate) {
        JsonObject candidateJson = new JsonObject();
        candidateJson.addProperty("candidate", candidate.sdp);
        candidateJson.addProperty("sdpMid", candidate.sdpMid);
        candidateJson.addProperty("sdpMLineIndex", candidate.sdpMLineIndex);

        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "trickle");
        msg.addProperty("session_id", this.sessionId);
        msg.addProperty("handle_id", this.handleId);
        msg.add("candidate", candidateJson);
        msg.addProperty("transaction", TxUtils.gen(userID, "ICE_forward"));

        send(msg);
    }

    private void handleRemoteJsep(JsonObject jsep) {
        String sdpText = jsep.get("sdp").getAsString();
        RTCSessionDescription answer = new RTCSessionDescription(RTCSdpType.ANSWER, sdpText);

        this.peerConnection.setRemoteDescription(answer, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                logger.logDebug("WebRTC connection set up");
            }

            @Override
            public void onFailure(String error) {
                logger.logError("setRemoteDescription error: "+ error);
            }
        });
    }

    // jrtc

    private void initWebRTCPeerConnection() {
        try {
            RTCConfiguration config = new RTCConfiguration();

            RTCIceServer stunServer = new RTCIceServer();
            stunServer.urls.add(this.stunServer);
            config.iceServers.add(stunServer);

            this.peerConnection = factory.createPeerConnection(config, this);

            AudioTrack localAudioTrack = factory.createAudioTrack("loc-audio-0", factory.createAudioSource(audioOptions));
            this.peerConnection.addTrack(localAudioTrack, List.of("stream-0-"+ userID));

            RTCOfferOptions options = new RTCOfferOptions();
            this.peerConnection.createOffer(options ,new CreateSessionDescriptionObserver() {
                @Override
                public void onSuccess(RTCSessionDescription sdp) {
                    peerConnection.setLocalDescription(sdp, new SetSessionDescriptionObserver() {
                        @Override
                        public void onSuccess() {
                            sendOfferToJanus(sdp);
                        }
                        @Override
                        public void onFailure(String error) {
                            logger.logError("setLocalDescription error: "+ error);
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    logger.logError("WebRTC offer error: "+ error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Janus signaling channel

    private void sendPluginMessage(JsonObject body, String transaction) {
        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "message");
        msg.addProperty("session_id", this.sessionId);
        msg.addProperty("handle_id", this.handleId);
        msg.add("body", body);
        msg.addProperty("transaction", transaction);
        send(msg);
    }

    private void send(JsonObject json) {
        String jsonString = gson.toJson(json);

        logger.logDebug("OUT: "+jsonString);

        this.webSocket.sendText(jsonString, true);
    }

    // Implementeation of WebSocket.Listener
    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        //TODO: sistemare la selezione

        String jsonString = data.toString();
        JsonObject response = gson.fromJson(jsonString, JsonObject.class);

        logger.logDebug("IN: "+jsonString);

        String janus = response.has("janus") ? response.get("janus").getAsString() : "";
        String tx = response.has("transaction") ? response.get("transaction").getAsString() : "";

        if ("success".equals(janus)) {
            if (response.has("data")) {
                JsonObject dataObj = response.getAsJsonObject("data");
                if (dataObj.has("id") && sessionId == null) {
                    sessionId = dataObj.get("id").getAsLong();
                    keepAlive();
                    attachAudioBridge();
                    return WebSocket.Listener.super.onText(webSocket, data, last);
                } else if (dataObj.has("id") && handleId == null) {
                    handleId = dataObj.get("id").getAsLong();
                    joinRoom();
                    return WebSocket.Listener.super.onText(webSocket, data, last);
                }
            }
        }
        if ("success".equals(janus) || "event".equals(janus)) {
            handlePluginTransaction(tx, response);
        }

        if (response.has("jsep")) {
            handleRemoteJsep(response.getAsJsonObject("jsep"));
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    private void handlePluginTransaction(String tx, JsonObject response) {
        if (!response.has("plugindata")) return;

        JsonObject pluginData = response.getAsJsonObject("plugindata").getAsJsonObject("data");
        if (!pluginData.has("audiobridge")) return;

        //TODO: sistemare la selezione e logica

        String eventType = pluginData.get("audiobridge").getAsString();

        switch (eventType) {
            case "joined": // Conferma di ingresso nella stanza
                if(peerConnection == null) initWebRTCPeerConnection();
                break;

            default:
                break;
        }
    }

    // Implementation of PeerConnectionObserver

    @Override
    public void onIceCandidate(dev.onvoid.webrtc.RTCIceCandidate candidate) {
        sendIceCandidateToJanus(candidate);
    }

    @Override public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {}
    @Override public void onSignalingChange(dev.onvoid.webrtc.RTCSignalingState state) {}
    @Override public void onIceConnectionChange(dev.onvoid.webrtc.RTCIceConnectionState state) {}
    @Override public void onIceConnectionReceivingChange(boolean receiving) {}
    @Override public void onIceGatheringChange(dev.onvoid.webrtc.RTCIceGatheringState state) {}
    @Override public void onIceCandidatesRemoved(dev.onvoid.webrtc.RTCIceCandidate[] candidates) {}
    @Override public void onRemoveTrack(RTCRtpReceiver receiver) {}
    @Override public void onDataChannel(dev.onvoid.webrtc.RTCDataChannel dataChannel) {}
    @Override public void onRenegotiationNeeded() {}
}

