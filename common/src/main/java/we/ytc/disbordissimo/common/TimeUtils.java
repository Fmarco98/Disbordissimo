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
