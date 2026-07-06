package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>UnreachableServer Exception</h1>
 *
 * This exception is thrown when is impossible to connect to the {@code DisbordissimoServer}
 */
public class UnreachableServerException extends RuntimeException {

    /**
     * Constructor.
     */
    public UnreachableServerException() {
        super("The Disbordissimo server is unreachable.");
    }
}
