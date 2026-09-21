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

import static we.ytc.disbordissimo.server.internal.commands.CreateGuildChannelCommandResponse.IS_OWNER;
import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

/**
 * <h1>DropGuildChannel CommandResponse</h1>
 * Logic to respond to the command "drop-guild-channel".
 */
public class DropGuildChannelCommandResponse implements CommandResponse {

    private static String DROP_CHANNEL = "DELETE FROM channels " +
                                         "WHERE name = ? AND fk_guild = ( " +
                                         "    SELECT id_guild " +
                                         "    FROM guilds " +
                                         "    WHERE name = ? " +
                                         ")";

    @Override
    public String getCommandName() {
        return "drop-guild-channel";
    }

    @Override
    public JsonObject onPerformed(JsonObject request) {
        if(!request.has("userID") || !request.has("guild") || !request.has("channel"))
            return Template.error();

        long userID = request.get("userID").getAsLong();
        String guildName = request.get("guild").getAsString();
        String channelName = request.get("channel").getAsString();

        Connection db = null;
        try {
            db = DisbordissimoServer.getServer().getDB();

            //Checks if the user is a guild member
            ResultSet queryResult = DBUtils.bindParams(db, IS_MEMBER_QUERY, "sl", guildName, userID).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("exist")) {
                queryResult.close();
                DBUtils.close(db);
                return Template.guildNotFound();
            }
            queryResult.close();

            //Checks if the user is the owner
            queryResult = DBUtils.bindParams(db, IS_OWNER, "ls", userID, guildName).executeQuery();
            queryResult.last();
            if (queryResult.getRow() != 1 || !queryResult.getBoolean("owner")) {
                queryResult.close();
                DBUtils.close(db);
                return Template.noPermission();
            }
            queryResult.close();

            DBUtils.startTransaction(db);
            int affectedRows = DBUtils.bindParams(db, DROP_CHANNEL, "ss", channelName, guildName).executeUpdate();
            if(affectedRows == 0) {
                DBUtils.rollback(db);
                DBUtils.close(db);
                return Template.channelNotFound();
            } else if (affectedRows > 1) {
                throw new Exception("Deleted too many rows");
            }

            DBUtils.commit(db);
            DBUtils.close(db);
            return Template.success();
        } catch (SQLException e) {
            DBUtils.rollback(db);
            DBUtils.close(db);
            DisbordissimoServer.getServer().getLogger().logError("SQL error occurred: " + e);
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
