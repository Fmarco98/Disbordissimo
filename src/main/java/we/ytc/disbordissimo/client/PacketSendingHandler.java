package we.ytc.disbordissimo.client;

/**
 * <h1>PacketReceivedHandler interface</h1>
 *
 * This interface contains the callback handler of a {@code PacketSendingEvent}.
 * It can be set to a {@link DisbordissimoClient} using {@code setPacketSendingHandler(..)} method.
 */
public interface PacketSendingHandler {

    /**
     * The handler function.
     *
     * @return The user {@code micFrame}.
     */
    byte[] onPacketSending();
}
