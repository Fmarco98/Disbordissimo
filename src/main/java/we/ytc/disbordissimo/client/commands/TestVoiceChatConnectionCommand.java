package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>TestVoiceChatConnection Command</h1>
 * Tests if the user is connected to a specific voice channel.
 */
public class TestVoiceChatConnectionCommand extends Command {

    public TestVoiceChatConnectionCommand() {
        super("test-voice-chat-connection");
    }

    @Override
    public int onActionPerformed(String... params) {
        String channelName = params[0];
        String guildName = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(String.valueOf(Client.getUserID()), channelName, guildName));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.setLastBooleanResult(Boolean.valueOf(response.result.get(0)));
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_NOT_FOUND;

            case ReturnCodes.COMMAND_NOT_FOUND:
                Client.getLogger().logWarning("An invalid command was sent.");
                return ReturnCodes.COMMAND_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response="+response.toString());
                return ReturnCodes.ERROR;
        }
    }
}
