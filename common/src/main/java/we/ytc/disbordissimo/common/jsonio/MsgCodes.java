package we.ytc.disbordissimo.common.jsonio;

/**
 * <h1>MsgCodes static class</h1>
 */
public class MsgCodes {
    private MsgCodes() {}

    /**
     * {@code SERVER_UNREACHABLE} message.
     */
    public static final String SERVER_UNREACHABLE = "The Disbordissimo server is unreachable";

    //generics
    /**
     * {@code SUCCESS} message.
     */
    public static final String SUCCESS = "Ok";

    /**
     * {@code NO_PERMISSION} message.
     */
    public static final String NO_PERMISSION = "Forbidden";

    /**
     * {@code COMMAND_NOT_FOUND} message.
     */
    public static final String COMMAND_NOT_FOUND = "Command Not Found";

    /**
     * Generic {@code ERROR} message.
     */
    public static final String ERROR = "An error occurred";

    //DB
    // User
    /**
     * {@code USER_NOT_FOUND} message.
     */
    public static final String USER_NOT_FOUND = "The requested user doesn't exists";

    /**
     * {@code USER_ALREADY_EXISTS} message.
     */
    public static final String USER_ALREADY_EXISTS = "An user with that username already exists";

    // Guild
    /**
     * {@code GUILD_NOT_FOUND} message.
     */
    public static final String GUILD_NOT_FOUND = "The requested guild doesn't exists";

    /**
     * {@code GUILD_ALREADY_EXISTS} message.
     */
    public static final String GUILD_ALREADY_EXISTS = "The guild already exists";

    /**
     * {@code GUILD_ALREADY_JOINED} message.
     */
    public static final String GUILD_ALREADY_JOINED = "You've already joined the requested guild";

    // Channel
    /**
     * {@code CHANNEL_NOT_FOUND} message.
     */
    public static final String CHANNEL_NOT_FOUND = "The requested channel doesn't exists";

    /**
     * {@code CHANNEL_ALREADY_EXISTS} message.
     */
    public static final String CHANNEL_ALREADY_EXISTS = "The channel already exists";

    /**
     * {@code CHANNEL_ALREADY_JOINED} message.
     */
    public static final String CHANNEL_ALREADY_JOINED = "You've already joined the requested channel";
}
