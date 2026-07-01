package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class GetGuildOwnerCommandResponse implements CommandResponse {

    private static String GET_OWNER_NAME_QUERY = "SELECT u.username AS username " +
                                                 "FROM users u " +
                                                 "JOIN guilds g ON g.fk_owner = u.id_user " +
                                                 "WHERE g.name = ?;";

    @Override
    public String getCommandName() {
        return "get-owner";
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
            String guildName = params[1];

            try {
                ResultSet queryResult = db.execute(IS_MEMBER_QUERY, "sl", guildName, userID);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                    db.close();
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }
                queryResult.close();

                queryResult = db.execute(GET_OWNER_NAME_QUERY, "s", guildName);
                queryResult.next();
                String owner = queryResult.getString("username");
                queryResult.close();

                db.close();
                return JsonIO.genSuccessResponse(List.of(owner));
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
