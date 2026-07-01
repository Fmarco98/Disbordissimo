package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

public class CreateGuildChannelCommand extends Command {

    public CreateGuildChannelCommand() {
        super("create-guild-channel");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(Client.getUserID());
        String guildName = params[0];
        String channelName = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guildName, channelName));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                return ReturnCodes.SUCCESS;

            case ReturnCodes.NO_PERMISSION:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.NO_PERMISSION;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_ALREADY_EXISTS:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_ALREADY_EXISTS;

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
