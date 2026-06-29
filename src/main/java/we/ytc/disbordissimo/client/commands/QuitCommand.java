package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

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
    public int onActionPerformed(String... params) {
        String userID = params[0];
        String channel = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, channel));
        super.send(JsonIO.serializeReq(request));

        String jsonResponse = super.recv();
        JsonIO.Resp response = JsonIO.deserializeResp(jsonResponse);

        if(response.code != ReturnCodes.SUCCESS) {
            String err = "Command:Quit -> response"+jsonResponse;
            Client.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        Client.getSenderThread().stopThread();
        Client.getReceiverThread().stopThread();
        Client.getSocket().close();

        Client.getLogger().logDebug("quit ok");

        return ReturnCodes.SUCCESS;
    }
}
