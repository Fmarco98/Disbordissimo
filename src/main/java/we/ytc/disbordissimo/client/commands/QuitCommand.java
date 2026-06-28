package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Main;
import we.ytc.disbordissimo.common.JsonIO;

import java.io.IOException;
import java.util.List;

//TODO: documentation

/**
 * <H1>Quit Command</h1>
 *
 *
 */
public class QuitCommand extends Command {
    public QuitCommand() {
        super("quit");
    }

    @Override
    public void onActionPerformed(String... params) {
        String userID = params[0];
        String channel = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, channel));
        super.send(JsonIO.serializeReq(request));

        String jsonResponse = super.recv();
        JsonIO.Resp response = JsonIO.deserializeResp(jsonResponse);

        if(response.code != 0) {
            String err = "Command:Quit -> response"+jsonResponse;
            Main.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        Main.getLogger().logDebug("quit ok");
    }
}
