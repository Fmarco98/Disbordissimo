package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

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
        Connection db = DisbordissimoServer.getServer().getDB();
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            //Checks if the user is a guild member
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }
            queryResult.close();

            queryResult = DBUtils.bindParams(db, GET_OWNER_NAME_QUERY, "s", guildName).executeQuery();
            queryResult.next();
            String owner = queryResult.getString("username");
            queryResult.close();

            DBUtils.close(db);
            return JsonIO.genSuccessResponse(List.of(owner));
        } catch (SQLException e) {
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
