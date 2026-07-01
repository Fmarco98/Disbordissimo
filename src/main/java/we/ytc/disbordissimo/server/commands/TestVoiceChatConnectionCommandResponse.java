package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

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
        try {
            long userID = Long.valueOf(params[0]);
            String channelName = params[1];
            String guildName = params[2];

            try {
                ResultSet queryResult = Main.getDB().execute(IS_MEMBER_QUERY, "sl", guildName, userID);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }
                queryResult.close();

                queryResult = Main.getDB().execute(CHANNEL_EXIST, "ss", guildName, channelName);
                queryResult.last();
                if(queryResult.getRow() != 1) {
                    return new JsonIO.Resp(ReturnCodes.CHANNEL_NOT_FOUND, MsgCodes.CHANNEL_NOT_FOUND, null);
                }
                long channelID = queryResult.getLong("id_channel");
                queryResult.close();

                boolean connected = Main.getActiveVoiceChannels().getVoiceChannel(userID) == channelID;

                return JsonIO.genSuccessResponse(List.of(String.valueOf(connected)));
            } catch (SQLException e) {
                Main.getLogger().logError("SQL error occurred: "+ e.getMessage());
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
