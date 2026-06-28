package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.JsonIO;

import java.util.List;

//TODO: documentation

/**
 * <H1>Join Command</h1>
 */
public class JoinCommand extends Command {

    public JoinCommand() {
        super("join");
    }

    @Override
    public boolean onActionPerformed(String... params) {
        String userID = params[0];
        String channel = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, channel));
        super.send(JsonIO.serializeReq(request));

        String jsonResponse = super.recv();
        JsonIO.Resp response = JsonIO.deserializeResp(jsonResponse);

        if(response.code != 0) {
            String err = "Command:Join -> response"+jsonResponse;
            Client.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        Client.getLogger().logDebug("join ok");

        return true;
    }
}
