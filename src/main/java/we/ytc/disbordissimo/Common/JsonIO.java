package we.ytc.disbordissimo.Common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonIO {
    private Gson gson = new GsonBuilder().create();

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
        public Resp(int code, List<Object> result) {
            this.code = code;
            this.result = result;
        }

        @SerializedName("code")
        public int code;

        @SerializedName("result")
        public List<Object> result;
    }

    public Req deserializeReq(String json) {
        return gson.fromJson(json, Req.class);
    }

    public String serializeReq(Req req) {
        return gson.toJson(req);
    }

    public Resp deserializeResp(String json) {
        return gson.fromJson(json, Resp.class);
    }

    public String serializeResp(Resp resp) {
        return gson.toJson(resp);
    }
}
