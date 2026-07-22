package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>Login Command</h1>
 * Login.<br>
 * <br>
 * Important: All password are hash before they are sent to the server.
 */
public class LoginCommand extends Command {

    public LoginCommand() {
        super("login");
    }

    @Override
    public int onActionPerformed(String... params) {
        String username = params[0];
        String passwd = params[1];

        JsonIO.Req request = new JsonIO.Req(
                super.getCommandName(), List.of(username, HashUtils.fromStringToHashedHex(passwd))
        );
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                getClient().getLogger().logDebug("user{"+username+"} logged in successfully.");
                getClient().setUsername(username);
                getClient().setUserID(Long.valueOf(response.result.get(0)));
                return ReturnCodes.SUCCESS;

            case ReturnCodes.USER_NOT_FOUND:
                getClient().getLogger().logWarning("user{"+username+"} hasn't been found.");
                return ReturnCodes.USER_NOT_FOUND;

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
