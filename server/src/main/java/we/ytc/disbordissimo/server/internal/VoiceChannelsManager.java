/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.server.internal;

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.commands.JoinChannelCommandResponse;
import we.ytc.disbordissimo.server.internal.dataclasses.Room;
import we.ytc.disbordissimo.server.internal.dataclasses.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <h1>VoiceChannel Manager class</h1>
 * The VoiceChannelManager manages the "status" of all janus rooms. It creates and destroys them.<br>
 * The cleaner Thread starts automatically at the object construction.<br>
 * <br>
 * Object methods:<br>
 *  - "constructor"(..)<br>
 *  - getChannel(..)<br>
 *  - destroyChannel(..)<br>
 *  - getChannelMembers(..)<br>
 *  - stopCleaner(..)<br>
 */
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

    /**
     * Constructor.
     *
     * @param cleanerInterval
     *        cleaner thread execution interval
     */
    public VoiceChannelsManager(int cleanerInterval) {
        activeChannels = new TreeMap<>();
        janus = new JanusClient(0, JoinChannelCommandResponse.JANUS_URL);

        this.cleanerSleep = cleanerInterval;
        t_running = true;
        cleaner.start();
    }

    /**
     * Requests a room.
     *
     * @param id
     *        Room ID
     *
     * @return {@link Room}
     */
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

    /**
     * Destroys a room.
     *
     * @param id
     *        Room ID
     *
     * @return {@code true} if operation is performed successfully;
     *         {@code false} otherwise
     */
    public synchronized boolean destroyChannel(long id) {
        RoomInfo roomInfo = activeChannels.remove(Long.valueOf(id));
        if(roomInfo == null) return false;

        return janus.destroyRoom(roomInfo.room);
    }

    /**
     * Gets the member connected to the channel which has a ID == {@code channelID}.
     *
     * @param channelID
     *        Channel ID (Room ID)
     *
     * @return List of Users' ID
     */
    public synchronized List<Long> getChannelMembers(long channelID) {
        RoomInfo roomInfo = activeChannels.get(Long.valueOf(channelID));
        if(roomInfo == null) return List.of();

        return janus.listParticipants(roomInfo.room);
    }

    /**
     * Stops the cleaner thread.
     */
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
