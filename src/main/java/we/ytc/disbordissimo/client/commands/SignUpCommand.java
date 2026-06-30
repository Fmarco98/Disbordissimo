package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

/**
 * <H1>SignUp Command</h1>
 * Creates a user.<br>
 * <br>
 * {@code onPerformed(..)} implemented with {@code ReturnType} as {@link java.lang.Void}
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

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(username, HashUtils.fromStringToHashedHex(passwd)));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.getLogger().logDebug("user{"+username+"} signed up successfully.");
                return ReturnCodes.SUCCESS;

            case ReturnCodes.USER_ALREADY_EXISTS:
                Client.getLogger().logDebug("That user already exists");
                return ReturnCodes.USER_ALREADY_EXISTS;

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
