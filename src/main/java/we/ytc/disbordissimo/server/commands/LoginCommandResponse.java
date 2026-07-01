package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        try {
            DBManager db = new DBManager(
                    Main.getConfig().sqlConnectionConfig.host,
                    Main.getConfig().sqlConnectionConfig.user,
                    Main.getConfig().sqlConnectionConfig.password,
                    Main.getConfig().sqlConnectionConfig.dbName
            );

            String username = params[0];
            String hashPasswd = params[1];

            try {
                ResultSet result = db.execute(USER_REQUEST_QUERY,"ss", username, hashPasswd);

                result.last();
                int rowsNum = result.getRow();
                if(rowsNum != 1) {
                    return new JsonIO.Resp(ReturnCodes.USER_NOT_FOUND, MsgCodes.USER_NOT_FOUND, null);
                }

                return JsonIO.genSuccessResponse(List.of(result.getString("id_user")));
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
