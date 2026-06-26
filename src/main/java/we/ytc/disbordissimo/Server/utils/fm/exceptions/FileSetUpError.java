package we.ytc.disbordissimo.Server.utils.fm.exceptions;

import java.io.IOException;

public class FileSetUpError extends IOException {
    public FileSetUpError() {
        super("Impossible to create a file");
    }
}
