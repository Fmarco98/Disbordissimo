package we.ytc.disbordissimo.Common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonIO {

    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MSG = "Success";

    private static final Gson gson = new GsonBuilder().create();
    private JsonIO() {}

    public static class Req {
        public Req(String cmdName, List<Object> params) {
            this.cmdName = cmdName;
            this.params = params;
        }

        @SerializedName("cmdName")
        public String cmdName;

        @SerializedName("params")
        public List<Object> params;
    }

    public static class Resp {
        public Resp(int code, String msgCode, List<Object> result) {
            this.code = code;
            this.result = result;
            this.msgCode = msgCode;
        }

        @SerializedName("code")
        public int code;

        @SerializedName("msgCode")
        public String msgCode;

        @SerializedName("result")
        public List<Object> result;
    }

    public static Req deserializeReq(String json) {
        return gson.fromJson(json, Req.class);
    }

    public static String serializeReq(Req req) {
        return gson.toJson(req);
    }

    public static Resp deserializeResp(String json) {
        return gson.fromJson(json, Resp.class);
    }

    public static String serializeResp(Resp resp) {
        return gson.toJson(resp);
    }
}
