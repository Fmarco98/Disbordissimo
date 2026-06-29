package we.ytc.disbordissimo.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPReceiver extends Thread {

    private boolean running;
    private DatagramSocket socket;

    public UDPReceiver(DatagramSocket socket) {
        running = true;
        this.socket = socket;
        try {
            this.socket.setSoTimeout(Client.getConfig().getUDPTimeOut());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        byte[] packetBuffer = new byte[Client.DATAGRAM_PACKET_SIZE];

        while(running) {
            DatagramPacket packet = new DatagramPacket(packetBuffer, packetBuffer.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                Client.getLogger().logError("An error occurred while receiving a UDP packet");
            }
           // Client.getLogger().logMsg(new String(packet.getData()));

            //write into audiostream

        }
    }

    public void stopThread() {
        running = false;

        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}
