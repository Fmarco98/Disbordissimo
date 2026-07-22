module client {
    requires common;
    requires kcp.base;
    requires kcp.fec;
    requires io.netty.buffer;
    requires jdk.unsupported;
    requires java.net.http;
    requires com.google.gson;
    requires webrtc.java;
    requires org.slf4j;

    exports we.ytc.disbordissimo.client;
    exports we.ytc.disbordissimo.client.exceptions;
}