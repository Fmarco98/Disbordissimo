package we.ytc.disbordissimo.server.internal.dataclasses;

public class RoomInfo {
    public Room room;
    public long timestamp;

    public RoomInfo(Room room, long timestamp) {
        this.room = room;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RoomInfo{room="+room+"; time="+timestamp+"}";
    }
}
