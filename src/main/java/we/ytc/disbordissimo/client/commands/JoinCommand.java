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
 * <H1>Join Command</h1>
 */
public class JoinCommand extends Command {

    public JoinCommand() {
        super("join");
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
            String err = "Command:Join -> response"+jsonResponse;
            Client.getLogger().logError(err);
            throw new RuntimeException(err);
        }

        try {
            Client.setSocket(new DatagramSocket());
            Client.setReceiverThread(new UDPReceiver(Client.getSocket()));
            Client.setSenderThread(new UDPSender(Client.getSocket(), Client.getConfig().getServerAddress(),
                                    Client.getConfig().getServerPort()));
            Client.getReceiverThread().start();
            Client.getSenderThread().start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        Client.getLogger().logDebug("join ok");

        return ReturnCodes.SUCCESS;
    }
}
