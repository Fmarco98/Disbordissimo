package we.ytc.disbordissimo.common.fm.exceptions;

/**
 * <h1>NoPermission Exception</h1>
 *
 * The exception is thrown when it's tried to perform an operation on a file opened with an
 * {@link we.ytc.disbordissimo.common.fm.FileManager.OpenType} which doesn't allow that operation.
 */
public class NoPermissionException extends RuntimeException {

    /**
     * Constructor.
     */
    public NoPermissionException() {
        super("Open mode doesn't allow to make that operation");
    }
}