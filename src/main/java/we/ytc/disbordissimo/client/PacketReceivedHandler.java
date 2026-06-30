package we.ytc.disbordissimo.client;

public interface PacketReceivedHandler {

    void onPacketReceived(byte[] audioFrame);
}
