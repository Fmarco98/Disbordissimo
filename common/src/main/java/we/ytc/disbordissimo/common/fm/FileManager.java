/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.common.fm;

import we.ytc.disbordissimo.common.fm.exceptions.ClosedException;
import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpException;
import we.ytc.disbordissimo.common.fm.exceptions.NoPermissionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>FileManager class</h1>
 * An interaction Interface to ASCII files only.<br>
 * A file can be opened with {@code FileManager.OpenType} <br><br>
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
     * @throws FileSetUpException
     */
    public FileManager(String filepath, OpenType openType) throws FileSetUpException {
        this.filepath = filepath;
        this.openType = openType;

        this.file = new File(this.filepath);
        if(!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            } catch (IOException e) {
                throw new FileSetUpException();
            }
        }
    }

    /**
     * Reads all the file's lines.
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
     *
     * @param index
     *        Line index (the first one is 0)
     *
     * @return the line if it exists;
     *         {@code null} otherwise
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
     * Writes a message into the file. The operation is possible only if the file was opened with the right method.
     *
     * @param msg
     *        The message to write
     *
     * @throws IOException
     */
    public void write(String msg) throws IOException {
        this.checkIsClosed();
        if (!(this.openType == OpenType.WRITE || this.openType == OpenType.READAPPEND
                || this.openType == OpenType.READWRITE || this.openType == OpenType.APPEND)) {
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
     * Clears the file by deleting its content. The operation is possible only if the file was opened with the right method.
     */
    public void clear() {
        this.checkIsClosed();
        if (!(this.openType == OpenType.WRITE || this.openType == OpenType.READAPPEND
                || this.openType == OpenType.READWRITE || this.openType == OpenType.APPEND)) {
            throw new NoPermissionException();
        }

        try {
            this.printToFile("", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the {@code FileManager}. When closed, it's no longer possible to perform any operation.
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
