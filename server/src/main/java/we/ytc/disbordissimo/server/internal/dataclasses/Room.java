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

package we.ytc.disbordissimo.server.internal.dataclasses;

/**
 * <h1>Room dataclass</h1>
 */
public class Room {
    public String pin;
    public String secret;
    public int id;

    public Room(long ID) {
        this(ID, "");
    }
    public Room(long ID, String pin) {
        this(ID, pin, "");
    }
    public Room(long ID, String pin, String secret) {
        this.id = (int) ID; // possible value collision (future fix)
        this.pin = pin;
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "room{id="+id+"}";
    }
}
