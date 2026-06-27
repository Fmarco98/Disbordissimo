package we.ytc.disbordissimo.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//TODO: documentation

/**
 * <h1>Voice Channels Manager class</h1>
 *
 * Manages the status of all active channels, it tracks which user is connected.
 */
public class VoiceChannelsManager {

    private static HashMap<Long, LinkedList<ActiveUser>> channel_users = new HashMap<>(); //HashMap: <channel, List<ActiveUser>>
    private static HashMap<Long, Long> users_channel = new HashMap<>(); //HashMap: <user, channel>

    public VoiceChannelsManager() {}

    /** //TODO
     * Join into a channel
     *
     * @param channelID
     * @param user
     */
    public synchronized void join(long channelID, ActiveUser user) {
        List<ActiveUser> users = channel_users.get(channelID);
        if(users == null) {
            users = new LinkedList();
            users.add(user);
            channel_users.put(channelID, (LinkedList<ActiveUser>) users);
        } else {
            users.add(user);
        }

        users_channel.put(user.getUserID(), channelID);
    }

    /** //TODO
     * quit from a channel
     *
     * @param channelID
     * @param user
     */
    public synchronized void quit(long channelID, ActiveUser user) {
        List<ActiveUser> users = channel_users.get(channelID);
        users.remove(user);

        if(users.size() == 0) {
            channel_users.remove(channelID);
        }

        users_channel.remove(user.getUserID());
    }

    /**
     * Gets the channel where the user is connected
     *
     * @param userID
     *        User id
     * @return {@code channel id}
     */
    public synchronized long getVoiceChannel(long userID) {
        Long result = users_channel.get(userID);
        return result != null ? result : -1;
    }

    /**
     * Gets all users connected to the channel {@code channelID}.
     *
     * @param channelID
     *        voice chat id
     * @return List of users
     */
    public synchronized List<ActiveUser> getConnectedUsers(long channelID) {
        return channel_users.get(channelID);
    }

    @Override
    public String toString() {
        return channel_users.toString();
    }
}
