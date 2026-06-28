package we.ytc.disbordissimo.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver extends Thread {

    private boolean running;
    private DatagramSocket socket;

    public UDPReceiver(DatagramSocket socket) {
        running = true;
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] packetBuffer = new byte[Client.DATAGRAM_PACKET_SIZE];

        while(running) {
            DatagramPacket packet = new DatagramPacket(packetBuffer, packetBuffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                Client.getLogger().logError("An error occurred while receiving a UDP packet");
            }
            Main.getLogger().logMsg(new String(packet.getData()));
        }
    }

    public void stopThread() {
        running = false;

        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}
