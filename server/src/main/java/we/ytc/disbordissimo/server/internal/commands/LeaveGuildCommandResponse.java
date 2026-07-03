package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static we.ytc.disbordissimo.server.internal.commands.CreateGuildChannelCommandResponse.IS_OWNER;
import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class LeaveGuildCommandResponse implements CommandResponse {

    private static String LEAVE_GUILD_QUERY = "DELETE FROM users_guilds " +
                                              "WHERE fk_user = ? AND fk_guild = ( " +
                                              "    SELECT id_guild " +
                                              "    FROM guilds " +
                                              "    WHERE name = ? " +
                                              ")";

    @Override
    public String getCommandName() {
        return "leave-guild";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

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

            //Checks if the user isn't the owner (the owner can't leave the guild, but can drop it)
            queryResult = DBUtils.bindParams(db, IS_OWNER, "ls", userID, guildName).executeQuery();
            queryResult.last();
            if (queryResult.getRow() == 1 && queryResult.getBoolean("owner")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.NO_PERMISSION, MsgCodes.NO_PERMISSION, null);
            }
            queryResult.close();

            DBUtils.startTransaction(db);
            int affectedRows = DBUtils.bindParams(db, LEAVE_GUILD_QUERY, "ls", userID, guildName).executeUpdate();
            if (affectedRows != 1) {
                throw new Exception("Deleted an unaspected number of rows");
            }

            DBUtils.commit(db);
            DBUtils.close(db);
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            if(db != null) {
                DBUtils.rollback(db);
                DBUtils.close(db);
            }
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
