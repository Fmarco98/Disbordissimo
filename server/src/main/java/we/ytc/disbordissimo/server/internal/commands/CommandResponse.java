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

package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;

/**
 * <h1>Command response interface</h1>
 *
 * This interface represents every TCP command response. Each command is defined by a name,
 * to handle the command the method {@code getCommandName} must be equal.
 */
public interface CommandResponse {

    /**
     * Gets the command name.
     * @return command name
     */
    String getCommandName();

    /**
     * Performs the command response.
     *
     * @param params
     *        {@link JsonIO.Req} params
     *
     * @return {@link JsonIO.Resp}
     */
    JsonIO.Resp onPerformed(String ...params);
}
