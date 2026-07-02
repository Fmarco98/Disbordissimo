package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>CommandFailed Exception</h1>
 *
 * This exception is thrown when the execution of a {@link we.ytc.disbordissimo.client.commands.Command} ends
 * with a {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes} different by {@code SUCCEESS}.
 */
public class CommandFailedException extends Exception {

    private int errCode;

    public CommandFailedException(int errCode) {
        super("Command failed (err="+errCode+")");
        this.errCode = errCode;
    }

    /**
     * Gets the error code.
     *
     * @return {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes}
     */
    public int getErrCode() {
        return errCode;
    }
}
