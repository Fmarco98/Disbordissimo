package we.ytc.disbordissimo.common.jsonio;

public class MsgCodes {
    private MsgCodes() {}

    public static final String SERVER_UNREACHABLE = "The Disbordissimo server is unreachable";
    //generics
    public static final String SUCCESS = "Ok";
    public static final String NO_PERMISSION = "Forbidden";
    public static final String COMMAND_NOT_FOUND = "Command Not Found";
    public static final String ERROR = "An error occurred";

    //DB
    // User
    public static final String USER_NOT_FOUND = "The requested user doesn't exists";
    public static final String USER_ALREADY_EXISTS = "An user with that username already exists";

    // Guild
    public static final String GUILD_NOT_FOUND = "The requested guild doesn't exists";
    public static final String GUILD_ALREADY_EXISTS = "The guild already exists";

    public static final String GUILD_ALREADY_JOINED = "You've already joined the requested guild";

    // Channel
    public static final String CHANNEL_NOT_FOUND = "The requested channel doesn't exists";
    public static final String CHANNEL_ALREADY_EXISTS = "The channel already exists";
    public static final String CHANNEL_ALREADY_JOINED = "You've already joined the requested channel";
}
