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

public class ConfWebrtc {
    @SerializedName("stunUrl")
    public String stunUrl;

    @SerializedName("janusUrl")
    public String janusUrl;

    public ConfWebrtc() {
        stunUrl = "stun:stun.l.google.com:19302";
        janusUrl = "";
    }

    public ConfWebrtc(String stunUrl, String janusUrl) {
        this.janusUrl = janusUrl;
        this.stunUrl = stunUrl;
    }
}
