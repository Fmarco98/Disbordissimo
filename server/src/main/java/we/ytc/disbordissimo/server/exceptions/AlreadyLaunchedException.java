package we.ytc.disbordissimo.server.exceptions;


public class AlreadyLaunchedException extends RuntimeException {
    public AlreadyLaunchedException() {
        super("Disbordissimo Server has already been launched");
    }
}
