package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Main;
import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.socketmanager.SocketManager.SocketContainer;

import java.io.IOException;

//TODO: documentation

/**
 * <H1>Quit Command</h1>
 *
 *
 */
public class QuitCommand extends Command<Void>{
    public QuitCommand() {
        super("quit");
    }

    @Override
    public Void onPerformed(Object... params) {
        super.openSocket((SocketContainer) params[0]);
        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), null);
        super.send(JsonIO.serializeReq(request));

        String jsonResponse = super.recv();
        JsonIO.Resp response = JsonIO.deserializeResp(jsonResponse);

        if(response.code != 0) {
            String err = "Command:Quit -> response"+jsonResponse;
            Main.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        Main.getLogger().logDebug("quit ok");

        try {
            super.closeSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
