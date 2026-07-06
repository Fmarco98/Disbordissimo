package we.ytc.disbordissimo.common.fm.exceptions;

import java.io.IOException;

/**
 * <h1>FileSetUp Exception</h1>
 *
 * The exception is thrown when an error occurred while setting up the file.
 */
public class FileSetUpException extends IOException {
    public FileSetUpException() {
        super("Impossible to create a file");
    }
}
