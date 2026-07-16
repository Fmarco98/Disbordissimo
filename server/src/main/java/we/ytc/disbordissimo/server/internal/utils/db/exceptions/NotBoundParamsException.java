package we.ytc.disbordissimo.server.internal.utils.db.exceptions;

/**
 * <h1>NotBoundParamsException Exception</h1>
 *
 * This exception is thrown when there's a mismatch between params and their types.
 */
public class NotBoundParamsException extends RuntimeException {

    /**
     * Constructor.
     */
    public NotBoundParamsException() {
        super("Query params and their types don't match together");
    }
}
