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
import dev.onvoid.webrtc.media.audio.AudioOptions;
import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.client.internal.WebRTCClient;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <H1>JoinChannel Command</h1>
 * Joins a voice channel.
 */
public class JoinChannelCommand extends Command {

    public JoinChannelCommand(Client c) {
        super("join", c);
    }

    @Override
    public int onActionPerformed(String... params) {
        long userID = getClient().getUserID();
        String guild = params[0];
        String channel = params[1];

        JsonObject request = new JsonObject();
        request.addProperty("userID", userID);
        request.addProperty("guild", guild);
        request.addProperty("channel", channel);
        super.send(request);

        JsonObject response = super.recv();
        int code = response.get("code").getAsInt();
        String msgCode = response.get("msgCode").getAsString();

        switch (code) {
            case ReturnCodes.SUCCESS:

                AudioOptions o = new AudioOptions();
                o.highpassFilter = false;
                o.noiseSuppression = false;
                o.echoCancellation = false;
                o.autoGainControl = false;

                getClient().setWebRTCClient(new WebRTCClient(
                        userID,
                        getClient().getUsername(),
                        response.get("roomID").getAsInt(),
                        response.get("roomPin").getAsString(),
                        response.get("janus").getAsString(),
                        response.get("stun").getAsString(),
                        o,                                          // Audio Options
                        getClient().getEventHandler()               // Client handler
                ).setLogger(getClient().getLogger()));
                getClient().getWebRTCClient().start();

                getClient().getLogger().logDebug(msgCode);
                break;

            case ReturnCodes.CHANNEL_ALREADY_JOINED:
            case ReturnCodes.GUILD_NOT_FOUND:
            case ReturnCodes.CHANNEL_NOT_FOUND:
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
