package we.ytc.disbordissimo.Server.commands;

import we.ytc.disbordissimo.Common.JsonIO;

public interface CommandResponse {
    String getCommandName();
    JsonIO.Resp onPerformed(String ...params);
}
