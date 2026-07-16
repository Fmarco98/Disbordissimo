package we.ytc.disbordissimo.client.internal;

import com.backblaze.erasure.FecAdapt;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kcp.*;
import we.ytc.disbordissimo.client.exceptions.IllegalMicFrameSize;
import we.ytc.disbordissimo.common.AudioUtils;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * <h1>KCPClient class</h1>
 *
 * It manages the sending of KCP packets to allow the VoIP communication.<br>
 * The client will call the {@link we.ytc.disbordissimo.client.PacketReceivedHandler} and {@link we.ytc.disbordissimo.client.PacketSendingHandler}
 * handlers.
 */
public class KCPClient implements KcpListener {

    private KcpClient client;

    private Queue<byte[]> frameQueue = new LinkedList<>();
    private Ukcp myUkcp;

    private boolean t_senderRunning;
    /**
     * KCP packets sender thread.
     */
    private Thread sender = new Thread(() -> {
        if(Client.getOnSending() == null) return;

        ByteBuf byteBuf = Unpooled.buffer(Client.DATAGRAM_PACKET_SIZE);
        long userID = Client.getUserID();

        while(t_senderRunning) {

            byte[] micRaw = Client.getOnSending().onPacketSending();
            if(micRaw.length != AudioUtils.MIC_FRAME_LENGTH) {
                throw new IllegalMicFrameSize(micRaw.length);
            }

            byteBuf.writeLong(userID);
            byteBuf.writeBytes(micRaw);

            myUkcp.write(byteBuf);

            byteBuf.clear();
        }
        byteBuf.release();

    }, "sender");

    private boolean t_playerRunning;
    /**
     * Audio player Thread.
     */
    private Thread player = new Thread(() -> {
        if(Client.getOnReceived() == null) return;

        while(t_playerRunning) {
            byte[] buffer = null;
            synchronized (frameQueue) {
                buffer = frameQueue.poll();
            }
            if(buffer == null) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
                continue;
            }

            Client.getOnReceived().onPacketReceived(buffer);
        }
    }, "player");

    /**
     * Constructor.<br>
     * The object construction makes start a KCP connection.
     *
     * @param host
     *        KCP Server host
     *
     * @param port
     *        KCP Server port
     */
    public KCPClient(String host, int port) {
        KcpConfig kcpConfig = new KcpConfig();
        kcpConfig.nodelay(true,10,2,true);
        kcpConfig.setSndwnd(512);
        kcpConfig.setRcvwnd(512);
        kcpConfig.setMtu(1024);
        kcpConfig.setAckNoDelay(true);

        //TODO: trovare identificatore (int) migliore, rischio collisione su long
        kcpConfig.setConv((int) Client.getUserID());

        ChannelConfig channelConfig = new ChannelConfig(kcpConfig);
        channelConfig.setFecAdapt(new FecAdapt(3,1));
        channelConfig.setCrc32Check(true);

        client = new KcpClient(channelConfig);
        client.connect(new InetSocketAddress(host,port),this);
    }

    /**
     * Closes the KCPClient. <br>
     */
    public void close() {
        client.stop();

        try {
            sender.join();
            player.join();
        } catch (InterruptedException e) {}
    }

    @Override
    public void onConnected(Ukcp ukcp) {
        myUkcp = ukcp;
        t_senderRunning = true;
        t_playerRunning = true;
        sender.start();
        player.start();
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        byte[] bufff = new byte[AudioUtils.MIC_FRAME_LENGTH];
        byteBuf.getBytes(8, bufff);

        synchronized (frameQueue) {
            frameQueue.offer(bufff);
        }
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {}

    @Override
    public void handleClose(Ukcp kcp) {
        t_senderRunning = false;
        t_playerRunning = false;
        sender.interrupt();
        player.interrupt();

        try {
            sender.join();
            player.join();
        } catch (InterruptedException e) {}
    }
}
