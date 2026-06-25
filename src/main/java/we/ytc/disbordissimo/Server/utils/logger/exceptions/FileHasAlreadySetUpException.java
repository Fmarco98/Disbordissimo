package we.ytc.disbordissimo.Server.utils.logger.exceptions;

/**
 * <h1>File has already set up Exception</h1>
 * <p>The log file has already been set up.</p>
 *
 * <h6>File: LoggerExceptions.java</h6>
 * <h6>Path: LoggerExceptions.FileHasAlreadySetUpException</h6>
 */
public class FileHasAlreadySetUpException extends RuntimeException {
    public FileHasAlreadySetUpException() {
        super("Log file has already been set up.");
    }
}

