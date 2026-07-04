package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>GetGuildOwner Command</h1>
 * Gets the owner username of a specific guild.
 */
public class GetGuildOwnerCommand extends Command {

    public GetGuildOwnerCommand() {
        super("get-owner");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(Client.getUserID());
        String guildName = params[0];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guildName));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.setLastStringList(response.result);
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.COMMAND_NOT_FOUND:
                Client.getLogger().logWarning("An invalid command was sent.");
                return ReturnCodes.COMMAND_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response=" + response);
                return ReturnCodes.ERROR;
        }
    }
}
