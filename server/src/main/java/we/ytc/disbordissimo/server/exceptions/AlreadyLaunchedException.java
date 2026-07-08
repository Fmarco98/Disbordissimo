package we.ytc.disbordissimo.server.exceptions;

/**
 * <h1>AlreadyLauched Exception</h1>
 *
 * This exception is thrown when the user tries to instantiate another {@link we.ytc.disbordissimo.server.DisbordissimoServer}.<br>
 * Important: Only one instance of {@link we.ytc.disbordissimo.server.DisbordissimoServer} is allowed for each process.
 */
public class AlreadyLaunchedException extends RuntimeException {

    /**
     * Constructor.
     */
    public AlreadyLaunchedException() {
        super("Disbordissimo Server has already been launched");
    }
}
