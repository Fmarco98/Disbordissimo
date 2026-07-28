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

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>JoinGuild Command</h1>
 * Joins a guild.
 */
public class JoinGuildCommand extends Command {

    public JoinGuildCommand() {
            super("join-guild");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(getClient().getUserID());
        String guildName = params[0];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guildName));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_ALREADY_JOINED:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_ALREADY_JOINED;

            case ReturnCodes.GUILD_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

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
