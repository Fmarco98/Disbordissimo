package we.ytc.disbordissimo.client;

// TODO: make the client use them

public interface EventHandler {

    void onChannelJoin(String channel, String user);

    void onChannelLeave(String channel, String user);
}
