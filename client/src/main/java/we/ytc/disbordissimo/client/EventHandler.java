package we.ytc.disbordissimo.client;

// TODO: make the client use them

public interface EventHandler {

    void onChannelJoin(String user);

    void onChannelLeave(String user);
}
