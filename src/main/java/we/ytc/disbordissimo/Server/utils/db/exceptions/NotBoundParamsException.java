package we.ytc.disbordissimo.Server.utils.db.exceptions;

public class NotBoundParamsException extends RuntimeException {
    public NotBoundParamsException() {
        super("Query params and their types don't match together");
    }
}
