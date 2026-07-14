module client {
    requires common;
    requires kcp.base;
    requires kcp.fec;
    requires io.netty.buffer;
    requires jdk.unsupported;

    exports we.ytc.disbordissimo.client;
    exports we.ytc.disbordissimo.client.exceptions;
}