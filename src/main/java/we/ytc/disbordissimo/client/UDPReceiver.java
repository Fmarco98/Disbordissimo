package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.exceptions.IllegalMicFrameSize;
import we.ytc.disbordissimo.common.AudioUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
        if(Client.getOnReceived() == null) return;

        byte[] arrayBuf = new byte[Client.DATAGRAM_PACKET_SIZE];
        byte[] audio = new byte[AudioUtils.MIC_FRAME_LENGTH];
        DatagramPacket packet = new DatagramPacket(arrayBuf, arrayBuf.length);
        ByteBuffer packetBuffer = ByteBuffer.wrap(arrayBuf);

        while(running) {
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                Client.getLogger().logError("An error occurred while receiving a UDP packet");
            }
            // Client.getLogger().logMsg(new String(packet.getData()));
            packetBuffer.rewind();
            long channelID = packetBuffer.getLong();
            packetBuffer.get(audio);

            if(audio.length != AudioUtils.MIC_FRAME_LENGTH) {
                throw new IllegalMicFrameSize(audio.length);
            }
            Client.getOnReceived().onPacketReceived(audio);
        }
    }

    public void stopThread() {
        running = false;

        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}
