package we.ytc.disbordissimo.client.exceptions;

/** //TODO: documentation
 *
 *
 */
public class UnreachableServerException extends RuntimeException {

    /**
     * Constructor.
     */
    public UnreachableServerException() {
        super("The Disbordissimo server is unreachable.");
    }
}
