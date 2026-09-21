/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.server.internal.commands;

import com.google.gson.JsonObject;
import we.ytc.disbordissimo.common.jsonio.Template;
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
    public JsonObject onPerformed(JsonObject request) {
        if(!request.has("username") || !request.has("hpwd"))
            return Template.error();

        String username = request.get("username").getAsString();
        String hashPasswd = request.get("hpwd").getAsString();

        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            DBUtils.startTransaction(db);
            DBUtils.bindParams(db, USER_INSERT_QUERY,"ss", username, hashPasswd).executeUpdate();
            DBUtils.commit(db);

            return Template.success();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            if (e.getErrorCode() == 1062) { // That username has already been used.
                return Template.userAlreadyExists();
            }

            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: "+ e);
            e.printStackTrace();
            return Template.error();

        } catch (Exception e) {
            if(db != null) {
                DBUtils.rollback(db);
                DBUtils.close(db);
            }
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return Template.error();
        }
    }
}
