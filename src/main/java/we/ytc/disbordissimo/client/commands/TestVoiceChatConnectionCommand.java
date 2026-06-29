package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.util.List;

public class TestVoiceChatConnectionCommand extends Command {

    public TestVoiceChatConnectionCommand() {
        super("test-voice-chat-connection");
    }

    @Override
    public int onActionPerformed(String... params) {
        String channelID = params[0];

        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), List.of(String.valueOf(Client.getUserID()), channelID));
        super.send(JsonIO.serializeReq(request));

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                boolean connected = Boolean.valueOf(response.result.get(0));
                Client.setLastBooleanResult(connected);
                Client.getLogger().logDebug("The user("+Client.getUserID()+") is "+ (connected ? "": "not") +" connected to "+channelID);
                return 0;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("An Server error occurred");
                return 1;

            default:
                Client.getLogger().logWarning("Unhandled response code; response="+response.toString());
                return 1;
        }
    }
}
