package we.ytc.disbordissimo.Client.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import we.ytc.disbordissimo.Client.Main;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * <H1>SignUp Command</h1>
 * Creates a user.<br>
 * <br>
 * {@code onPerformed(..)} implemented with {@code ReturnType} as {@link java.lang.Void}
 */
public class SignUpCommand extends Command<Void>{

    /**
     * Constructor.
     */
    public SignUpCommand() {
        super("sign-up");
    }

    @Override
    public Void onPerformed(Object... params) {
        super.openSocket(Main.Config.TCP_HOST, Main.Config.TCP_PORT);

        HashMap<String, Object> jsonRequestContent = new HashMap<>();
        jsonRequestContent.put("command", this.getCommandName());
        jsonRequestContent.put("params", List.of(params));

        Gson gson = new GsonBuilder().create();
        String request = gson.toJson(jsonRequestContent);

        super.send(request);
        Logger.logMsg(super.recv());

        super.closeSocket();
        return null;
    }
}
