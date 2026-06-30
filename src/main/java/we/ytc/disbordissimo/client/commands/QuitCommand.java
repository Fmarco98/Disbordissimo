package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.client.UDPReceiver;
import we.ytc.disbordissimo.client.UDPSender;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.net.DatagramSocket;
import java.net.SocketException;
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
        String userID = String.valueOf(Client.getUserID());
        String guild = params[0];
        String channel = params[1];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(userID, guild, channel));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                Client.getSenderThread().stopThread();
                Client.getReceiverThread().stopThread();
                Client.getSocket().close();

                Client.getLogger().logDebug("quit ok");
                return ReturnCodes.SUCCESS;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response="+response.toString());
                return ReturnCodes.ERROR;
        }
    }

    public int osnActionPerformed(String... params) {
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



        return ReturnCodes.SUCCESS;
    }
}
