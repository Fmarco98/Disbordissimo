package we.ytc.disbordissimo.client;

/**
 * <h1>PacketReceivedHandler interface</h1>
 *
 * This interface contains the callback handler of a {@code PacketReceivedEvent}.
 * It can be set to a {@link DisbordissimoClient} using {@code setPacketReceivedHandler(..)} method.
 */
public interface PacketReceivedHandler {

    /**
     * The handler function.
     *
     * @param audioFrame
     *        The {@code audioFrame} received from the server. It contains the mix of all channel connected user {@code micFrams}.
     */
    void onPacketReceived(byte[] audioFrame);
}
