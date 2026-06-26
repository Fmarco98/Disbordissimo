package we.ytc.disbordissimo.common.fm.exceptions;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super("Open mode doesn't allow to make that operation");
    }
}