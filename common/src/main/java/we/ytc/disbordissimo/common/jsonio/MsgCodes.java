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

/**
 * <h1>MsgCodes static class</h1>
 */
public class MsgCodes {
    private MsgCodes() {}

    /**
     * {@code SERVER_UNREACHABLE} message.
     */
    public static final String SERVER_UNREACHABLE = "The Disbordissimo server is unreachable";

    //generics
    /**
     * {@code SUCCESS} message.
     */
    public static final String SUCCESS = "Ok";

    /**
     * {@code NO_PERMISSION} message.
     */
    public static final String NO_PERMISSION = "Forbidden";

    /**
     * {@code COMMAND_NOT_FOUND} message.
     */
    public static final String COMMAND_NOT_FOUND = "Command Not Found";

    /**
     * Generic {@code ERROR} message.
     */
    public static final String ERROR = "An error occurred";

    //DB
    // User
    /**
     * {@code USER_NOT_FOUND} message.
     */
    public static final String USER_NOT_FOUND = "The requested user doesn't exists";

    /**
     * {@code USER_ALREADY_EXISTS} message.
     */
    public static final String USER_ALREADY_EXISTS = "An user with that username already exists";

    // Guild
    /**
     * {@code GUILD_NOT_FOUND} message.
     */
    public static final String GUILD_NOT_FOUND = "The requested guild doesn't exists";

    /**
     * {@code GUILD_ALREADY_EXISTS} message.
     */
    public static final String GUILD_ALREADY_EXISTS = "The guild already exists";

    /**
     * {@code GUILD_ALREADY_JOINED} message.
     */
    public static final String GUILD_ALREADY_JOINED = "You've already joined the requested guild";

    // Channel
    /**
     * {@code CHANNEL_NOT_FOUND} message.
     */
    public static final String CHANNEL_NOT_FOUND = "The requested channel doesn't exists";

    /**
     * {@code CHANNEL_ALREADY_EXISTS} message.
     */
    public static final String CHANNEL_ALREADY_EXISTS = "The channel already exists";

    /**
     * {@code CHANNEL_ALREADY_JOINED} message.
     */
    public static final String CHANNEL_ALREADY_JOINED = "You've already joined the requested channel";
}
