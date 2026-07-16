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

/**
 * <h1>Login CommandResponse</h1>
 * Logic to respond to the command "login".
 */
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
        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

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
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            if(db != null) DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
