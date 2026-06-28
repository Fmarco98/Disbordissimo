package we.ytc.disbordissimo.common.logger.exceptions;

public class ClosedException extends RuntimeException {
    public ClosedException() {
        super("It's impossible to perform operations on a closed Logger");
    }
}
