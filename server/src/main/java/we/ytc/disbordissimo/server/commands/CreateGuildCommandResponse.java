package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class CreateGuildCommandResponse implements CommandResponse {

    private static String GUILD_INSERT_QUERY = "INSERT INTO guilds(name, fk_owner) VALUES (?, ?);";
    private static String INSERT_MEMBER = "INSERT INTO users_guilds(fk_guild, fk_user) VALUES " +
                                          "(( " +
                                          "    SELECT id_guild " +
                                          "    FROM guilds " +
                                          "    WHERE name = ? " +
                                          "), ?);";

    @Override
    public String getCommandName() {
        return "create-guild";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, GUILD_INSERT_QUERY,"sl", guildName, userID).executeUpdate();
            DBUtils.bindParams(db, INSERT_MEMBER, "sl", guildName, userID).executeUpdate();
            DBUtils.commit(db);

            DBUtils.close(db);
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            if (e.getErrorCode() == 1062) { // That guild already exists
                return new JsonIO.Resp(ReturnCodes.GUILD_ALREADY_EXISTS, MsgCodes.GUILD_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: " + e);
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
