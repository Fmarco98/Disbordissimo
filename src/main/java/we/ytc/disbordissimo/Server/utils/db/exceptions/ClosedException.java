package we.ytc.disbordissimo.Server.utils.db.exceptions;

public class ClosedException extends RuntimeException {
    public ClosedException() {
        super("DB manager has been closed");
    }
}
