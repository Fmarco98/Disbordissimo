package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

public class LoginCommand extends Command {

    public LoginCommand() {
        super("login");
    }

    @Override
    public int onActionPerformed(String... params) {
        String username = params[0];
        String passwd = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(username, HashUtils.fromStringToHashedHex(passwd)));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.getLogger().logDebug("user{"+username+"} logged in successfully.");
                Client.setUserID(Long.valueOf(response.result.get(0)));
                return ReturnCodes.SUCCESS;

            case ReturnCodes.USER_NOT_FOUND:
                Client.getLogger().logWarning("user{"+username+"} hasn't been found.");
                return ReturnCodes.USER_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response="+response.toString());
                return ReturnCodes.ERROR;
        }
    }
}
