package we.ytc.disbordissimo.client.internal.commands;

import dev.onvoid.webrtc.media.audio.AudioOptions;
import we.ytc.disbordissimo.client.internal.WebRTCClient;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>JoinChannel Command</h1>
 * Joins a voice channel.
 */
public class JoinChannelCommand extends Command {

    public JoinChannelCommand() {
        super("join");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(getClient().getUserID());
        String guild = params[0];
        String channel = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guild, channel));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:

                AudioOptions o = new AudioOptions();
                o.highpassFilter = false;
                o.noiseSuppression = false;
                o.echoCancellation = false;
                o.autoGainControl = false;

                getClient().setWebRTCClient(new WebRTCClient(
                        getClient().getUserID(),
                        getClient().getUsername(),
                        Integer.valueOf(response.result.get(0)),    // RoomID
                        response.result.get(1),                     // Room pin
                        response.result.get(2),                     // JanusURL
                        response.result.get(3),                     // StunURL
                        o
                ));
                try {
                    getClient().getWebRTCClient().start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

//                getClient().setKCPClient(new KCPClient(
//                    getClient().getConfig().getServerAddress(),
//                    getClient().getConfig().getServerPort(),
//                    getClient()
//                ));

                getClient().getLogger().logDebug("join ok");
                return ReturnCodes.SUCCESS;

            case ReturnCodes.CHANNEL_ALREADY_JOINED:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_ALREADY_JOINED;

            case ReturnCodes.GUILD_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_NOT_FOUND;

            case ReturnCodes.COMMAND_NOT_FOUND:
                getClient().getLogger().logError("An invalid command was sent.");
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
