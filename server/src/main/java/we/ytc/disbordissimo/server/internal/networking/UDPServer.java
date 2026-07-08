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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: documentation

/**
 * <h1>UDP Server class</h1>
 *
 */
public class UDPServer extends Thread {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;
    public static final int nThreadPool = 8;

    private boolean running;
    private DatagramSocket server;

    private ExecutorService threadPool;

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

        threadPool = Executors.newFixedThreadPool(nThreadPool);
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

            threadPool.submit(() -> {
                DatagramPacket resp = UDPResponse.response(packet.getAddress(), packet.getPort(), packet.getData().clone());

                if(resp != null) this.send(resp);
            });
        }

        threadPool.close();
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
}
