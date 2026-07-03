package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetGuildsCommandResponse implements CommandResponse{

    private final String GET_GUILDS = "SELECT guildname " +
                                      "FROM user_guild_byname " +
                                      "WHERE member = ? " +
                                      "GROUP BY guildname;";

    @Override
    public String getCommandName() {
        return "get-guilds";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            long userID = Long.valueOf(params[0]);

            ResultSet queryResult = DBUtils.bindParams(db, GET_GUILDS, "l", userID).executeQuery();
            List<String> result = new ArrayList<>();

            while(queryResult.next()) {
                result.add(queryResult.getString("guildname"));
            }
            queryResult.close();

            DBUtils.close(db);
            return JsonIO.genSuccessResponse(result);
        } catch (SQLException e) {
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            if(db != null) DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
