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

package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>Login Command</h1>
 * Login.<br>
 * <br>
 * Important: All password are hash before they are sent to the server.
 */
public class LoginCommand extends Command {

    public LoginCommand() {
        super("login");
    }

    @Override
    public int onActionPerformed(String... params) {
        String username = params[0];
        String passwd = params[1];

        JsonIO.Req request = new JsonIO.Req(
                super.getCommandName(), List.of(username, HashUtils.fromStringToHashedHex(passwd))
        );
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                getClient().getLogger().logDebug("user{"+username+"} logged in successfully.");
                getClient().setUsername(username);
                getClient().setUserID(Long.valueOf(response.result.get(0)));
                return ReturnCodes.SUCCESS;

            case ReturnCodes.USER_NOT_FOUND:
                getClient().getLogger().logWarning("user{"+username+"} hasn't been found.");
                return ReturnCodes.USER_NOT_FOUND;

            case ReturnCodes.COMMAND_NOT_FOUND:
                getClient().getLogger().logWarning("An invalid command was sent.");
                return ReturnCodes.COMMAND_NOT_FOUND;

            case ReturnCodes.ERROR:
                getClient().getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                getClient().getLogger().logWarning("Unknown response code; response=" + response);
                return ReturnCodes.ERROR;
        }
    }
}
