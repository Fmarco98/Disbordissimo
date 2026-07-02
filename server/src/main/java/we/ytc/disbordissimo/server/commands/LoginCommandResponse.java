package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

public class LoginCommandResponse implements CommandResponse{

    private static String USER_REQUEST_QUERY = "SELECT id_user " +
                                               "FROM users " +
                                               "WHERE username = ? AND passwd = ? ;";

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        try {
            String username = params[0];
            String hashPasswd = params[1];

            ResultSet queryResult = DBUtils.bindParams(db, USER_REQUEST_QUERY,"ss", username, hashPasswd).executeQuery();
            queryResult.last();
            if(queryResult.getRow() != 1) {
                DBUtils.close(db);
                queryResult.close();
                return new JsonIO.Resp(ReturnCodes.USER_NOT_FOUND, MsgCodes.USER_NOT_FOUND, null);
            }
            String userID = queryResult.getString("id_user");
            queryResult.close();

            return JsonIO.genSuccessResponse(List.of(userID));
        } catch (SQLException e) {
            DBUtils.close(db);
            Main.getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
