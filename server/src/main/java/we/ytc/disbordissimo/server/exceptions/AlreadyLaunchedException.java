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

package we.ytc.disbordissimo.server.exceptions;

/**
 * <h1>AlreadyLauched Exception</h1>
 *
 * This exception is thrown when the user tries to instantiate another {@link we.ytc.disbordissimo.server.DisbordissimoServer}.<br>
 * Important: Only one instance of {@link we.ytc.disbordissimo.server.DisbordissimoServer} is allowed for each process.
 */
public class AlreadyLaunchedException extends RuntimeException {

    /**
     * Constructor.
     */
    public AlreadyLaunchedException() {
        super("Disbordissimo Server has already been launched");
    }
}
