package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;

public class QuitCommandResponse implements CommandResponse{
    @Override
    public String getCommandName() {
        return "quit";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        long userID = Long.valueOf(params[0]);
        long channelID = Long.valueOf(params[1]);

        Main.getActiveVoiceChannels().quit(channelID, new ActiveUser(userID));

        return JsonIO.genSuccessResponse();
    }
}
