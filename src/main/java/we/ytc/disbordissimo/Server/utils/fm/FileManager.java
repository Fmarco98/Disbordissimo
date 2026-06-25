package we.ytc.disbordissimo.Server.utils.fm;

import we.ytc.disbordissimo.Server.utils.fm.exceptions.ClosedException;
import we.ytc.disbordissimo.Server.utils.fm.exceptions.FileSetUpError;
import we.ytc.disbordissimo.Server.utils.fm.exceptions.NoPermissionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>FileManager class</h1>
 * An interaction Interface to only ASCII files.<br>
 * A file can be opened with all {@code FileManager.OpenType} <br><br>
 * Methods:<br>
 *  - constructor(..)<br>
 *  - readAll(..)<br>
 *  - readLine(..)<br>
 *  - write(..)<br>
 *  - clear(..)<br>
 *  - close(..)<br>
 *  <br>
 *  It's suggested to close the FileManager at the end of use (To avoid resource leaks).
 */
public class FileManager {

    /**
     * <h1>OpenType enum</h1>
     * Enum of possible file open types:<br>
     *  - READ<br>
     *  - WRITE<br>
     *  - APPEND<br>
     *  - READWRITE<br>
     *  - READAPPEND<br>
     */
    public enum OpenType {
        WRITE,
        APPEND,
        READ,
        READWRITE,
        READAPPEND
    }

    private String filepath;
    private OpenType openType;
    private File file;
    private boolean isClosed = false;

    /**
     * Constructor.
     *
     * @param filepath
     *        File
     * @param openType
     *        File open method
     *
     * @throws FileSetUpError
     */
    public FileManager(String filepath, OpenType openType) throws FileSetUpError {
        this.filepath = filepath;
        this.openType = openType;

        this.file = new File(this.filepath);
        if(!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            } catch (IOException e) {
                throw new FileSetUpError();
            }
        }
    }

    /**
     * Reads all file lines.
     *
     * @return list of lines
     */
    public List<String> readAll() {
        this.checkIsClosed();
        if (!(this.openType == OpenType.READ || this.openType == OpenType.READAPPEND || this.openType == OpenType.READWRITE)) {
            throw new NoPermissionException();
        }

        try {
            Scanner fr = new Scanner(this.file);
            ArrayList<String> lines = new ArrayList<>();

            while(fr.hasNextLine()) {
                lines.add(fr.nextLine());
            }

            return lines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the {@code index} line of the file.
     * If you call this function multiple times, maybe you should consider to call {@code readAll} (Efficiency suggestion).
     *
     * @param index
     *        Line index (starts from 0)
     *
     * @return the line if it exists;
     *         null otherwise
     */
    public String readLine(int index) {
        this.checkIsClosed();
        if (!(this.openType == OpenType.READ || this.openType == OpenType.READAPPEND || this.openType == OpenType.READWRITE)) {
            throw new NoPermissionException();
        }

        try {
            Scanner fr = new Scanner(this.file);

            while(fr.hasNextLine()) {
                if(index == 0) return fr.nextLine();
                index--;
            }

            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a message into the file. The operation is possible only if you opened the file with the right method.
     *
     * @param msg
     *        The message to write
     *
     * @throws IOException
     */
    public void write(String msg) throws IOException {
        this.checkIsClosed();
        if (!(this.openType == OpenType.WRITE || this.openType == OpenType.READAPPEND || this.openType == OpenType.READWRITE || this.openType == OpenType.APPEND)) {
            throw new NoPermissionException();
        }

        switch (this.openType) {
            case WRITE:
            case READWRITE:
                this.printToFile(msg, false);
                break;
            case APPEND:
            case READAPPEND:
                this.printToFile(msg, true);
                break;
        }
    }

    /**
     * Clears the file, delete its content. The operation is possible only if you opened the file with the right method.
     */
    public void clear() {
        this.checkIsClosed();
        if (!(this.openType == OpenType.WRITE || this.openType == OpenType.READAPPEND || this.openType == OpenType.READWRITE || this.openType == OpenType.APPEND)) {
            throw new NoPermissionException();
        }

        try {
            this.printToFile("", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the {@code FileManager}. When it was closed, you won't be able to perform any operation.
     */
    public void close() {
        this.checkIsClosed();

        this.isClosed = true;

        this.file = null;
        this.openType = null;
        this.filepath = null;
        System.gc();
    }

    private void printToFile(String msg, boolean append) throws IOException {
        FileWriter fw = new FileWriter(this.file, append);
        fw.write(msg);
        fw.close();
    }
    private void checkIsClosed() {
        if(this.isClosed) {
            throw new ClosedException();
        }
    }
}
