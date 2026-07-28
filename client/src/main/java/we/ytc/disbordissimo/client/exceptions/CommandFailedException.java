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

package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>CommandFailed Exception</h1>
 *
 * This exception is thrown when the execution of a {@link we.ytc.disbordissimo.client.internal.commands.Command} ends
 * with a {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes} different by {@code SUCCEESS}.
 */
public class CommandFailedException extends Exception {

    private int errCode;

    /**
     * Constructor.
     * @param errCode
     *        The command exit code
     */
    public CommandFailedException(int errCode) {
        super("Command failed (err="+errCode+")");
        this.errCode = errCode;
    }

    /**
     * Gets the error code.
     *
     * @return {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes}
     */
    public int getErrCode() {
        return errCode;
    }
}
