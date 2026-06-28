package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.common.AudioUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDPSender extends Thread {

    private boolean running;
    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public UDPSender(DatagramSocket socket, InetAddress address, int port) {
        running = true;
        this.socket = socket;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        ByteBuffer packetBuffer = ByteBuffer.allocate(Client.DATAGRAM_PACKET_SIZE);
        packetBuffer.putLong(Client.getUserID());

        while(running) {
            byte[] micRaw = new byte[AudioUtils.MIC_FRAME_LENGTH];

            packetBuffer.position(8);
            packetBuffer.limit(packetBuffer.capacity());
            packetBuffer.put(micRaw);

            packetBuffer.flip();
            try {
                DatagramPacket p = new DatagramPacket(packetBuffer.array(), packetBuffer.array().length, address, port);
                socket.send(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            packetBuffer.flip();
        }
    }

    public void stopThread() {
        running = false;

        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}

