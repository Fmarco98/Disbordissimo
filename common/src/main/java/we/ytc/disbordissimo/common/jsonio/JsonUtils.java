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

package we.ytc.disbordissimo.common.jsonio;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * <h1>JsonUtils class</h1>
 *
 * It contains static functions that are useful to cast standard Java Types into JSON friendly types.<br>
 *
 * Functions:<br>
 *  - toJsonArray(..)
 */
public class JsonUtils {

    /**
     * Casts a {@code List<String>} into a {@code JsonArray} object.
     *
     * @param list
     *        List of strings
     *
     * @return {@link JsonArray}
     */
    public static JsonArray toJsonArray(List<String> list) {
        JsonArray array = new JsonArray(list.size());

        list.forEach(e -> {
            array.add(e);
        });

        return array;
    }
}
