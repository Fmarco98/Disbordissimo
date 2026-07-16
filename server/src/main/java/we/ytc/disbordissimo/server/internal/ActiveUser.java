package we.ytc.disbordissimo.server.internal;

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.AudioUtils;

//TODO: documentation

/**
 * <h1>Active user data class</h1>
 * The data class represents a user connected to any voice-channel.
 */
public class ActiveUser {
    private long userID;

    private byte[] micFrame;
    private long lastRecvTime;

    /**
     * Constructor.
     * @param userID
     *        User ID
     */
    public ActiveUser(long userID) {
        this.userID = userID;
        this.micFrame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        this.lastRecvTime = TimeUtils.currentTimestamp();
    }

    /**
     * Sets a new {@code microphone frame} for the represented user.
     */
    public void setMicFrame(byte[] micFrame) {
        this.lastRecvTime = TimeUtils.currentTimestamp();
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
     * Gets the {@code LastRecvTimestamp}. That timestamp is the time when the last {@code microphone frame}
     * of the represented user arrived.
     *
     * @return {@code LastRecvTimestamp}
     */
    public long getLastRecvTimestamp() {
        return lastRecvTime;
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActiveUser) {
            ActiveUser other = (ActiveUser) obj;

            return other.userID == this.userID;
        }
        return false;
    }
}
