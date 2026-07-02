package we.ytc.disbordissimo.common.fm.exceptions;

public class ClosedException extends RuntimeException {
    public ClosedException() {
        super("FileManager has been closed");
    }
}
