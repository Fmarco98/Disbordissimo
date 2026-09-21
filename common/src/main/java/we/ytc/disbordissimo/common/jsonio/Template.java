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

package we.ytc.disbordissimo.common.jsonio;

import com.google.gson.JsonObject;

/**
 * <h1>Template class</h1>
 *
 * This class contains static functions only.
 * Every function returns a JSON object generated according its response type template.
 */
public class Template {

    /**
     * Gets a success JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.SUCCESS,
     *    "msgCode": MsgCodes.SUCCESS
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject success() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.SUCCESS);
        json.addProperty("msgCode", MsgCodes.SUCCESS);
        return json;
    }

    /**
     * Gets a error JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.ERROR,
     *    "msgCode": MsgCodes.ERROR
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject error() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.ERROR);
        json.addProperty("msgCode", MsgCodes.ERROR);
        return json;
    }

    /**
     * Gets a channelAlreadyExists JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.CHANNEL_ALREADY_EXISTS,
     *    "msgCode": MsgCodes.CHANNEL_ALREADY_EXISTS
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject channelAlreadyExists() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.CHANNEL_ALREADY_EXISTS);
        json.addProperty("msgCode", MsgCodes.CHANNEL_ALREADY_EXISTS);
        return json;
    }

    /**
     * Gets a guildAlreadyExists JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.GUILD_ALREADY_EXISTS,
     *    "msgCode": MsgCodes.GUILD_ALREADY_EXISTS
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject guildAlreadyExists() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.GUILD_ALREADY_EXISTS);
        json.addProperty("msgCode", MsgCodes.GUILD_ALREADY_EXISTS);
        return json;
    }

    /**
     * Gets a guildNotFound JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.GUILD_NOT_FOUND,
     *    "msgCode": MsgCodes.GUILD_NOT_FOUND
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject guildNotFound() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.GUILD_NOT_FOUND);
        json.addProperty("msgCode", MsgCodes.GUILD_NOT_FOUND);
        return json;
    }

    /**
     * Gets a channelNotFound JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.CHANNEL_NOT_FOUND,
     *    "msgCode": MsgCodes.CHANNEL_NOT_FOUND
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject channelNotFound() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.CHANNEL_NOT_FOUND);
        json.addProperty("msgCode", MsgCodes.CHANNEL_NOT_FOUND);
        return json;
    }

    /**
     * Gets a noPermission JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.NO_PERMISSION,
     *    "msgCode": MsgCodes.NO_PERMISSION
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject noPermission() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.NO_PERMISSION);
        json.addProperty("msgCode", MsgCodes.NO_PERMISSION);
        return json;
    }

    /**
     * Gets a channelAlreadyJoined JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.CHANNEL_ALREADY_JOINED,
     *    "msgCode": MsgCodes.CHANNEL_ALREADY_JOINED
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject channelAlreadyJoined() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.CHANNEL_ALREADY_JOINED);
        json.addProperty("msgCode", MsgCodes.CHANNEL_ALREADY_JOINED);
        return json;
    }

    /**
     * Gets a guildAlreadyJoined JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.GUILD_ALREADY_JOINED,
     *    "msgCode": MsgCodes.GUILD_ALREADY_JOINED
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject guildAlreadyJoined() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.GUILD_ALREADY_JOINED);
        json.addProperty("msgCode", MsgCodes.GUILD_ALREADY_JOINED);
        return json;
    }

    /**
     * Gets a userNotFound JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.USER_NOT_FOUND,
     *    "msgCode": MsgCodes.USER_NOT_FOUND
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject userNotFound() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.USER_NOT_FOUND);
        json.addProperty("msgCode", MsgCodes.USER_NOT_FOUND);
        return json;
    }

    /**
     * Gets a userAlreadyExists JSON object response.
     * <pre>
     *  {
     *    "code": ReturnCodes.USER_ALREADY_EXISTS,
     *    "msgCode": MsgCodes.USER_ALREADY_EXISTS
     *  }
     * </pre>
     *
     * @return {@link JsonObject}.
     */
    public static JsonObject userAlreadyExists() {
        JsonObject json = new JsonObject();
        json.addProperty("code", ReturnCodes.USER_ALREADY_EXISTS);
        json.addProperty("msgCode", MsgCodes.USER_ALREADY_EXISTS);
        return json;
    }
}
