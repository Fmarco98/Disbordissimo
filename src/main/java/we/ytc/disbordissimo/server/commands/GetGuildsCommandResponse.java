package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetGuildsCommandResponse implements CommandResponse{

    private final String GET_GUILDS = "SELECT guildname " +
                                      "FROM user_guild_byname " +
                                      "WHERE member = ? " +
                                      "GROUP BY guildname;";

    @Override
    public String getCommandName() {
        return "get-guilds";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        try {
            long userID = Long.valueOf(params[0]);

            try {
                ResultSet queryResult = Main.getDB().execute(GET_GUILDS, "l", userID);
                List<String> result = new ArrayList<>();

                while(queryResult.next()) {
                    result.add(queryResult.getString("guildname"));
                }

                return JsonIO.genSuccessResponse(result);
            } catch (SQLException e) {
                Main.getLogger().logError("SQL error occurred: " + e);
                return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
            }
        } catch (Exception e) {
            Main.getLogger().logError(e.toString());
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
