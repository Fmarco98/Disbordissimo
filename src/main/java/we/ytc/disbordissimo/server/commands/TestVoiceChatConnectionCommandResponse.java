package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.server.Main;

import java.util.List;

public class TestVoiceChatConnectionCommandResponse implements CommandResponse{
    @Override
    public String getCommandName() {
        return "test-voice-chat-connection";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        long userID = Long.valueOf(params[0]);
        long channelID = Long.valueOf(params[1]);

        boolean connected = Main.getActiveVoiceChannels().getVoiceChannel(userID) == channelID;

        return JsonIO.genSuccessResponse(List.of(String.valueOf(connected)));
    }
}
