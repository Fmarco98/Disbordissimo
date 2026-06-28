package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.server.Main;

import java.util.Arrays;

//TODO: documentation

/**
 * <h1>Sign up command response</h1>
 *
 *
 */
public class SignUpCommandResponse implements CommandResponse{
    @Override
    public String getCommandName() {
        return "sign-up";
    }

    @Override
    public JsonIO.Resp onPerformed(String... params) {
        Main.getLogger().logMsg(String.valueOf(Arrays.stream(params).toList()));

        return new JsonIO.Resp(JsonIO.SUCCESS_CODE, JsonIO.SUCCESS_MSG, null);
    }
}
