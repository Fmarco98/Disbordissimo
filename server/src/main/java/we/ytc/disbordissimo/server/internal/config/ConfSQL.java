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

package we.ytc.disbordissimo.server.internal.config;

import com.google.gson.annotations.SerializedName;

public class ConfSQL {

    @SerializedName("host")
    public String host;

    @SerializedName("port")
    public int port;

    @SerializedName("user")
    public String user;

    @SerializedName("pswd")
    public String password;

    @SerializedName("name")
    public String dbName;

    public ConfSQL() {
        host = "";
        port = 3306;
        user = "";
        password = "";
        dbName = "";
    }

    public ConfSQL(String host, int port, String user, String password, String dbName) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
    }


}