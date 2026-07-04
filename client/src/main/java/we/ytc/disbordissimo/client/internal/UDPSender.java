package we.ytc.disbordissimo.client.internal;

import we.ytc.disbordissimo.client.PacketSendingHandler;
import we.ytc.disbordissimo.client.exceptions.IllegalMicFrameSize;
import we.ytc.disbordissimo.common.AudioUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * <h1>UDPSender class</h1>
 *
 * UDP Sender Thread. It sends UDP Packets through UDP socket.
 * Every time that a packet must be sent, the {@link PacketSendingHandler} function is called before.
 */
public class UDPSender extends Thread {

    private boolean running;
    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    private int sleep;

    /**
     * Constructor.
     *
     * @param socket
     *        The UDP socket
     * @param address
     *        The UDP Server address
     * @param port
     *        The UDP Server port
     */
    public UDPSender(DatagramSocket socket, InetAddress address, int port) {
        running = true;
        this.socket = socket;
        this.address = address;
        this.port = port;

        sleep = (1000 * AudioUtils.MIC_FRAME_LENGTH) / (Client.getConfig().getKbps() * 1024);
    }

    @Override
    public void run() {
        if(Client.getOnSending() == null) return;

        ByteBuffer packetBuffer = ByteBuffer.allocate(Client.DATAGRAM_PACKET_SIZE);
        while(running) {
            byte[] micRaw = Client.getOnSending().onPacketSending();
            if(micRaw.length != AudioUtils.MIC_FRAME_LENGTH) {
                throw new IllegalMicFrameSize(micRaw.length);
            }

            packetBuffer.putLong(Client.getUserID());
            packetBuffer.put(micRaw);

            packetBuffer.flip();
            try {
                DatagramPacket p = new DatagramPacket(packetBuffer.array(), packetBuffer.array().length, address, port);
                socket.send(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            packetBuffer.flip();
            packetBuffer.clear();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Stops the Thread.
     */
    public void stopThread() {
        running = false;
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}

