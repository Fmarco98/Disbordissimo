package we.ytc.disbordissimo.common.jsonio;

public class ReturnCodes {
    private ReturnCodes() {}

    //
    public static final int SERVER_UNREACHABLE = -1;

    //generics (0 <= err < 1000)
    public static final int SUCCESS = 0;
    public static final int NO_PERMISSION = 403;
    public static final int COMMAND_NOT_FOUND = 404;
    public static final int ERROR = 500;

    // DB error codes (1000 <= err < 2000)
    // User (1000 <= err < 1099)
    public static final int USER_NOT_FOUND = 1001;
    public static final int USER_ALREADY_EXISTS = 1002;

    //Guild (1100 <= err < 1199)
    public static final int GUILD_NOT_FOUND = 1101;
    public static final int GUILD_ALREADY_EXISTS = 1102;

    public static final int GUILD_ALREADY_JOINED = 1110;

    //Channel (1200 <= err < 1299)
    public static final int CHANNEL_NOT_FOUND = 1201;
    public static final int CHANNEL_ALREADY_EXISTS = 1202;

    public static final int CHANNEL_ALREADY_JOINED = 1210;

}
