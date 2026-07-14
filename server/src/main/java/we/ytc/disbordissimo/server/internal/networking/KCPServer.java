package we.ytc.disbordissimo.server.internal.networking;

import com.backblaze.erasure.FecAdapt;
import io.netty.buffer.ByteBuf;
import kcp.*;
import we.ytc.disbordissimo.common.AudioUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KCPServer implements KcpListener {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;
    public static final int nThreadPool = 8;

    private KcpServer server;
    private ExecutorService threadPool;

    private int port;

    public KCPServer(int port) {
        this.port = port;

        threadPool = Executors.newFixedThreadPool(nThreadPool);
    }

    public void start() {
        server = this.configServer(port);
    }

    public void close() {
        server.stop();
    }

    @Override
    public void onConnected(Ukcp ukcp) {}

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        //TODO: DECOOMENTARE

//        threadPool.submit(() -> {
//            KCPResponse.response(byteBuf, ukcp);
//        });

        ukcp.write(byteBuf);
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        System.out.println("Connessione chiusa con: " + kcp.user().getRemoteAddress());
    }


    private KcpServer configServer(int port) {
        KcpConfig kcpConfig = new KcpConfig();
        kcpConfig.nodelay(true,40,2,true);
        kcpConfig.setSndwnd(512);
        kcpConfig.setRcvwnd(512);
        kcpConfig.setMtu(512);
//        kcpConfig.setMtu(1536); //1,5kB
        kcpConfig.setAckNoDelay(true);

        ChannelConfig channelConfig = new ChannelConfig(kcpConfig);
        channelConfig.setFecAdapt(new FecAdapt(3,1));
        channelConfig.setTimeoutMillis(10000);
        channelConfig.setUseConvChannel(true);
        channelConfig.setCrc32Check(true);

        return KcpServer.createStarted(channelConfig, this, port);
    }
}