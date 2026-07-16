package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h1>SignUp CommandResponse</h1>
 * Logic to respond to the command "sign-up".
 */
public class SignUpCommandResponse implements CommandResponse {
    private static String USER_INSERT_QUERY = "INSERT INTO users(username, passwd) VALUES (?, ?);";

    @Override
    public String getCommandName() {
        return "sign-up";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            String username = params[0];
            String hashPasswd = params[1];

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, USER_INSERT_QUERY,"ss", username, hashPasswd).executeUpdate();
            DBUtils.commit(db);

            return JsonIO.genSuccessResponse();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            if (e.getErrorCode() == 1062) { // That username has already been used.
                return new JsonIO.Resp(ReturnCodes.USER_ALREADY_EXISTS, MsgCodes.USER_ALREADY_EXISTS, null);
            }

            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: "+ e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            if(db != null) {
                DBUtils.rollback(db);
                DBUtils.close(db);
            }
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
