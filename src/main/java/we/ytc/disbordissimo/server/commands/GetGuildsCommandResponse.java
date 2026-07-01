package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBManager;

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
        DBManager db = null;
        try {
            db = new DBManager(
                    Main.getConfig().sqlConnectionConfig.host,
                    Main.getConfig().sqlConnectionConfig.user,
                    Main.getConfig().sqlConnectionConfig.password,
                    Main.getConfig().sqlConnectionConfig.dbName
            );

            long userID = Long.valueOf(params[0]);

            try {
                ResultSet queryResult = db.execute(GET_GUILDS, "l", userID);
                List<String> result = new ArrayList<>();

                while(queryResult.next()) {
                    result.add(queryResult.getString("guildname"));
                }
                queryResult.close();

                db.close();
                return JsonIO.genSuccessResponse(result);
            } catch (SQLException e) {
                db.close();
                Main.getLogger().logError("SQL error occurred: " + e.getMessage());
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            db.close();
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
