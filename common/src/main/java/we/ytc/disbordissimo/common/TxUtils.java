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

import java.util.UUID;

public class TxUtils {

    /**
     * Generates a transaction ID. <br>
     * That ID is generated according the format: {@code contest|userID|<random string> }
     *
     * @param userID
     * @param contest
     * @return
     */
    public static String gen(long userID, String contest) {
        return contest +"|"+ userID +"|"+ UUID.randomUUID().toString().substring(0, 8);
    }
}
