package we.ytc.disbordissimo.client.exceptions;

public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("No user logged in");
    }
}
