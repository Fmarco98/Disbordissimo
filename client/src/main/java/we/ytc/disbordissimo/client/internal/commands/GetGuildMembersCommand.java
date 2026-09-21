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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>GetGuildMembers Command</h1>
 * Gets a list of all {@code guild} members.
 */
public class GetGuildMembersCommand extends Command {

    public GetGuildMembersCommand(Client c) {
        super("get-guild-member", c);
    }

    @Override
    public int onActionPerformed(String... params) {
        long userID = getClient().getUserID();
        String guildName = params[0];

        JsonObject request = new JsonObject();
        request.addProperty("userID", userID);
        request.addProperty("guild", guildName);
        super.send(request);

        JsonObject response = super.recv();
        int code = response.get("code").getAsInt();
        String msgCode = response.get("msgCode").getAsString();

        switch (code) {
            case ReturnCodes.SUCCESS:
                List<String> result = new ArrayList<>();
                for(JsonElement e : response.get("members").getAsJsonArray()) {
                    result.add(e.getAsString());
                }
                getClient().setLastStringList(result);

                getClient().getLogger().logDebug(msgCode);
                break;

            case ReturnCodes.GUILD_NOT_FOUND:
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
