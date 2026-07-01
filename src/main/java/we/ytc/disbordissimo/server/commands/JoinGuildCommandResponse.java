package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            try {
                Main.getDB().startTransaction();
                Main.getDB().execute(JOIN_GUILD_QUERY, "sl", guildName, userID);
                Main.getDB().commit();

                return JsonIO.genSuccessResponse();
            } catch (SQLException e) {
                Main.getDB().rollback();
                if (e.getErrorCode() == 1062) { // That user has already joined the requested guild.
                    return new JsonIO.Resp(ReturnCodes.GUILD_ALREADY_JOINED, MsgCodes.GUILD_ALREADY_JOINED, null);
                }
                if (e.getErrorCode() == 1048) { // The requested guild doesn't exist
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }

                Main.getLogger().logError("SQL error occurred: "+ e.getMessage());
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
