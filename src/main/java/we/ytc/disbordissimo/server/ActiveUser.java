package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.audio.AudioUtils;

//TODO: documentation

/**
 * <h1>Active user data class</h1>
 *
 */
public class ActiveUser {

    private long userID;
    private byte[] micFrame;

    /**
     * Constructor.
     * @param userID
     *        User ID
     */
    public ActiveUser(long userID) {
        this.userID = userID;
        this.micFrame = new byte[AudioUtils.MIC_FRAME_LENGTH];
    }

    /**
     * Sets a new {@code microphone frame} for the represented user.
     */
    public void setMicFrame(byte[] micFrame) {
        this.micFrame = micFrame;
    }

    /**
     * Gets the {@code microphone frame} of the represented user.
     *
     * @return {@code microphone frame}
     */
    public byte[] getMicFrame() {
        return micFrame;
    }

    /**
     * Gets the {@code user id} of the represented user.
     *
     * @return {@code userID}
     */
    public long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "ActiveUser{userId="+userID+";}";
    }
}
