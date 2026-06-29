package we.ytc.disbordissimo.common.jsonio;

public class MsgCodes {
    private MsgCodes() {}

    //generics
    public static final String SUCCESS = "Ok";
    public static final String COMMAND_NOT_FOUND = "Command Not Found";
    public static final String ERROR = "An error occurred";

    //DB
    public static final String USER_ALREADY_EXISTS = "An user with that username already exists";
    public static final String USER_NOT_FOUND = "The requested user isn't contained into the DB";
}
