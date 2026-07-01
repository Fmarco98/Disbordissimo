package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

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
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            Main.getDB().startTransaction();
            Main.getDB().execute(GUILD_INSERT_QUERY,"sl", guildName, userID);
            Main.getDB().execute(INSERT_MEMBER, "sl", guildName, userID);
            Main.getDB().commit();

            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            Main.getDB().rollback();
            if (e.getErrorCode() == 1062) { // That username has already been used.
                return new JsonIO.Resp(ReturnCodes.GUILD_ALREADY_EXISTS, MsgCodes.GUILD_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: "+e);
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        } catch (Exception e) {
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
