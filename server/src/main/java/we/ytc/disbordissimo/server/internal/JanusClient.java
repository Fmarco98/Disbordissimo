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

package we.ytc.disbordissimo.server.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.dataclasses.Room;
import we.ytc.disbordissimo.common.TxUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.*;
import java.util.concurrent.*;

/**
 * <h1>Janus Client class</h1>
 * This is a Janus API client.
 * <br>
 * <br>
 * Janus: https://github.com/meetecho/janus-gateway
 */
public class JanusClient implements WebSocket.Listener {
    private static final Gson gson = new Gson();
    
    private int clientID;
    private Long sessionId;
    private Long handleId;
    private WebSocket webSocket;
    
    private ScheduledExecutorService keepAlive;
    private Map<String, CompletableFuture<JsonObject>> activeRequests;

    /**
     * Constructor.
     *
     * @param clientID
     *        ID
     * @param janusUrl
     *        Janus server URL
     */
    public JanusClient(int clientID, String janusUrl) {
        this.clientID = clientID;
        activeRequests = new TreeMap<>();
        
        HttpClient client = HttpClient.newHttpClient();
        this.webSocket = client.newWebSocketBuilder()
                .subprotocols("janus-protocol")
                .buildAsync(URI.create(janusUrl), this)
                .join();
        
        createSession();
    }

    /**
     * Creates a room.
     *
     * @param id
     *        Room ID
     *
     * @return {@link Room} if operation completed successfully;
     *         {@code null} otherwise
     */
    public Room createRoom(long id) {
        String pin = genRandomPswd();
        String secret = genRandomPswd();
        String tx = this.createRoom0(id, pin, secret);

        return ( waitComplete(tx).get("audiobridge").getAsString().equals("created") ?
                new Room(id, pin, secret) : null
        );
    }

    /**
     * Destroys a Room.
     *
     * @param room
     *        {@link Room}
     *
     * @return {@code true} if operation completed successfully;
     *         {@code false} otherwise
     */
    public boolean destroyRoom(Room room) {
        if(room == null) return false;

        String tx = this.destroyRoom0(room.id, room.secret);

        return waitComplete(tx).get("audiobridge").getAsString().equals("destroyed");
    }

    /**
     * Lists the room's participants.
     *
     * @param room
     *        {@link Room}
     *
     * @return List of users' ID
     */
    public List<Long> listParticipants(Room room) {
        List<Long> members = new ArrayList<>();
        if(room == null) return members;

        String tx = this.listParticipants0(room.id);
        JsonObject resp = waitComplete(tx);

        if(!resp.get("audiobridge").getAsString().equals("participants")) return members;

        resp.get("participants").getAsJsonArray().forEach(e -> {
            JsonObject o = e.getAsJsonObject();

            members.add(o.get("id").getAsLong());
        });

        return members;
    }

    /**
     * Close the client.
     */
    public void close() {
        if(!keepAlive.isShutdown()) keepAlive.shutdown();

        activeRequests.clear();
        activeRequests.notifyAll();

        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye").join();
        }
        System.gc();
    }
    
    
    // janus communication

    private void createSession() {
        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "create");
        msg.addProperty("transaction", TxUtils.gen(clientID, "session_create"));
        send(msg);
    }

    private void attachAudioBridge() {
        JsonObject msg = new JsonObject();
        msg.addProperty("janus", "attach");
        msg.addProperty("session_id", this.sessionId);
        msg.addProperty("plugin", "janus.plugin.audiobridge");
        msg.addProperty("transaction", TxUtils.gen(clientID, "audio-bridge_attach"));
        send(msg);
    }

    private void keepAlive() {
        keepAlive = Executors.newSingleThreadScheduledExecutor();
        keepAlive.scheduleAtFixedRate(() -> {
            if (webSocket != null && sessionId != null) {
                JsonObject keepAlive = new JsonObject();
                keepAlive.addProperty("janus", "keepalive");
                keepAlive.addProperty("session_id", this.sessionId);
                keepAlive.addProperty("transaction", TxUtils.gen(clientID, "keepalive"));
                send(keepAlive);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    private String createRoom0(long id, String pin, String secret) {
        JsonObject body = new JsonObject();

        body.addProperty("request", "create");
        body.addProperty("room", id);
        body.addProperty("permanent", false);
        body.addProperty("secret", secret);
        body.addProperty("pin", pin);
        body.addProperty("denoise", true);

        String tx = TxUtils.gen(clientID, "room_create");
        sendPluginMessage(body, tx);
        return tx;
    }

    private String destroyRoom0(int id, String secret) {
        JsonObject body = new JsonObject();

        body.addProperty("request", "destroy");
        body.addProperty("room", id);
        body.addProperty("secret", secret);

        String tx = TxUtils.gen(clientID, "room_destroy");
        sendPluginMessage(body, tx);
        return tx;
    }

    private String listParticipants0(int id) {
        JsonObject body = new JsonObject();

        body.addProperty("request", "listparticipants");
        body.addProperty("room", id);

        String tx = TxUtils.gen(clientID, "list_room_participants");
        sendPluginMessage(body, tx);
        return tx;
    }


    // Implementation of WebSocket.Listener
    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        //TODO: sistemare la selezione

        String jsonString = data.toString();
        JsonObject response = gson.fromJson(jsonString, JsonObject.class);

        DisbordissimoServer.getServer().getLogger().logDebug("IN: "+jsonString);

        String janus = response.has("janus") ? response.get("janus").getAsString() : "";
        String tx = response.get("transaction").getAsString();

        if ("success".equals(janus)) {
            if (response.has("data")) {
                JsonObject dataObj = response.getAsJsonObject("data");
                if (sessionId == null && dataObj.has("id")) {
                    sessionId = dataObj.get("id").getAsLong();
                    keepAlive();
                    attachAudioBridge();

                } else if (handleId == null && dataObj.has("id")) {
                    handleId = dataObj.get("id").getAsLong();
                }
            }
        }
        if (("success".equals(janus) || "event".equals(janus)) && response.has("plugindata")) {
            JsonObject pluginData = response.getAsJsonObject("plugindata")
                    .getAsJsonObject("data");

            if (pluginData.has("audiobridge")) {
                completeRequest(tx, pluginData);
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }
        }

        completeRequest(tx, response);
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

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
        synchronized (activeRequests) {
            activeRequests.put(json.get("transaction").getAsString(), new CompletableFuture<>());
        }
        String jsonString = gson.toJson(json);

        DisbordissimoServer.getServer().getLogger().logDebug("OUT: "+jsonString);

        this.webSocket.sendText(jsonString, true);
    }

    private void completeRequest(String tx, JsonObject content) {
        synchronized (activeRequests) {
            activeRequests.get(tx).complete(content);
        }
    }

    // Returns true, if it's successfully completed
    private JsonObject waitComplete(String tx) {
        CompletableFuture<JsonObject> future;
        synchronized (activeRequests) {
            future = activeRequests.get(tx);
        }

        try {
            return future.get();
        } catch (Exception e) {
            return null;
        } finally {
            synchronized (activeRequests) {
                activeRequests.remove(tx);
            }
        }
    }

    private String genRandomPswd() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
