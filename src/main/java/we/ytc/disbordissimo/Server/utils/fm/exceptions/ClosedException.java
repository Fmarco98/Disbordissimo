package we.ytc.disbordissimo.Server.utils.fm.exceptions;

public class ClosedException extends RuntimeException {
    public ClosedException() {
        super("FileManager has been closed");
    }
}
