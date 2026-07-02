package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>CreateGuild Command</h1>
 * Creates a guild.
 */
public class CreateGuildCommand extends Command {

    public CreateGuildCommand() {
        super("create-guild");
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
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_ALREADY_EXISTS:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_ALREADY_EXISTS;

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
