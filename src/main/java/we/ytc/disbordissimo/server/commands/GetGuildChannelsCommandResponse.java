package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
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
        Connection db = Main.getDB();
        try {
            long userID = Long.valueOf(params[0]);
            String guildName = params[1];

            //Checks if the user is a guild member
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return new JsonIO.Resp(ReturnCodes.GUILD_NOT_FOUND, MsgCodes.GUILD_NOT_FOUND, null);
            }
            queryResult.close();

            queryResult = DBUtils.bindParams(db, GET_GUILD_CHANNELS, "s", guildName).executeQuery();
            List<String> result = new ArrayList<>();
            while(queryResult.next()) {
                result.add(queryResult.getString("channelname"));
            }
            queryResult.close();

            DBUtils.close(db);
            return JsonIO.genSuccessResponse(result);
        } catch (SQLException e) {
            DBUtils.close(db);
            Main.getLogger().logError("SQL error occurred: " + e);
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
