package we.ytc.disbordissimo.client.exceptions;

public class CommandFailedException extends Exception {

    private int errCode;

    public CommandFailedException(int errCode) {
        super("Command failed (err="+errCode+")");
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
