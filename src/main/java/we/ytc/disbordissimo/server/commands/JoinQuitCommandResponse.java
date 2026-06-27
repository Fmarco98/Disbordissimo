package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.socketmanager.SocketManager;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;

//TODO: documentatio

/**
 * <h1>Join / Quit command response</h1>
 */
public class JoinQuitCommandResponse implements CommandResponse{
    private static final JsonIO.Resp successResp = new JsonIO.Resp(JsonIO.SUCCESS_CODE, JsonIO.SUCCESS_MSG, null);

    @Override
    public String getCommandName() {
        return "join";
    }

    // Join and Quit Commands will use the same socket.
    // Reason: A project design choice is to use the TCP connection to check if the client is
    // or isn't still connected.
    @Override
    public JsonIO.Resp onPerformed(SocketManager socket, String... params) {
        long userID = Long.valueOf(params[0]);
        long channelID = Long.valueOf(params[1]);
        ActiveUser user = new ActiveUser(userID);
        Main.getActiveVoiceChannels().join(channelID, user);
        socket.send(JsonIO.serializeResp(successResp));

        try {
            while(true) {
                JsonIO.Req request = JsonIO.deserializeReq(socket.recv());
                if(request.cmdName.equals("quit")) {
                    Main.getActiveVoiceChannels().quit(channelID, user);
                    break;
                }
            }
        } catch (RuntimeException e) {
            Main.getActiveVoiceChannels().quit(channelID, user);
            throw e;
        }

        return successResp;
    }
}
