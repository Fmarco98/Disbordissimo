package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>SignUp Command</h1>
 * Creates a user.<br>
 * <br>
 * Important: All password are hash before they are sent to the server.
 */
public class SignUpCommand extends Command {

    /**
     * Constructor.
     */
    public SignUpCommand() {
        super("sign-up");
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
                getClient().getLogger().logDebug("user{"+username+"} signed up successfully.");
                return ReturnCodes.SUCCESS;

            case ReturnCodes.USER_ALREADY_EXISTS:
                getClient().getLogger().logDebug("That user already exists");
                return ReturnCodes.USER_ALREADY_EXISTS;

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
