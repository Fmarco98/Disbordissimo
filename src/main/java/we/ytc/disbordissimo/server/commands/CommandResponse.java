package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.JsonIO;

public interface CommandResponse {
    String getCommandName();
    JsonIO.Resp onPerformed(String ...params);
}
