package we.ytc.disbordissimo.Client.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import we.ytc.disbordissimo.Client.Main;
import we.ytc.disbordissimo.Common.HashUtils;
import we.ytc.disbordissimo.Common.JsonIO;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

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

        JsonIO.Req req = new JsonIO.Req("sign-up", List.of(user, HashUtils.fromStringToHashedHex(pswd)));
        Gson gson = new GsonBuilder().create();
        String request = gson.toJson(req);

        super.send(request);
        Logger.logMsg(super.recv());

        super.closeSocket();
        return null;
    }
}
