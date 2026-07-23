package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.client.internal.WebRTCClient;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>QuitChannel Command</h1>
 * Quits from a voice channel.
 */
public class QuitChannelCommand extends Command {
    public QuitChannelCommand() {
        super("quit");
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
                try {
                    getClient().getWebRTCClient().stop();
                    getClient().setWebRTCClient(null);
                    System.gc();
                } catch (NullPointerException ignored) {}

                getClient().getLogger().logDebug("quit ok");
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_NOT_FOUND;

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
