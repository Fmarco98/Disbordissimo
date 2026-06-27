package we.ytc.disbordissimo.server;

public class ActiveUser {
    private static final int FRAME_LENGTH = 1024;

    private long userID;
    private byte[] micFrame;

    public ActiveUser(long userID) {
        this.userID = userID;
        this.micFrame = new byte[FRAME_LENGTH];
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
