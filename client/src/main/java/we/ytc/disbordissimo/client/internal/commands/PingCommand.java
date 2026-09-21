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
import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <h1>Ping Command</h1>
 * Makes a ping to the server.
 */
public class PingCommand extends Command {

    public PingCommand(Client c) {
        super("ping", c);
    }

    @Override
    public int onActionPerformed(String... params) {
        JsonObject request = new JsonObject();

        long t0 = TimeUtils.currentTimestamp();
        super.send(request);
        JsonObject response = super.recv();
        long t1 = TimeUtils.currentTimestamp();

        int code = response.get("code").getAsInt();
        String msgCode = response.get("msgCode").getAsString();

        switch (code) {
            case ReturnCodes.SUCCESS:
                getClient().setLastInt((int)(t1 - t0));

                getClient().getLogger().logDebug(msgCode);
                break;

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
