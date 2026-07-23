package we.ytc.disbordissimo.server.internal.dataclasses;

public class Room {
    public String pin;
    public String secret;
    public int id;

    public Room(long ID) {
        this(ID, "");
    }
    public Room(long ID, String pin) {
        this(ID, pin, "");
    }
    public Room(long ID, String pin, String secret) {
        this.id = (int) ID; // possible value collision (future fix)
        this.pin = pin;
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "room{id="+id+"}";
    }
}
