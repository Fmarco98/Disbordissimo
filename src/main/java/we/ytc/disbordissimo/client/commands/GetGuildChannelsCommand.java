package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

public class GetGuildChannelsCommand extends Command {

    public GetGuildChannelsCommand() {
        super("get-guild-channel");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(Client.getUserID());
        String guildname = params[0];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guildname));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.setLastStringList(response.result);
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logError(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

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
