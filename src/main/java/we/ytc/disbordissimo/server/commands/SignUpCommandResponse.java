package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.server.Main;

import java.sql.SQLException;

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
        String username = params[0];
        String hashPasswd = params[1];

        try {
            Main.getDB().execute(USER_INSERT_QUERY,"ss", username, hashPasswd);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // That username has already been used.
                return new JsonIO.Resp(ReturnCodes.USER_ALREADY_EXISTS, MsgCodes.USER_ALREADY_EXISTS, null);
            }

            Main.getLogger().logError("SQL error occurred: "+e);
            return new JsonIO.Resp(ReturnCodes.ERROR, MsgCodes.ERROR, null);
        }

        return JsonIO.genSuccessResponse();
    }
}
