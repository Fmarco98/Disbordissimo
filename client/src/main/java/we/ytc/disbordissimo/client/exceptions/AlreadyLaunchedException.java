package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>AlreadyLauched Exception</h1>
 *
 * This exception is thrown when the user tries to instantiate another {@link we.ytc.disbordissimo.client.DisbordissimoClient}.<br>
 * Important: Only one instance of {@link we.ytc.disbordissimo.client.DisbordissimoClient} is allowed for each process.
 */
public class AlreadyLaunchedException extends RuntimeException {
    public AlreadyLaunchedException() {
        super("Disbordissimo client has already been launched");
    }
}
