package we.ytc.disbordissimo.server.internal;

import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.server.DisbordissimoServer;

import java.util.*;

//TODO: documentation

/**
 * <h1>Voice Channels Manager class</h1>
 *
 * Manages the status of all active channels and tracks which user is connected.
 * For each connected user it stores the last audio received information.
 */
public class VoiceChannelsManager {

    private HashMap<Long, List<ActiveUser>> channel_users;
    private HashMap<Long, Long> users_channel;
    private long timeOut;
    private long sleep;
    private boolean t_running;

    @Deprecated(forRemoval = true)
    private Thread cleaning;

    public VoiceChannelsManager(long timeOut, long sleep) {
        channel_users = new HashMap<>(); //HashMap: <channel, List<ActiveUser>>
        users_channel = new HashMap<>(); //HashMap: <user, channel>
        this.timeOut = timeOut;
        this.sleep = sleep;

        t_running = true;
        cleaning = new Thread(() -> {
            //TODO: migrare questo a KCPServer.handleClose()
            while(t_running) {
                DisbordissimoServer.getServer().getLogger().logDebug("Try to clean");

                Map<Long, List<ActiveUser>> activeChannels = new HashMap<>();
                synchronized (this) {
                    for( Long chID : channel_users.keySet()) {
                        activeChannels.put(chID, channel_users.get(chID));
                    }
                }

                for( Long chID : activeChannels.keySet() ) {
                    List<ActiveUser> activeUsers = activeChannels.get(chID);

                    synchronized (activeUsers) {
                        int i=0;
                        while (i < activeUsers.size()) {
                            ActiveUser user = activeUsers.get(i);
                            // Checks if last recv is timed out (: recv timed out => user is no longer connected)
                            if (TimeUtils.currentTimestamp() - user.getLastRecvTimestamp() > timeOut) {
                                this.quit(chID, user);
                                DisbordissimoServer.getServer().getLogger().logDebug("Cleaned user: " + user.getUserID());
                            } else {
                                i++;
                            }
                        }
                    }
                }

                DisbordissimoServer.getServer().getLogger().logDebug("cleaning finished");

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {}
            }
        }, "ActiveUser-Cleaner");
        cleaning.start();
    }

    /**
     * Makes the {@code user} joins to the voice-channel which ID equals to {@code channelID}.
     *
     * @param channelID
     *        Voice-channel ID
     *
     * @param user
     *        The user.
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

    /**
     * Makes the {@code user} quits form the voice-channel which ID equals to {@code channelID}.
     *
     * @param channelID
     *        Voice-channel ID
     *
     * @param user
     *        The user.
     */
    public synchronized void quit(long channelID, ActiveUser user) {
        List<ActiveUser> users = channel_users.get(channelID);

        if(users == null) return;

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

    /**
     * Stops the Cleaner Thread
     * @throws InterruptedException
     */
    @Deprecated(forRemoval = true)
    public void stopCleaning() throws InterruptedException {
        t_running = false;
        cleaning.join();
    }

    @Override
    public String toString() {
        return channel_users.toString();
    }
}
