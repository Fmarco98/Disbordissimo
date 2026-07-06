package we.ytc.disbordissimo.common.jsonio;

/**
 * <h1>ReturnCodes static class</h1>
 */
public class ReturnCodes {
    private ReturnCodes() {}

    /**
     * {@code SERVER_UNREACHABLE} return code.
     */
    public static final int SERVER_UNREACHABLE = -1;

    //generics (0 <= err < 1000)
    /**
     * {@code SUCCESS} return code.
     */
    public static final int SUCCESS = 0;

    /**
     * {@code NO_PERMISSION} return code.
     */
    public static final int NO_PERMISSION = 403;

    /**
     * {@code COMMAND_NOT_FOUND} return code.
     */
    public static final int COMMAND_NOT_FOUND = 404;

    /**
     * Generic {@code ERROR} return code.
     */
    public static final int ERROR = 500;

    // DB error codes (1000 <= err < 2000)
    // User (1000 <= err < 1099)
    /**
     * {@code USER_NOT_FOUND} return code.
     */
    public static final int USER_NOT_FOUND = 1001;

    /**
     * {@code USER_ALREADY_EXISTS} return code.
     */
    public static final int USER_ALREADY_EXISTS = 1002;

    //Guild (1100 <= err < 1199)
    /**
     * {@code GUILD_NOT_FOUND} return code.
     */
    public static final int GUILD_NOT_FOUND = 1101;

    /**
     * {@code GUILD_ALREADY_EXISTS} return code.
     */
    public static final int GUILD_ALREADY_EXISTS = 1102;

    /**
     * {@code GUILD_ALREADY_JOINED} return code.
     */
    public static final int GUILD_ALREADY_JOINED = 1110;

    //Channel (1200 <= err < 1299)
    /**
     * {@code CHANNEL_NOT_FOUND} return code.
     */
    public static final int CHANNEL_NOT_FOUND = 1201;

    /**
     * {@code CHANNEL_ALREADY_EXISTS} return code.
     */
    public static final int CHANNEL_ALREADY_EXISTS = 1202;

    /**
     * {@code CHANNEL_ALREADY_JOINED} return code.
     */
    public static final int CHANNEL_ALREADY_JOINED = 1210;
}
