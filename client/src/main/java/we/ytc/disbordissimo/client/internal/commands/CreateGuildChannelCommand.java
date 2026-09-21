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

import com.google.gson.JsonObject;
import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <H1>CreateGuildChannel Command</h1>
 * Creates a voice channel into a specific guild.
 */
public class CreateGuildChannelCommand extends Command {

    public CreateGuildChannelCommand(Client c) {
        super("create-guild-channel", c);
    }

    @Override
    public int onActionPerformed(String... params) {
        long userID = getClient().getUserID();
        String guildName = params[0];
        String channelName = params[1];

        JsonObject request = new JsonObject();
        request.addProperty("userID", userID);
        request.addProperty("guild", guildName);
        request.addProperty("channel", channelName);
        super.send(request);

        JsonObject response = super.recv();
        int code = response.get("code").getAsInt();
        String msgCode = response.get("msgCode").getAsString();

        switch (code) {
            case ReturnCodes.SUCCESS:
                getClient().getLogger().logDebug(msgCode);
                break;

            case ReturnCodes.NO_PERMISSION:
            case ReturnCodes.GUILD_NOT_FOUND:
            case ReturnCodes.CHANNEL_ALREADY_EXISTS:
            case ReturnCodes.COMMAND_NOT_FOUND:
                getClient().getLogger().logWarning(msgCode);
                break;

            case ReturnCodes.ERROR:
                getClient().getLogger().logError("A server error occurred");
                break;

            default:
                getClient().getLogger().logWarning("Unknown response code; response=" + response);
        }
        return code;
    }
}
