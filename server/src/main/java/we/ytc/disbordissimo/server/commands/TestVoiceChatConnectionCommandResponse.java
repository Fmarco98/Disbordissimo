package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class TestVoiceChatConnectionCommandResponse implements CommandResponse{
    @Override
    public String getCommandName() {
        return "test-voice-chat-connection";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        try {
            long userID = Long.valueOf(params[0]);
            String channelName = params[1];
            String guildName = params[2];

            //Checks if the user is a guild member
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }
            queryResult.close();

            // Checks if the requested channel exists
            queryResult = DBUtils.bindParams(db, CHANNEL_EXIST, "ss", guildName, channelName).executeQuery();
            queryResult.last();
            if(queryResult.getRow() != 1) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
            }
            long channelID = queryResult.getLong("id_channel");
            queryResult.close();

            DBUtils.close(db);
            return JsonIO.genSuccessResponse(List.of(String.valueOf(
                    Main.getActiveVoiceChannels().getVoiceChannel(userID) == channelID
            )));
        } catch (SQLException e){
            DBUtils.close(db);
            Main.getLogger().logError("SQL error occurred: "+ e);
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
