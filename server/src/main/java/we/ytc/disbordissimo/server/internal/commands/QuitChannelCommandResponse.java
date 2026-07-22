package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

/**
 * <h1>QuitChannel CommandResponse</h1>
 * Logic to respond to the command "quit".
 */
public class QuitChannelCommandResponse implements CommandResponse {
    @Override
    public String getCommandName() {
        return "quit";
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
//            if (queryResult.getRow() != 1) {
//                queryResult.close();
//                DBUtils.close(db);
//                return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
//            }
//            long channelID = queryResult.getLong("id_channel");
//            queryResult.close();
//
//            DBUtils.close(db);
//            DisbordissimoServer.getServer().getActiveVoiceChannels().quit(channelID, new ActiveUser(userID));
//            return JsonIO.genSuccessResponse();
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

        return JsonIO.genSuccessResponse();
    }
}
