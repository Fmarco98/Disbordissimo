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

package we.ytc.disbordissimo.common.logger.exceptions;

/**
 * <h1>Closed Exception</h1>
 *
 * The exception is thrown when it's tried to perform an operation on a closed
 * {@link we.ytc.disbordissimo.common.logger.Logger}.
 */
public class ClosedException extends RuntimeException {

    /**
     * Constructor.
     */
    public ClosedException() {
        super("It's impossible to perform operations on a closed Logger");
    }
}
