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
import we.ytc.disbordissimo.common.jsonio.JsonUtils;
import we.ytc.disbordissimo.common.jsonio.Template;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.CHANNEL_EXIST;
import static we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse.IS_MEMBER_QUERY;

/**
 * <h1>GetGuildChannelConnectedMembers CommandResponse</h1>
 * Logic to respond to the command "get-guild-channel-connected-members".
 */
public class GetGuildChannelConnectedMembersCommandResponse implements CommandResponse {

    @Override
    public String getCommandName() {
        return "get-guild-channel-connected-members";
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

            // Checks if the requested channel exists
            queryResult = DBUtils.bindParams(db, CHANNEL_EXIST, "ss", guildName, channelName).executeQuery();
            queryResult.last();
            if(queryResult.getRow() != 1) {
                queryResult.close();
                DBUtils.close(db);
                return Template.channelNotFound();
            }
            long channelID = queryResult.getLong("id_channel");
            queryResult.close();

            JsonObject response = Template.success();
            List<Long> ids = DisbordissimoServer.getServer().getActiveVoiceChannels().getChannelMembers(channelID);
            if(ids.size() == 0) {
                DBUtils.close(db);
                return response;
            }

            final String[] GET_MEMBER_NAMES = prepareQuery(ids.size());
            queryResult = DBUtils.bindParams(db, GET_MEMBER_NAMES[0], GET_MEMBER_NAMES[1], ids.toArray()).executeQuery();
            List<String> result = new ArrayList<>();
            while (queryResult.next()) {
                result.add(queryResult.getString("username"));
            }
            queryResult.close();

            DBUtils.close(db);
            response.add("connected-members", JsonUtils.toJsonArray(result));
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

    private String[] prepareQuery(int size) {
        String query = "SELECT username " +
                       "FROM users " +
                       "WHERE id_user IN ( ";

        String params = "";

        for (int i = 0; i < size; i++) {
            query += (i != size-1 ? "?, " : "?");
            params += "l";
        }
        query += " );";

        return new String[]{query, params};
    }
}
