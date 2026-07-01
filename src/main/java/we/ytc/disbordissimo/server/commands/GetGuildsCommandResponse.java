package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

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
        Connection db = Main.getDB();
        try {
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
            Main.getLogger().logError("SQL error occurred: " + e);
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
