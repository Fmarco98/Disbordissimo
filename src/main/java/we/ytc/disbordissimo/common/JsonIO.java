package we.ytc.disbordissimo.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import javax.swing.*;
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
    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MSG = "Ok";

    public static final String CMD_NOT_FOUND_RESPONSE = serializeResp(
            new JsonIO.Resp(404, "Command Not Found", new ArrayList<>())
    );

    public static Resp genSuccessResponse() {
        return genSuccessResponse(null);
    }

    public static Resp genSuccessResponse(List<String> params) {
        return new Resp(SUCCESS_CODE, SUCCESS_MSG, params);
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
