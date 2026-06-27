package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.audio.AudioUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
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
        Main.getLogger().logDebug("Responding to " + address + ":" + port);

        ByteBuffer recvBytes = ByteBuffer.wrap(rawData);
        long userID = recvBytes.getLong();
        byte[] mic_frame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        recvBytes.position(8);
        recvBytes.get(mic_frame);

        long voiceChannelID = Main.getActiveVoiceChannels().getVoiceChannel(userID);
        if(voiceChannelID == -1) {
            Main.getLogger().logWarning("Responding to an NOT IN VOICE CHAT user(" + address + ":" + port + ")");
            this.closeResponse();
            return;
        }

        //Mix audio
        byte[] mixed_audio = new byte[1];

        ByteBuffer resposnePacket = ByteBuffer.allocate(UDPServer.DATAGRAM_PACKET_SIZE);
        resposnePacket.putLong(voiceChannelID);
        resposnePacket.put(mixed_audio);
        resposnePacket.flip();

        DatagramPacket packet = new DatagramPacket(resposnePacket.array(), resposnePacket.array().length, address, port);
        server.send(packet);

        this.closeResponse();
    }

    private void closeResponse() {
        synchronized (this.activeResponses) {
            this.activeResponses.remove(this);
        }
    }
}
