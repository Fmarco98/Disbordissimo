package we.ytc.disbordissimo.common.fm.exceptions;

/**
 * <h1>Closed Exception</h1>
 *
 * The exception is thrown when it's tried to perform an operation on a closed
 * {@link we.ytc.disbordissimo.common.fm.FileManager}.
 */
public class ClosedException extends RuntimeException {

    /**
     * Constructor.
     */
    public ClosedException() {
        super("FileManager has been closed");
    }
}
