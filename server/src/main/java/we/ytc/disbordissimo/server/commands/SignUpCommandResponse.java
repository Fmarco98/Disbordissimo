package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;
import we.ytc.disbordissimo.server.utils.db.DBUtils;

import java.awt.image.DataBufferInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

//TODO: documentation

/**
 * <h1>Sign up command response</h1>
 *
 *
 */
public class SignUpCommandResponse implements CommandResponse {
    private static String USER_INSERT_QUERY = "INSERT INTO users(username, passwd) VALUES (?, ?);";

    @Override
    public String getCommandName() {
        return "sign-up";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Connection db = Main.getDB();
        try {
            String username = params[0];
            String hashPasswd = params[1];

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, USER_INSERT_QUERY,"ss", username, hashPasswd).executeUpdate();
            DBUtils.commit(db);

            return JsonIO.genSuccessResponse();
        } catch (SQLException e){
            DBUtils.rollback(db);
            DBUtils.close(db);
            if (e.getErrorCode() == 1062) { // That username has already been used.
                return new JsonIO.Resp(ReturnCodes.USER_ALREADY_EXISTS, MsgCodes.USER_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: "+ e);
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);

        } catch (Exception e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            Main.getLogger().logError(e.toString());
            e.printStackTrace();
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }
    }
}
