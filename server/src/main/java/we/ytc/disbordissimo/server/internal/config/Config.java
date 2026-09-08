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

public class Config {
    @SerializedName("version")
    public String version;

    @SerializedName("tcpServer")
    public ConfTCP tcpServer;

    @SerializedName("webrtc")
    public ConfWebrtc webrtc;

    @SerializedName("sql")
    public ConfSQL sql;

    @SerializedName("logger")
    public ConfLogger logger;

    @SerializedName("misc")
    public ConfMisc misc;

    public Config() {
        this.version = "1.1-alpha";
        this.tcpServer = new ConfTCP();
        this.webrtc = new ConfWebrtc();
        this.sql = new ConfSQL();
        this.logger = new ConfLogger();
        this.misc = new ConfMisc();
    }

    public Config(String version, ConfTCP tcpServer, ConfWebrtc webrtc, ConfSQL sql, ConfLogger logger, ConfMisc misc) {
        this.version = version;
        this.tcpServer = tcpServer;
        this.webrtc = webrtc;
        this.sql = sql;
        this.logger = logger;
        this.misc = misc;
    }

}
