package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//TODO: documentatio

/**
 * <h1>JoinChannel CommandResponse</h1>
 * Logic to respond to the command "join".
 */
public class JoinChannelCommandResponse implements CommandResponse{
    protected static final String JANUS_URL = "ws://localhost:8188/";
    protected static final String STUN_URL = "stun:stun.l.google.com:19302";

    protected static String IS_MEMBER_QUERY = "SELECT COUNT(id_member) as exist " +
                                            "FROM user_guild_byname " +
                                            "WHERE guildname = ? AND id_member = ? " +
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
//        Connection db = null;
//        try {
//            db = DisbordissimoServer.getServer().getDB();
//
//            long userID = Long.valueOf(params[0]);
//            String guildName = params[1];
//            String channelName = params[2];
//
//            //Checks if the user is a guild member
//            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
//            queryResult.last();
//            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
//                queryResult.close();
//                DBUtils.close(db);
//                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
//            }
//            queryResult.close();
//
//            // Checks if the requested channel exists
//            queryResult = DBUtils.bindParams(db, CHANNEL_EXIST, "ss", guildName, channelName).executeQuery();
//            queryResult.last();
//            if(queryResult.getRow() != 1) {
//                queryResult.close();
//                DBUtils.close(db);
//                return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
//            }
//            long channelID = queryResult.getLong("id_channel");
//            queryResult.close();
//
//            // Checks if the user has already joined the channel
//            if (DisbordissimoServer.getServer().getActiveVoiceChannels().getVoiceChannel(userID) != -1) {
//                DBUtils.close(db);
//                return new JsonIO.Resp(ReturnCodes.CHANNEL_ALREADY_JOINED, MsgCodes.CHANNEL_ALREADY_JOINED, null);
//            }
//
//            DBUtils.close(db);
//            DisbordissimoServer.getServer().getActiveVoiceChannels().join(channelID, new ActiveUser(userID));
//            return JsonIO.genSuccessResponse(List.of(
//                    String.valueOf(1234),
//                    "",
//                    JANUS_URL,
//                    STUN_URL
//            ));
//        } catch (SQLException e) {
//            DBUtils.close(db);
//            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
//            e.printStackTrace();
//            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
//
//        } catch (Exception e) {
//            if(db != null) DBUtils.close(db);
//            DisbordissimoServer.getServer().getLogger().logError(e.toString());
//            e.printStackTrace();
//            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
//        }

        return JsonIO.genSuccessResponse(List.of(
                    String.valueOf(1234),
                    "",
                    JANUS_URL,
                    STUN_URL
            ));
    }
}
