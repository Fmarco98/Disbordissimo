package we.ytc.disbordissimo.client.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.JsonIO;

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
    public boolean onActionPerformed(String... params) {
        String user = params[0];
        String pswd = params[1];

        JsonIO.Req req = new JsonIO.Req(super.getCommandName(), List.of(user, HashUtils.fromStringToHashedHex(pswd)));
        Gson gson = new GsonBuilder().create();
        String request = gson.toJson(req);

        super.send(request);
        Client.getLogger().logMsg(super.recv());

        return true;
    }
}
