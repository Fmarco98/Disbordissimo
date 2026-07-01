package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBManager;

import java.sql.SQLException;

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

            db.startTransaction();
            db.execute(GUILD_INSERT_QUERY,"sl", guildName, userID);
            db.execute(INSERT_MEMBER, "sl", guildName, userID);
            db.commit();

            db.close();
            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            db.rollback();
            db.close();
            if (e.getErrorCode() == 1062) { // the requested guild already exists.
                return new JsonIO.Resp(ReturnCodes.GUILD_ALREADY_EXISTS, MsgCodes.GUILD_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: "+ e.getMessage());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        } catch (Exception e) {
            db.close();
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
