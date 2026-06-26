package we.ytc.disbordissimo.client.utils.socketmanager.exceptions;

public class SocketNotOpenException extends RuntimeException {
    public SocketNotOpenException() {
        super("Socket has not been opened or has been closed");
    }
}
