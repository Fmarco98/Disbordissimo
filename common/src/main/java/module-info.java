module common {
    requires com.google.gson;

    exports we.ytc.disbordissimo.common;
    exports we.ytc.disbordissimo.common.fm;
    exports we.ytc.disbordissimo.common.fm.exceptions;
    exports we.ytc.disbordissimo.common.jsonio;
    exports we.ytc.disbordissimo.common.logger;
    exports we.ytc.disbordissimo.common.logger.exceptions;

    opens we.ytc.disbordissimo.common.jsonio to com.google.gson;
}