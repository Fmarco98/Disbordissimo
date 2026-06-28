package we.ytc.disbordissimo.client.exceptions;

/** //TODO: documentation
 *
 */
public class AlreadyLaunchedException extends RuntimeException {
    public AlreadyLaunchedException() {
        super("Disbordissimo client has already been launched");
    }
}
