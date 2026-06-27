package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.socketmanager.SocketManager;
import we.ytc.disbordissimo.server.ActiveUser;
import we.ytc.disbordissimo.server.Main;

import java.util.LinkedList;
import java.util.List;

public class JoinQuitCommandResponse implements CommandResponse{
    private static final JsonIO.Resp successResp = new JsonIO.Resp(JsonIO.SUCCESS_CODE, JsonIO.SUCCESS_MSG, null);

    @Override
    public String getCommandName() {
        return "join";
    }

    @Override
    public JsonIO.Resp onPerformed(SocketManager socket, String... params) {
        long userID = Long.valueOf(params[0]);
        long channelID = Long.valueOf(params[1]);
        ActiveUser user = new ActiveUser(userID);
        this.join(channelID, user);
        socket.send(JsonIO.serializeResp(successResp));

        try {
            while(true) {
                JsonIO.Req request = JsonIO.deserializeReq(socket.recv());
                if(request.cmdName.equals("quit")) {
                    this.quit(channelID, user);
                    break;
                }
            }
        } catch (RuntimeException e) {
            this.quit(channelID, user);
            throw e;
        }

        return successResp;
    }

    private void quit(long channelID, ActiveUser user) {
        var activeUsers = Main.getVoiceChatActiveUsers();

        synchronized (activeUsers) {
            List<ActiveUser> users = activeUsers.get(channelID);
            users.remove(user);

            if(users.size() == 0) {
                activeUsers.remove(channelID);
            }
        }
    }

    private void join(long channelID, ActiveUser user) {
        var activeUsers = Main.getVoiceChatActiveUsers();

        synchronized (activeUsers) {
            List<ActiveUser> users = activeUsers.get(channelID);
            if(users == null) {
                users = new LinkedList();
                users.add(user);
                activeUsers.put(channelID, (LinkedList<ActiveUser>) users);

            } else {
                users.add(user);
            }
        }
    }
}
