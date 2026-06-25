package we.ytc.disbordissimo.Server.utils.fm.exceptions;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super("Open mode doesn't allow you to make that operation");
    }
}