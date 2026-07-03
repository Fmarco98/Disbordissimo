package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PingCommandResponse implements CommandResponse {
    @Override
    public String getCommandName() {
        return "ping";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        try {
            return JsonIO.genSuccessResponse(List.of(String.valueOf(TimeUtils.currentTimestamp())));

        } catch (Exception e) {
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
