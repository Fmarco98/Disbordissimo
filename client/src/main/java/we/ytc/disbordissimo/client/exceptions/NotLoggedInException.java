package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>NotLoggedIn Exception</h1>
 *
 * This exception is thrown when an unlogged in user tries to perform any operation
 * (!= {@link we.ytc.disbordissimo.client.internal.commands.SignUpCommand}).
 */
public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("No user logged in");
    }
}
