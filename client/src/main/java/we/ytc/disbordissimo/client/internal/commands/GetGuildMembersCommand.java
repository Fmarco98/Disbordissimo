package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <h1>GetGuildMembers Command</h1>
 * Gets a list of all {@code guild} members.
 */
public class GetGuildMembersCommand extends Command {

    public GetGuildMembersCommand() {
        super("get-guild-member");
    }

    @Override
    public int onActionPerformed(String... params) {
        String userID = String.valueOf(getClient().getUserID());
        String guildName = params[0];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guildName));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                getClient().setLastStringList(response.result);
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                getClient().getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

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
