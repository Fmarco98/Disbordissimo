package we.ytc.disbordissimo.Client.utils.exceptions;

public class SocketNotOpenException extends RuntimeException {
    public SocketNotOpenException() {
        super("Socket has not been opened or has been closed");
    }
}
