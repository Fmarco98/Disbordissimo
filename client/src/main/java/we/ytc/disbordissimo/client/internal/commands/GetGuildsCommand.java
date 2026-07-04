package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>GetGuilds Command</h1>
 * Gets all guilds where the logged user is member.
 */
public class GetGuildsCommand extends Command{

    public GetGuildsCommand() {
        super("get-guilds");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(Client.getUserID());

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.setLastStringList(response.result);
                return ReturnCodes.SUCCESS;

            case ReturnCodes.COMMAND_NOT_FOUND:
                Client.getLogger().logError("An invalid command was sent.");
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
