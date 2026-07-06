package we.ytc.disbordissimo.common.logger.exceptions;

/**
 * <h1>Closed Exception</h1>
 *
 * The exception is thrown when it's tried to perform an operation on a closed
 * {@link we.ytc.disbordissimo.common.logger.Logger}.
 */
public class ClosedException extends RuntimeException {

    /**
     * Constructor.
     */
    public ClosedException() {
        super("It's impossible to perform operations on a closed Logger");
    }
}
