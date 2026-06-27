package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.audio.AudioUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class UDPServer extends Thread {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;

    private boolean open;
    private DatagramSocket server;
    private List<UDPResponse> activeResponses;

    public UDPServer(int port) throws SocketException {
        server = new DatagramSocket(port);
        open = true;
        activeResponses = new LinkedList<>();
    }

    @Override
    public void run() {
        byte[] packetBuff = new byte[DATAGRAM_PACKET_SIZE];
        while(open) {
            DatagramPacket packet = new DatagramPacket(packetBuff, packetBuff.length);
            try {
                synchronized (server) {
                    server.receive(packet);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            UDPResponse response = new UDPResponse(packet.getAddress(), packet.getPort(), packet.getData().clone(), this);
            response.start();
        }

        synchronized (activeResponses) {
            activeResponses.stream().forEach(response -> {
                try {
                    response.join();
                } catch (InterruptedException e) {
                    Main.getLogger().logError("UDPResponses joining: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }

        server.close();
    }

    public synchronized void send(DatagramPacket packet) {
        synchronized (server) {
            try {
                server.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopSever() {
        open = false;
    }

    public List<UDPResponse> getActiveResponses() {
        return activeResponses;
    }
}
