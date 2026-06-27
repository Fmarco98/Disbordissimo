package we.ytc.disbordissimo.client.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import we.ytc.disbordissimo.client.Main;
import we.ytc.disbordissimo.common.HashUtils;
import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.logger.Logger;

import java.io.IOException;
import java.util.List;

/**
 * <H1>SignUp Command</h1>
 * Creates a user.<br>
 * <br>
 * {@code onPerformed(..)} implemented with {@code ReturnType} as {@link java.lang.Void}
 */
public class SignUpCommand extends Command<Void> {

    /**
     * Constructor.
     */
    public SignUpCommand() {
        super("sign-up");
    }

    @Override
    public Void onPerformed(Object... params) {
        super.openSocket(Main.Config.TCP_HOST, Main.Config.TCP_PORT);

        String user = params[0].toString();
        String pswd = params[1].toString();

        JsonIO.Req req = new JsonIO.Req(super.getCommandName(), List.of(user, HashUtils.fromStringToHashedHex(pswd)));
        Gson gson = new GsonBuilder().create();
        String request = gson.toJson(req);

        super.send(request);
        Main.getLogger().logMsg(super.recv());

        try {
            super.closeSocket();
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
