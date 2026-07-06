package we.ytc.disbordissimo.client.exceptions;

/**
 * <h1>IllegalMicFrameSize Exception</h1>
 *
 * This exception is thrown when the {@link we.ytc.disbordissimo.client.DisbordissimoClient} tries to send
 * a microphone frame which size is different by {@code AudioUtils.MIC_FRAME_LENGTH}
 */
public class IllegalMicFrameSize extends RuntimeException {

    /**
     * Constructor.
     *
     * @param size
     *        MicFrame size
     */
    public IllegalMicFrameSize(int size) {
        super("The mic frame has an invalid size (size="+size+")");
    }
}
