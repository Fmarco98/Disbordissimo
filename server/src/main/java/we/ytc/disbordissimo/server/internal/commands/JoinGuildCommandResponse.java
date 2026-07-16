package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h1>JoinGuild CommandResponse</h1>
 * Logic to respond to the command "join-guild".
 */
public class JoinGuildCommandResponse implements CommandResponse {

    private static String JOIN_GUILD_QUERY = "INSERT INTO users_guilds(fk_guild, fk_user) VALUES " +
                                             "(( " +
                                             "    SELECT id_guild " +
                                             "    FROM guilds " +
                                             "    WHERE name = ? " +
                                             "), ?);";

    @Override
    public String getCommandName() {
        return "join-guild";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, JOIN_GUILD_QUERY, "sl", guildName, userID).executeUpdate();
            DBUtils.commit(db);

            DBUtils.close(db);
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            if (e.getErrorCode() == 1062) { // That user has already joined the requested guild.
                return new JsonIO.Resp(ReturnCodes.GUILD_ALREADY_JOINED, MsgCodes.GUILD_ALREADY_JOINED, null);
            }
            if (e.getErrorCode() == 1048) { // The requested guild doesn't exist
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }

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
