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
import we.ytc.disbordissimo.server.internal.dataclasses.Room;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1>JoinChannel CommandResponse</h1>
 * Logic to respond to the command "join".
 */
public class JoinChannelCommandResponse implements CommandResponse{
    //public static final String JANUS_URL = "ws://localhost:8188/";
    //public static final String STUN_URL = "stun:stun.l.google.com:19302";

    protected static String IS_MEMBER_QUERY = "SELECT COUNT(id_member) as exist " +
                                            "FROM user_guild_byname " +
                                            "WHERE guildname = ? AND id_member = ? " +
                                            "GROUP BY id_guild;";

    protected static String CHANNEL_EXIST = "SELECT id_guild, id_channel " +
                                          "FROM channel_guild_byname " +
                                          "WHERE guildname = ? AND channelname = ?;";

    @Override
    public String getCommandName() {
        return "join";
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

            // Checks if the user has already joined the channel
            if (DisbordissimoServer.getServer().getActiveVoiceChannels()
                    .getChannelMembers(channelID).contains(userID)) {
                DBUtils.close(db);
                return Template.channelAlreadyJoined();
            }

            DBUtils.close(db);
            Room channel = DisbordissimoServer.getServer().getActiveVoiceChannels().getChannel(channelID);

            JsonObject response = Template.success();
            response.addProperty("roomID", channel.id);
            response.addProperty("roomPin", channel.pin);
            response.addProperty("janus", DisbordissimoServer.getServer().getConfig().webrtc.janusUrl);
            response.addProperty("stun", DisbordissimoServer.getServer().getConfig().webrtc.stunUrl);
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
