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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// TODO: MILO

/** //TODO: documentation
 * <h1>Json I/O class</h1>
 *
 *
 */
public class JsonIO {
    private JsonIO() {}
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Command not found response.
     */
    public static final String CMD_NOT_FOUND_RESPONSE = serializeResp(
            new JsonIO.Resp(ReturnCodes.COMMAND_NOT_FOUND, MsgCodes.COMMAND_NOT_FOUND, null)
    );

    /**
     * Generates a SUCCESS Response with no payload.
     *
     * @return A SUCCESS {@link JsonIO.Resp}.
     */
    public static Resp genSuccessResponse() {
        return genSuccessResponse(null);
    }

    /**
     * Generates a SUCCESS Response with payload.
     *
     * @param params
     *        Payload
     *
     * @return A SUCCESS {@link JsonIO.Resp}.
     */
    public static Resp genSuccessResponse(List<String> params) {
        return new Resp(ReturnCodes.SUCCESS, MsgCodes.SUCCESS, params);
    }

    /** //TODO: documentation
     * <h1>Json Request data class</h1>
     *
     *
     */
    public static class Req {
        Req() {}

        public Req(String cmdName, List<String> params) {
            this.cmdName = cmdName;
            this.params = params;
        }

        @SerializedName("cmdName")
        public String cmdName;

        @SerializedName("params")
        public List<String> params;

        @Override
        public String toString() {
            return "JsonIO.Resp{cmdName="+cmdName+"; params="+params+"}";
        }
    }

    /** //TODO: documentation
     * <h1>Json Response data class</h1>
     *
     *
     */
    public static class Resp {
        Resp() {}

        public Resp(int code, String msgCode, List<String> result) {
            this.code = code;
            this.result = result;
            this.msgCode = msgCode;
        }

        @SerializedName("code")
        public int code;

        @SerializedName("msgCode")
        public String msgCode;

        @SerializedName("result")
        public List<String> result;

        @Override
        public String toString() {
            return "JsonIO.Resp{code="+code+"; msgCode="+msgCode+"; result="+result+"}";
        }
    }

    /**
     * Deserializes a {@code json} string into a {@link JsonIO.Req}
     *
     * @param json
     *        Json String
     * @return {@link JsonIO.Req} object
     */
    public static Req deserializeReq(String json) {
        return gson.fromJson(json, Req.class);
    }

    /**
     * Serializes the {@link JsonIO.Req}
     *
     * @param req
     *        {@link JsonIO.Req} object
     *
     * @return Json String
     */
    public static String serializeReq(Req req) {
        if(req.params == null) {
            req.params = new ArrayList<>();
        }
        return gson.toJson(req);
    }

    /**
     * Deserializes a {@code json} string into a {@link JsonIO.Resp}
     *
     * @param json
     *        Json String
     * @return {@link JsonIO.Resp} object
     */
    public static Resp deserializeResp(String json) {
        return gson.fromJson(json, Resp.class);
    }

    /**
     * Serializes the {@link JsonIO.Resp}
     *
     * @param resp
     *        {@link JsonIO.Resp} object
     *
     * @return Json String
     */
    public static String serializeResp(Resp resp) {
        if(resp.result == null) {
            resp.result = new ArrayList<>();
        }
        return gson.toJson(resp);
    }
}
