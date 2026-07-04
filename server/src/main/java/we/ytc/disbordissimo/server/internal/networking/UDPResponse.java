package we.ytc.disbordissimo.server.internal.networking;

import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.server.internal.ActiveUser;
import we.ytc.disbordissimo.server.DisbordissimoServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

//TODO: documentation

/**
 * <h1>UDP Response</h1>
 *
 *
 */
public class UDPResponse extends Thread {

    private byte[] rawData;
    private List<UDPResponse> activeResponses;
    private UDPServer server;
    private InetAddress address;
    private int port;

    /**
     * Constructor.
     *
     * @param address
     *        Client address
     *
     * @param port
     *        Client port
     *
     * @param rawData
     *        Data received from client
     *
     * @param server
     *        UDP server
     */
    public UDPResponse(InetAddress address, int port, byte[] rawData, UDPServer server) {
        super("UDP-Response");

        this.activeResponses = server.getActiveResponses();
        synchronized (this.activeResponses) {
            this.activeResponses.add(this);
        }
        this.rawData = rawData;
        this.address = address;
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {
        DisbordissimoServer.getServer().getLogger().logDebug("Responding to " + address + ":" + port);

        ByteBuffer recvBytes = ByteBuffer.wrap(rawData);
        long userID = recvBytes.getLong();
        byte[] mic_frame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        recvBytes.position(8);
        recvBytes.get(mic_frame);

        long voiceChannelID = DisbordissimoServer.getServer().getActiveVoiceChannels().getVoiceChannel(userID);
        if(voiceChannelID == -1) {
            DisbordissimoServer.getServer().getLogger().logWarning(
                    "Responding to an NOT IN VOICE CHAT user(" + address + ":" + port + ")"
            );
            this.closeResponse();
            return;
        }

        List<ActiveUser> connectedUsers = DisbordissimoServer.getServer().getActiveVoiceChannels().getConnectedUsers(voiceChannelID);
        List<byte[]> streams = new ArrayList<>();
        synchronized (connectedUsers) {
            connectedUsers.stream().forEach(user -> {
                if (user.getUserID() != userID) {
                    streams.add(user.getMicFrame());
                } else {
                    user.setMicFrame(mic_frame);
                }
            });
        }

        //Mix audio

        byte[] mixed_audio = streams.size() > 0 ? AudioUtils.mixListOfStreams(streams) : new byte[AudioUtils.MIC_FRAME_LENGTH];

        ByteBuffer responsePacket = ByteBuffer.allocate(UDPServer.DATAGRAM_PACKET_SIZE);
        responsePacket.putLong(voiceChannelID);
        responsePacket.put(mixed_audio);
        responsePacket.flip();

        DatagramPacket packet = new DatagramPacket(responsePacket.array(), responsePacket.array().length, address, port);
        server.send(packet);

        this.closeResponse();
    }

    private void closeResponse() {
        synchronized (this.activeResponses) {
            this.activeResponses.remove(this);
        }
    }
}
