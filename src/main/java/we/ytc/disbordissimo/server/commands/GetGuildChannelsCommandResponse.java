package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class GetGuildChannelsCommandResponse implements CommandResponse {

    private final String GET_GUILD_CHANNELS = "SELECT channelname " +
                                              "FROM channel_guild_byname " +
                                              "WHERE guildname = ? " +
                                              "GROUP BY channelname;";

    @Override
    public String getCommandName() {
        return "get-guild-channel";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            try {
                ResultSet queryResult = Main.getDB().execute(IS_MEMBER_QUERY, "sl", guildName, userID);
                queryResult.last();
                if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                    return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
                }
                queryResult.close();

                queryResult = Main.getDB().execute(GET_GUILD_CHANNELS, "s", guildName);
                List<String> result = new ArrayList<>();
                while(queryResult.next()) {
                    result.add(queryResult.getString("channelname"));
                }
                queryResult.close();

                return JsonIO.genSuccessResponse(result);
            } catch (SQLException e) {
                Main.getLogger().logError("SQL error occurred: " + e.getMessage());
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
