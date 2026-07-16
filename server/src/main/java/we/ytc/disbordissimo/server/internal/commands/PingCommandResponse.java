package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;

/**
 * <h1>Ping CommandResponse</h1>
 * Logic to respond to the command "ping".
 */
public class PingCommandResponse implements CommandResponse {
    @Override
    public String getCommandName() {
        return "ping";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        try {
            return JsonIO.genSuccessResponse();

        } catch (Exception e) {
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
