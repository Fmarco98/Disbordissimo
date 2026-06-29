package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;

//TODO: documentatio

/**
 * <h1>Join / Quit command response</h1>
 */
public class JoinCommandResponse implements CommandResponse{

    @Override
    public String getCommandName() {
        return "join";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        long userID = Long.valueOf(params[0]);
        long channelID = Long.valueOf(params[1]);

        Main.getActiveVoiceChannels().join(channelID, new ActiveUser(userID));

        return JsonIO.genSuccessResponse();
    }
}
