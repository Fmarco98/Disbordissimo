module server {
    requires com.google.gson;
    requires java.desktop;
    requires java.sql;

    requires common;

    exports we.ytc.disbordissimo.server;

    opens we.ytc.disbordissimo.server to com.google.gson;
}