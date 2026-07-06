package we.ytc.disbordissimo.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h1>TimeUtils static class</h1>
 *
 * Functions:<br>
 *  - getLocalTime()<br>
 *  - currentTimestamp()<br>
 */
public class TimeUtils {
    private TimeUtils(){}

    /**
     * Gets the localTime with the pattern "dd-MM-yyyy HH:mm:ss".
     *
     * @return localTime string
     */
    public static String getLocalTime() {
        // Get local datetime formatted
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    /**
     * Gets the current timestamp.
     *
     * @return timestamp
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
