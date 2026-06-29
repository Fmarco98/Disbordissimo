package we.ytc.disbordissimo.client.exceptions;

public class NotLoggedException extends RuntimeException {
    public NotLoggedException() {
        super("No user logged in");
    }
}
