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

package we.ytc.disbordissimo.common.logger;

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.fm.FileManager;
import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpException;
import we.ytc.disbordissimo.common.logger.exceptions.ClosedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h1>Logger class</h1>
 * It's an implementation of {@link Logger}.<br>
 * A simple Logger. It writes into {@code System.err} or {@code System.out} stream based on the log message level. <br>
 * Feature: <br>
 *  - Write into a logfile (if is set up) <br>
 *  - In-Console colors <br>
 *  - Thread safe <br>
 *
 */
public final class YtcLogger implements Logger {

    // Log colors
    private static final String warningColor = ConsoleColors.YELLOW_BRIGHT;
    private static final String errorColor = ConsoleColors.RED;
    private static final String infoColor = ConsoleColors.WHITE_BRIGHT;
    private static final String debugColor = ConsoleColors.CYAN_BRIGHT;

    private boolean isFileSetUp;
    private boolean isConsolePrintingEnabled;
    private String filepath;
    private FileManager fm;
    private boolean open;

    /**
     * Constructor.
     */
    public YtcLogger() {
        this.isConsolePrintingEnabled = true;
        this.isFileSetUp = false;
        this.open = true;
    }

    /**
     * Constructor.
     *
     * @param inConsolePrint
     *        Allow in console printing
     *
     * @param inLogfilePrint
     *        Allow in logfile printing
     *
     * @throws FileSetUpException
     */
    public YtcLogger(boolean inConsolePrint, boolean inLogfilePrint) throws FileSetUpException {
        this.isConsolePrintingEnabled = inConsolePrint;
        if(inLogfilePrint) {
            this.setUpFile("logs/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))+".log");
        } else {
            this.isFileSetUp = false;
        }
        this.open = true;
    }

    /**
     * Constructor.
     *
     * @param inConsolePrint
     *        Allow in console printing
     *
     * @param filepath
     *        logfile path
     *
     * @throws FileSetUpException
     */
    public YtcLogger(boolean inConsolePrint, String filepath) throws FileSetUpException {
        this.isConsolePrintingEnabled = inConsolePrint;
        this.setUpFile(filepath);
        this.open = true;
    }

    @Override
    public synchronized void log(Type level, String msg) {
        log(level, msg, false);
    }

    @Override
    public synchronized void logln(Type level, String msg) {
        log(level, msg, true);
    }

    @Override
    public synchronized void logMsg(String msg) {
        logln(Type.INFO, msg);
    }

    @Override
    public synchronized void logError(String msg) {
        logln(Type.ERROR, msg);
    }

    @Override
    public synchronized void logDebug(String msg) {
        logln(Type.DEBUG, msg);
    }

    @Override
    public synchronized void logWarning(String msg) {
        logln(Type.WARNING, msg);
    }

    @Override
    public synchronized void log(Type level, String msg, boolean nl) {
        if(this.isClosed()) throw new ClosedException();

        String line = "["+ TimeUtils.getLocalTime()+"]["+this.getThreadID()+"]["+level+"] "+msg;
        line = nl ? line+"\n" : line;

        // file printing
        if (isFileSetUp) {
            try {
                fm.write(line);
            } catch (Exception e) {
                System.err.println("An error occurred while printing on logfile");
            }
        }
        if (isConsolePrintingEnabled) {
            String color = switch (level) {
                case WARNING -> warningColor;
                case DEBUG -> debugColor;
                case INFO -> infoColor;
                case ERROR -> errorColor;
            };

            switch (level) {
                case INFO:
                case DEBUG:
                    System.out.print(color + line + ConsoleColors.RESET);
                    break;

                case WARNING:
                case ERROR:
                    System.err.print(color + line + ConsoleColors.RESET);
                    break;
            }
        }
    }

    @Override
    public synchronized boolean isClosed() {
        return !open;
    }

    @Override
    public synchronized void close() {
        open = false;
        if(fm != null) fm.close();
    }

    /**
     * Gets the {@code logfilepath}.
     *
     * @return filepath
     */
    public synchronized String getLogFilepath() {
        return filepath;
    }

    private void setUpFile(String filepath) throws FileSetUpException {
        this.isFileSetUp = true;
        this.filepath = filepath;
        this.fm = new FileManager(filepath, FileManager.OpenType.APPEND);
    }
    private String getThreadID() {
        // Get Thread ID (Name)
        return Thread.currentThread().getName();
    }
}
