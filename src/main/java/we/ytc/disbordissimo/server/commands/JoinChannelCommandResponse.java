package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;

//TODO: documentatio

/**
 * <h1>Join / Quit command response</h1>
 */
public class JoinChannelCommandResponse implements CommandResponse{

    protected static String IS_MEMBER_QUERY = "SELECT COUNT(member) as exist " +
                                            "FROM user_guild_byname " +
                                            "WHERE guildname = ? AND member = ? " +
                                            "GROUP BY id_guild;";

    protected static String CHANNEL_EXIST = "SELECT id_guild, id_channel " +
                                          "FROM channel_guild_byname " +
                                          "WHERE guildname = ? AND channelname = ?;";

    @Override
    public String getCommandName() {
        return "join";
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
            String channelName = params[2];

            try {
                ResultSet queryResult = db.execute(IS_MEMBER_QUERY, "sl", guildName, userID);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                    db.close();
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }
                queryResult.close();

                queryResult = db.execute(CHANNEL_EXIST, "ss", guildName, channelName);
                queryResult.last();
                if (queryResult.getRow() != 1) {
                    db.close();
                    return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
                }
                long channelID = queryResult.getLong("id_channel");
                queryResult.close();

                if (Main.getActiveVoiceChannels().getVoiceChannel(userID) != -1) {
                    db.close();
                    return new JsonIO.Resp(ReturnCodes.CHANNEL_ALREADY_JOINED, MsgCodes.CHANNEL_ALREADY_JOINED, null);
                }

                db.close();
                Main.getActiveVoiceChannels().join(channelID, new ActiveUser(userID));
                return JsonIO.genSuccessResponse();
            } catch (SQLException e) {
                db.close();
                Main.getLogger().logError("SQL error occurred: " + e.getMessage());
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            db.close();
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
