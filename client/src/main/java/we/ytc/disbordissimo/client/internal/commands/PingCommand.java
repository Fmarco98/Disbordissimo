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

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <h1>Ping Command</h1>
 * Makes a ping to the server.
 */
public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public int onActionPerformed(String... params) {

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), null);
        String jsonRequest = JsonIO.serializeReq(request);

        long t0 = TimeUtils.currentTimestamp();
        super.send(jsonRequest);
        String r = super.recv();
        long t1 = TimeUtils.currentTimestamp();

        JsonIO.Resp response = JsonIO.deserializeResp(r);
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                getClient().getPingThread().setLastPing((int)(t1 - t0));
                return ReturnCodes.SUCCESS;

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
