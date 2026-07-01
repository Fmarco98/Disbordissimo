package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class CreateGuildChannelCommandResponse implements CommandResponse {

    private static String IS_OWNER = "SELECT fk_owner = ? as owner " +
                                     "FROM guilds " +
                                     "WHERE name = ?; ";

    private static String INSERT_CHANNEL = "INSERT INTO channels(name, fk_guild) VALUES " +
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
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];
            String channelName = params[2];

            try {
                ResultSet queryResult = Main.getDB().execute(IS_MEMBER_QUERY, "sl", guildName, userID);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }
                queryResult.close();
            } catch (SQLException e) {
                Main.getLogger().logError("SQL error occurred: "+e);
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }

            try {
                ResultSet queryResult = Main.getDB().execute(IS_OWNER, "ls", userID, guildName);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("owner")) {
                    return new JsonIO.Resp(ReturnCodes.NO_PERMISSION, MsgCodes.NO_PERMISSION, null);
                }
                queryResult.close();
            } catch (SQLException e) {
                Main.getLogger().logError("SQL error occurred: "+e);
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }

            try {
                Main.getDB().execute(INSERT_CHANNEL, "ss", channelName, guildName);

                return JsonIO.genSuccessResponse();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) { // That username has already been used.
                    return new JsonIO.Resp(ReturnCodes.CHANNEL_ALREADY_EXISTS, MsgCodes.CHANNEL_ALREADY_EXISTS, null);
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
