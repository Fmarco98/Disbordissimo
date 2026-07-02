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

public class CreateGuildChannelCommandResponse implements CommandResponse {

    protected static String IS_OWNER = "SELECT fk_owner = ? as owner " +
                                     "FROM guilds " +
                                     "WHERE name = ?; ";

    protected static String INSERT_CHANNEL = "INSERT INTO channels(name, fk_guild) VALUES " +
                                           "( ?, ( " +
                                           "    SELECT id_guild " +
                                           "    FROM guilds " +
                                           "    WHERE name = ? " +
                                           "))";

    @Override
    public String getCommandName() {
        return "create-guild-channel";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        int nQuery = 0;
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];
            String channelName = params[2];

            //Checks if the user is a guild member
            nQuery = 1;
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }
            queryResult.close();

            //Checks if the user has the permission to do the operation
            nQuery = 2;
            queryResult = DBUtils.bindParams(db, IS_OWNER, "ls", userID, guildName).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("owner")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.NO_PERMISSION, MsgCodes.NO_PERMISSION, null);
            }
            queryResult.close();

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, INSERT_CHANNEL, "ss", channelName, guildName).executeUpdate();
            DBUtils.commit(db);

            DBUtils.close(db);
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            if (nQuery == 3) DBUtils.rollback(db);
            DBUtils.close(db);
            if (nQuery == 3 && e.getErrorCode() == 1062) { // That channel already exists.
                return new JsonIO.Resp(ReturnCodes.CHANNEL_ALREADY_EXISTS, MsgCodes.CHANNEL_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            if (nQuery == 3) DBUtils.rollback(db);
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
