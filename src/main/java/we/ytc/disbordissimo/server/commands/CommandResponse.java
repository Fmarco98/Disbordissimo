package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.socketmanager.SocketManager;

public interface CommandResponse {
    String getCommandName();
    JsonIO.Resp onPerformed(SocketManager socket, String ...params);
}
