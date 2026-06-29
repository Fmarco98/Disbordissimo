package we.ytc.disbordissimo.common.jsonio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/** //TODO: documentation
 * <h1>Json I/O class</h1>
 *
 *
 */
public class JsonIO {
    private JsonIO() {}

    private static final Gson gson = new GsonBuilder().create();

    public static final String CMD_NOT_FOUND_RESPONSE = serializeResp(
            new JsonIO.Resp(ReturnCodes.COMMAND_NOT_FOUND, MsgCodes.COMMAND_NOT_FOUND, null)
    );

    public static Resp genSuccessResponse() {
        return genSuccessResponse(null);
    }

    public static Resp genSuccessResponse(List<String> params) {
        return new Resp(ReturnCodes.SUCCESS, MsgCodes.SUCCESS, params);
    }

    /** //TODO: documentation
     * <h1>Json Request data class</h1>
     *
     *
     */
    public static class Req {
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
