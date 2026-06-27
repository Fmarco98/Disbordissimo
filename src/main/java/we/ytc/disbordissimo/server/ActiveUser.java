package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.audio.AudioUtils;

public class ActiveUser {


    private long userID;
    private byte[] micFrame;

    public ActiveUser(long userID) {
        this.userID = userID;
        this.micFrame = new byte[AudioUtils.MIC_FRAME_LENGTH];
    }

    public byte[] getMicFrame() {
        return micFrame;
    }
    public long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "ActiveUser{userId="+userID+";}";
    }
}
