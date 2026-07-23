package we.ytc.disbordissimo.server.internal;

//TODO: documentation

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse;
import we.ytc.disbordissimo.server.internal.dataclasses.Room;
import we.ytc.disbordissimo.server.internal.dataclasses.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VoiceChannelsManager {
    private static final int TAU_TIME = 5*60; // 5 min

    private Map<Long, RoomInfo> activeChannels; // ID -> {Room, timestamp}
    private JanusClient janus;

    private int cleanerSleep;
    private boolean t_running;
    private Thread cleaner = new Thread(() -> {
        while (t_running) {
            synchronized (this) {
                List<Long> IDsCopy = new ArrayList<>(activeChannels.keySet());
                IDsCopy.stream().forEach(id -> {
                    if(activeChannels.get(id).timestamp > TimeUtils.currentTimestamp() - TAU_TIME) return;

                    if(getChannelMembers(id).isEmpty()) {
                        destroyChannel(id);
                        DisbordissimoServer.getServer().getLogger().logDebug("Destroyed room{id="+id+"}");
                    }
                });
            }

            try {
                if(!t_running) break;
                Thread.sleep(cleanerSleep);
            } catch (InterruptedException e) {}
        }
    }, "cleaner");

    public VoiceChannelsManager(int cleanerInterval) {
        activeChannels = new TreeMap<>();
        janus = new JanusClient(0, JoinChannelCommandResponse.JANUS_URL);

        this.cleanerSleep = cleanerInterval;
        t_running = true;
        cleaner.start();
    }

    public synchronized Room getChannel(long id) {
        RoomInfo roomInfo = activeChannels.get(Long.valueOf(id));
        if(roomInfo != null) {
            roomInfo.timestamp = TimeUtils.currentTimestamp();
            return roomInfo.room;
        }

        Room room = janus.createRoom(id);
        activeChannels.put(id, new RoomInfo(room, TimeUtils.currentTimestamp()));
        return room;
    }

    public synchronized boolean destroyChannel(long id) {
        RoomInfo roomInfo = activeChannels.remove(Long.valueOf(id));
        if(roomInfo == null) return false;

        return janus.destroyRoom(roomInfo.room);
    }

    public synchronized List<Long> getChannelMembers(long channelID) {
        RoomInfo roomInfo = activeChannels.get(Long.valueOf(channelID));
        if(roomInfo == null) return List.of();

        return janus.listParticipants(roomInfo.room);
    }

    public void stopCleaner() {
        t_running = false;
        cleaner.interrupt();
        try {
            cleaner.join();
        } catch (InterruptedException e) {}
    }

    @Override
    public String toString() {
        var ref = new Object() {
            String s = "VoiceChannelManager::Activechannels{";
        };

        activeChannels.values().forEach(roomInfo -> {
            ref.s += roomInfo+"; ";
        });

        return ref.s + "}";
    }
}
