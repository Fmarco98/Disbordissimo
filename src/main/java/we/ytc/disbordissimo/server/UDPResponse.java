package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.audio.AudioUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.List;

public class UDPResponse extends Thread {

    private byte[] rawData;
    private List<UDPResponse> activeResponses;
    private UDPServer server;
    private InetAddress address;
    private int port;

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
        byte[] packetBuff = new byte[UDPServer.DATAGRAM_PACKET_SIZE];

        ByteBuffer recvBytes = ByteBuffer.wrap(rawData);
        long userID = recvBytes.getLong();
        byte[] mic_frame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        recvBytes.position(8);
        recvBytes.get(mic_frame);

        String s = new String(mic_frame);

        Main.getLogger().logMsg(String.valueOf(userID));
        Main.getLogger().logMsg(s);

        DatagramPacket packet = new DatagramPacket(packetBuff, packetBuff.length, address, port);
        server.send(packet);

        synchronized (this.activeResponses) {
            this.activeResponses.remove(this);
        }
    }
}
