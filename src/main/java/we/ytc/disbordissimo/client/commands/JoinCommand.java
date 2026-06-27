package we.ytc.disbordissimo.client.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import we.ytc.disbordissimo.client.Main;
import we.ytc.disbordissimo.common.socketmanager.SocketManager.SocketContainer;
import we.ytc.disbordissimo.common.JsonIO;

import java.util.List;

//TODO: documentation

/**
 * <H1>Join Command</h1>
 */
public class JoinCommand extends Command<SocketContainer>{

    public JoinCommand() {
        super("join");
    }

    @Override
    public SocketContainer onPerformed(Object... params) {
        super.openSocket(Main.Config.TCP_HOST, Main.Config.TCP_PORT);
        String userID = params[0].toString();
        String channel = params[1].toString();

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, channel));
        super.send(JsonIO.serializeReq(request));

        String jsonResponse = super.recv();
        JsonIO.Resp response = JsonIO.deserializeResp(jsonResponse);

        if(response.code != 0) {
            String err = "Command:Join -> response"+jsonResponse;
            Main.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        Main.getLogger().logDebug("join ok");

        // Returns the socket to use for the QuitCommand.
        // Reason: A project design choice is to use the TCP connection to check if the client is
        // or isn't still connected.
        return this.getSocketManager().getSocketContainer();
    }
}
