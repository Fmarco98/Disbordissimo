package we.ytc.disbordissimo.Server.utils.logger;

import we.ytc.disbordissimo.Server.utils.fm.FileManager;
import we.ytc.disbordissimo.Server.utils.fm.exceptions.FileSetUpError;
import we.ytc.disbordissimo.Server.utils.logger.exceptions.FileHasAlreadySetUpException;

import java.io.IOException;
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
 *  - lognl(..)<br>
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

    private static boolean isFileSetUp = false;
    private static String filepath = "";
    private static FileManager fm;

    /**
     * Logs a message (without new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    public static synchronized void log(Type level, String msg) {
        Logger.log(level, msg, false);
    }

    /**
     * Logs a message (with new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    public static synchronized void lognl(Type level, String msg) {
        Logger.log(level, msg, true);
    }

    /**
     * Logs a {@code Logger.Type.INFO} message.
     *
     * @param msg
     *        The message
     */
    public static synchronized void logMsg(String msg) {
        lognl(Type.INFO, msg);
    }

    /**
     * Logs a {@code Logger.Type.ERROR} message.
     *
     * @param msg
     *        The message
     */
    public static synchronized void logError(String msg) {
        lognl(Type.ERROR, msg);
    }

    /**
     * Logs a {@code Logger.Type.DEBUG} message.
     *
     * @param msg
     *        The message
     */
    public static synchronized void logDebug(String msg) {
        lognl(Type.DEBUG, msg);
    }

    /**
     * Logs a {@code Logger.Type.WARNING} message.
     *
     * @param msg
     *        The message
     */
    public static synchronized void logWarning(String msg) {
        lognl(Type.WARNING, msg);
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
    public static synchronized void log(Type level, String msg, boolean nl) {
        String line = "["+Logger.getLocalTime()+"]["+Logger.getThreadID()+"]["+level+"] "+msg;
        line = nl ? line+"\n" : line;

        // file printing
        if (isFileSetUp) {
            try {
                Logger.printToFile(line);
            } catch (Exception e) {
                System.err.println("An error occurred while printing on logfile");
            }
        }

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

    /**
     * Sets up a default logfile. The created logfile will be named with the pattern: yyyy-MM-dd_HH-mm
     *
     * @throws FileSetUpError
     */
    public static synchronized void fileSetUp() throws FileSetUpError {
        Logger.fileSetUp("logs/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))+".log");
    }

    /**
     * Sets up the logfile.
     *
     * @param filepath
     *        Logfile filepath
     *
     * @throws FileSetUpError
     */
    public static synchronized void fileSetUp(String filepath) throws FileSetUpError {
        if(isFileSetUp) {
            throw new FileHasAlreadySetUpException();
        }
        isFileSetUp = true;

        fm = new FileManager(filepath, FileManager.OpenType.APPEND);
    }

    private static String getThreadID() {
        // Get Thread ID (Name)
        return Thread.currentThread().getName();
    }
    private static String getLocalTime() {
        // Get local datetime formatted
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    private static void printToFile(String line) throws IOException {
        fm.write(line);
    }
}
