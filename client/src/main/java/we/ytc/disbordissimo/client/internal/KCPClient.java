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

public class KCPClient implements KcpListener {

    private KcpClient client;

    private Queue<byte[]> frameQueue = new LinkedList<>();
    private Ukcp myUkcp;

    private Thread sender = new Thread(() -> {
        if(Client.getOnSending() == null) return;

        ByteBuf byteBuf = Unpooled.buffer(Client.DATAGRAM_PACKET_SIZE);
        long userID = Client.getUserID();

        while(!Thread.currentThread().isInterrupted()) {

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

    private Thread player = new Thread(() -> {
        if(Client.getOnReceived() == null) return;

        while(!Thread.currentThread().isInterrupted()) {
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

    public KCPClient(String host, int port) {
        KcpConfig kcpConfig = new KcpConfig();
        kcpConfig.nodelay(true,40,2,true);
        kcpConfig.setSndwnd(512);
        kcpConfig.setRcvwnd(512);
        kcpConfig.setMtu(512);
//        kcpConfig.setMtu(1536); //1,5kB
        kcpConfig.setAckNoDelay(true);
        kcpConfig.setConv(56);

        ChannelConfig channelConfig = new ChannelConfig(kcpConfig);
        channelConfig.setFecAdapt(new FecAdapt(3,1));
        channelConfig.setCrc32Check(true);
        //channelConfig.setTimeoutMillis(10000);
        //channelConfig.setAckMaskSize(32);
        client = new KcpClient(channelConfig);

        client.connect(new InetSocketAddress(host,port),this);
    }

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
        sender.start();
        player.start();
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        byte[] bufff = new byte[AudioUtils.MIC_FRAME_LENGTH];
        byteBuf.getBytes(8, bufff);

        System.out.println("dsadsadadadadadasdasd");
        synchronized (frameQueue) {
            frameQueue.offer(bufff);
        }
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {}

    @Override
    public void handleClose(Ukcp kcp) {
        sender.interrupt();
        player.interrupt();

        try {
            sender.join();
            player.join();
        } catch (InterruptedException e) {}
    }
}
