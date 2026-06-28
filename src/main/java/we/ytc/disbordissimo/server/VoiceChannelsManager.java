package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.TimeUtils;

import java.io.Closeable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//TODO: documentation

/**
 * <h1>Voice Channels Manager class</h1>
 *
 * Manages the status of all active channels, it tracks which user is connected.
 */
public class VoiceChannelsManager {

    private HashMap<Long, LinkedList<ActiveUser>> channel_users;
    private HashMap<Long, Long> users_channel;
    private long timeOut;
    private boolean t_running;
    private Thread cleaning;

    public VoiceChannelsManager(long timeOut) {
        channel_users = new HashMap<>(); //HashMap: <channel, List<ActiveUser>>
        users_channel = new HashMap<>(); //HashMap: <user, channel>
        this.timeOut = timeOut;

        t_running = true;
        cleaning = new Thread(() -> {
            while(t_running) {
                Main.getLogger().logDebug("Try to clean");
                synchronized (this) {
                    Set<Long> channelIDs = channel_users.keySet();
                    for(long chID : channelIDs) {
                        List<ActiveUser> activeUsers = channel_users.get(chID);

                        int i=0;
                        while (i < activeUsers.size()) {
                            ActiveUser user = activeUsers.get(i);
                            // Checks if last recv is timed out (: recv timed out => user is no longer connected)
                            if (TimeUtils.currentTimestamp() - user.getLastRecvTimestamp() > timeOut) {
                                this.quit(chID, user);
                                Main.getLogger().logDebug("Cleaned user: " + user.getUserID());
                            } else {
                                i++;
                            }
                        }
                    }
                }
                Main.getLogger().logDebug("cleaning finished");

                try {
                    Thread.sleep(Main.getConfig().activeClassCleanerConfig.cleaningSleep);
                } catch (InterruptedException e) {}
            }
        }, "ActiveUser-Cleaner");
        cleaning.start();
    }

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
        synchronized (users) {
            users.remove(user);
            if(users.size() == 0) {
                channel_users.remove(channelID);
            }
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

    public void stopCleaning() throws InterruptedException {
        t_running = false;
        cleaning.join();
    }

    /** //TODO: documentation
     *
     * @return
     */
    public long getTimeOut() {
        return timeOut;
    }

    @Override
    public String toString() {
        return channel_users.toString();
    }
}
