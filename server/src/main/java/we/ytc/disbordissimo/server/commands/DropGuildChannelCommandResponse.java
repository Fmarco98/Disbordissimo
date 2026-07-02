package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static we.ytc.disbordissimo.server.commands.CreateGuildChannelCommandResponse.IS_OWNER;
import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class DropGuildChannelCommandResponse implements CommandResponse {

    private static String DROP_CHANNEL = "DELETE FROM channels " +
                                         "WHERE name = ? AND fk_guild = ( " +
                                         "    SELECT id_guild " +
                                         "    FROM guilds " +
                                         "    WHERE name = ? " +
                                         ")";

    @Override
    public String getCommandName() {
        return "drop-guild-channel";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];
            String channelName = params[2];

            //Checks if the user is a guild member
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }
            queryResult.close();

            //Checks if the user is the owner
            queryResult = DBUtils.bindParams(db, IS_OWNER, "ls", userID, guildName).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("owner")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.NO_PERMISSION, MsgCodes.NO_PERMISSION, null);
            }
            queryResult.close();

            DBUtils.startTransaction(db);
            int affectedRows = DBUtils.bindParams(db, DROP_CHANNEL, "ss", channelName, guildName).executeUpdate();
            if(affectedRows == 0) {
                DBUtils.rollback(db);
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
            } else if (affectedRows > 1) {
                throw new Exception("Deleted too many rows");
            }

            DBUtils.commit(db);
            DBUtils.close(db);
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            Main.getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
