package we.ytc.disbordissimo.common.logger;

import we.ytc.disbordissimo.common.fm.FileManager;
import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h1>Logger class</h1>
 * A simple Logger. It writes into serr and sout stream based on the log message level. <br>
 * Feature: <br>
 *  - Write into a logfile (if is set up) <br>
 *  - In-Console colors <br>
 *  - Thread safe <br>
 * <br>
 * Log message level are defined in {@link Logger.Type}
 * <br><br>
 * Functions:<br>
 *  - log(..)<br>
 *  - logln(..)<br>
 *  - logMsg(..)<br>
 *  - logDebug(..)<br>
 *  - logWarning(..)<br>
 *  - logError(..)<br>
 *  - fileSetUp(..)<br>
 *
 */
public final class Logger {

    /**
     * <h1>Log Types enum</h1>
     * Message log level types:<br>
     * - INFO<br>
     * - DEBUG<br>
     * - WARNING<br>
     * - ERROR<br>
     */
    public enum Type {
        INFO("INFO"),
        ERROR("ERROR"),
        WARNING("WARNING"),
        DEBUG("DEBUG");

        private String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    // Log colors
    private static final String warningColor = ConsoleColors.YELLOW_BRIGHT;
    private static final String errorColor = ConsoleColors.RED;
    private static final String infoColor = ConsoleColors.WHITE_BRIGHT;
    private static final String debugColor = ConsoleColors.CYAN_BRIGHT;

    private boolean isFileSetUp;
    private boolean isConsolePrintingEnabled;
    private String filepath;
    private FileManager fm;

    public Logger() {
        this.isConsolePrintingEnabled = true;
        this.isFileSetUp = false;
    }

    public Logger(boolean inConsolePrint, boolean inLogfilePrint) throws FileSetUpError {
        this.isConsolePrintingEnabled = inConsolePrint;
        if(inLogfilePrint) {
            this.setUpFile("logs/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))+".log");
        } else {
            this.isFileSetUp = false;
        }
    }

    public Logger(boolean inConsolePrint, String filepath) throws FileSetUpError {
        this.isConsolePrintingEnabled = inConsolePrint;
        this.setUpFile(filepath);
    }

    /**
     * Logs a message (without new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    public synchronized void log(Type level, String msg) {
        log(level, msg, false);
    }

    /**
     * Logs a message (with new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    public synchronized void logln(Type level, String msg) {
        log(level, msg, true);
    }

    /**
     * Logs a {@code Logger.Type.INFO} message.
     *
     * @param msg
     *        The message
     */
    public synchronized void logMsg(String msg) {
        logln(Type.INFO, msg);
    }

    /**
     * Logs a {@code Logger.Type.ERROR} message.
     *
     * @param msg
     *        The message
     */
    public synchronized void logError(String msg) {
        logln(Type.ERROR, msg);
    }

    /**
     * Logs a {@code Logger.Type.DEBUG} message.
     *
     * @param msg
     *        The message
     */
    public synchronized void logDebug(String msg) {
        logln(Type.DEBUG, msg);
    }

    /**
     * Logs a {@code Logger.Type.WARNING} message.
     *
     * @param msg
     *        The message
     */
    public synchronized void logWarning(String msg) {
        logln(Type.WARNING, msg);
    }

    /**
     * Logs a message. The log operation is performed at the given {@code level}.
     * If {@code nl} is true, the {@code msg} will be printed with a new line char.
     *
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     * @param nl
     *        New line flag
     */
    public synchronized void log(Type level, String msg, boolean nl) {
        String line = "["+this.getLocalTime()+"]["+this.getThreadID()+"]["+level+"] "+msg;
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
                    System.out.print(color + line);
                    break;

                case WARNING:
                case ERROR:
                    System.err.print(color + line);
                    break;
            }
        }
    }

    private void setUpFile(String filepath) throws FileSetUpError {
        this.isFileSetUp = true;
        this.filepath = filepath;
        this.fm = new FileManager(filepath, FileManager.OpenType.APPEND);
    }
    private String getThreadID() {
        // Get Thread ID (Name)
        return Thread.currentThread().getName();
    }
    private String getLocalTime() {
        // Get local datetime formatted
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
