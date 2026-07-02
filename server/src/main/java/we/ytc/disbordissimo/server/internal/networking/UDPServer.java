package we.ytc.disbordissimo.server.internal.networking;

import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.server.DisbordissimoServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

//TODO: documentation

/**
 * <h1>UDP Server class</h1>
 *
 */
public class UDPServer extends Thread {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;

    private boolean running;
    private DatagramSocket server;
    private List<UDPResponse> activeResponses;

    /**
     * Constructor.
     *
     * @param port
     *        UDP Server port
     *
     * @throws SocketException
     */
    public UDPServer(int port) throws SocketException {
        super("UDP-Server");

        server = new DatagramSocket(port);
        server.setSoTimeout(100); // Sett a timeout to avoid deadlocks cause by line.35
        running = true;
        activeResponses = new ArrayList<>();
    }

    @Override
    public void run() {
        byte[] packetBuff = new byte[DATAGRAM_PACKET_SIZE];
        while(running) {
            DatagramPacket packet = new DatagramPacket(packetBuff, packetBuff.length);
            try {
                synchronized (server) {
                    try {
                        server.receive(packet);
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
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
                    DisbordissimoServer.getServer().getLogger().logError("UDPResponses joining: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }

        server.close();
    }

    /**
     * Sends a UDP Datagram.
     * @param packet
     *        Datagram
     */
    public synchronized void send(DatagramPacket packet) {
        synchronized (server) {
            try {
                server.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Stops the UDP server.
     */
    public synchronized void stopSever() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException e) {}
    }

    /**
     * Gets the list of UDP active responses
     *
     * @return UDP active responses
     */
    public synchronized List<UDPResponse> getActiveResponses() {
        return activeResponses;
    }
}
