package we.ytc.disbordissimo.Server.commands;

import we.ytc.disbordissimo.Common.JsonIO;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.util.Arrays;
import java.util.List;

public class SignUpCommandResponse implements CommandResponse{
    @Override
    public String getCommandName() {
        return "sign-up";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Logger.logMsg(String.valueOf(Arrays.stream(params).toList()));

        return new JsonIO.Resp(JsonIO.SUCCESS_CODE, JsonIO.SUCCESS_MSG, null);
    }
}
