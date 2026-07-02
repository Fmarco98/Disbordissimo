package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.client.internal.UDPReceiver;
import we.ytc.disbordissimo.client.internal.UDPSender;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

//TODO: documentation

/**
 * <H1>JoinChannel Command</h1>
 * Joins a voice channel.
 */
public class JoinChannelCommand extends Command {

    public JoinChannelCommand() {
        super("join");
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

            case ReturnCodes.CHANNEL_ALREADY_JOINED:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_ALREADY_JOINED;

            case ReturnCodes.GUILD_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.GUILD_NOT_FOUND;

            case ReturnCodes.CHANNEL_NOT_FOUND:
                Client.getLogger().logWarning(response.msgCode);
                return ReturnCodes.CHANNEL_NOT_FOUND;

            case ReturnCodes.COMMAND_NOT_FOUND:
                Client.getLogger().logError("An invalid command was sent.");
                return ReturnCodes.COMMAND_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response="+response.toString());
                return ReturnCodes.ERROR;
        }
    }
}
