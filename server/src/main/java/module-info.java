module server {
    requires com.google.gson;
    requires java.desktop;
    requires java.sql;

    requires common;
    requires kcp.fec;
    requires io.netty.buffer;
    requires kcp.base;
    requires jdk.unsupported;

    exports we.ytc.disbordissimo.server;
    exports we.ytc.disbordissimo.server.exceptions;

    opens we.ytc.disbordissimo.server to com.google.gson;
    opens we.ytc.disbordissimo.server.internal to com.google.gson;
}