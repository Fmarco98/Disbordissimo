module client {
    requires common;
    requires jdk.unsupported;
    requires java.net.http;
    requires com.google.gson;
    requires webrtc.java;

    exports we.ytc.disbordissimo.client;
    exports we.ytc.disbordissimo.client.exceptions;
}