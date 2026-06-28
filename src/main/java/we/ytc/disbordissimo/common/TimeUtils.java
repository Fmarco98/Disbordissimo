package we.ytc.disbordissimo.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private TimeUtils(){}

    public static String getLocalTime() {
        // Get local datetime formatted
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
