package we.ytc.disbordissimo.common.jsonio;

public class ReturnCodes {
    private ReturnCodes() {}

    //generics (0 <= err < 1000)
    public static final int SUCCESS = 0;
    public static final int COMMAND_NOT_FOUND = 404;
    public static final int ERROR = 500;

    // DB error codes (1000 <= err < 2000)
    public static final int USER_ALREADY_EXISTS = 1001;
    public static final int USER_NOT_FOUND = 1002;
}
