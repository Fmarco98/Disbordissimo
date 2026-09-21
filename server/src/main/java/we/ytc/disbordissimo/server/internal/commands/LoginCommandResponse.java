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
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public JsonObject onPerformed(JsonObject request) {
        if(!request.has("username") || !request.has("hpwd"))
            return Template.error();

        String username = request.get("username").getAsString();
        String hashPasswd = request.get("hpwd").getAsString();

        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            ResultSet queryResult = DBUtils.bindParams(db, USER_REQUEST_QUERY,"ss", username, hashPasswd).executeQuery();
            queryResult.last();
            if(queryResult.getRow() != 1) {
                DBUtils.close(db);
                queryResult.close();
                return Template.userNotFound();
            }
            String userID = queryResult.getString("id_user");
            queryResult.close();

            JsonObject response = Template.success();
            response.addProperty("userID", userID);
            return response;
        } catch (SQLException e) {
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
            e.printStackTrace();
            return Template.error();

        } catch (Exception e) {
            if(db != null) DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError(e.toString());
            e.printStackTrace();
            return Template.error();
        }
    }
}
