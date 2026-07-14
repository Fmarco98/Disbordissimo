package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.NullLogger;

import java.net.InetAddress;

/**
 * <h1>Disbordissimo Client class</h1>
 *
 * This is the Disbordissimo Client API class. <br>
 * <br>
 * A {@link DisbordissimoClient} is created by invoking the {@code DisbordissimoClient.create(..)} methods.<br>
 * <br>
 * Inner classes:<br>
 *  - Config<br>
 * <br>
 * Functions:<br>
 *  - create(..)<br>
 * <br>
 * Client's methods: <br>
 *  - setPacketSendingHandler(..)<br>
 *  - setPacketReceivedHandler(..)<br>
 *  - signUp(..)<br>
 *  - login(..)<br>
 *  - isLoggedIn(..)<br>
 *  - logout(..)<br>
 *  - joinChannel(..)<br>
 *  - isConnectedTo(..)<br>
 *  - quitChannel(..)<br>
 *  - reconnectToChannel(..)<br>
 *  - getGuilds(..)<br>
 *  - getGuildChannels(..)<br>
 *  - getGuildOwner(..)<br>
 *  - createGuild(..)<br>
 *  - createGuildChannel(..)<br>
 *  - joinGuild(..)<br>
 *  - leaveGuild(..)<br>
 *  - dropGuildChannel(..)<br>
 *  - dropGuild(..)<br>
 */
public interface DisbordissimoClient {

    /**
     * Sets the {@link PacketSendingHandler}. It defines all operation that will be performed
     * when the client tries to send a UDP packet. <br>
     * <br>
     * Important: All UDP packet contains audio data.
     *
     * @param sending
     *        The function
     */
    void setPacketSendingHandler(PacketSendingHandler sending);

    /**
     * Sets the {@link PacketReceivedHandler}. It defines all operation that will be performed
     * when the client received a UDP packet. <br>
     * <br>
     * Important: All UDP packet contains audio data.
     *
     * @param received
     *        The function
     */
    void setPacketReceivedHandler(PacketReceivedHandler received);

    /**
     * Signs up the user.
     *
     * @param username
     *        The username
     * @param password
     *        The password
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void signUp(String username, String password) throws CommandFailedException;

    /**
     * Logs in the user.
     *
     * @param username
     *        The username
     * @param password
     *        The password
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void login(String username, String password) throws CommandFailedException;

    /**
     * Logs out the user.
     */
    void logout();

    /**
     * Checks if the user is logged in.
     *
     * @return {@code true} if a user logged in;
     *         {@code false} otherwise;
     */
    boolean isLoggedIn();

    /**
     * Joins the given voice channel ({@code guild.channel}).
     *
     * @param guild
     *        The guild name
     * @param channel
     *        The voice channel name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void joinChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Quits the given voice channel ({@code guild.channel}).
     *
     * @param guild
     *        The guild name
     * @param channel
     *        The voice channel name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void quitChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Reconnects to the given voice channel ({@code guild.channel}).
     *
     * @param guild
     *        The guild name
     * @param channel
     *        The voice channel name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void reconnectToChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Checks if the user is connected to the given voice channel ({@code guild.channel}).
     *
     * @param guild
     *        The guild name
     * @param channel
     *        The voice channel name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    boolean isConnectedTo(String channel, String guild) throws CommandFailedException;

    /**
     * Gets all {@code guilds} where the logged user is member.
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    String[] getGuilds() throws CommandFailedException;

    /**
     * Gets all voice channel of the given {@code guild}.
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    String[] getGuildChannels(String guild) throws CommandFailedException;

    /**
     * Gets the owner username of the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    String getGuildOwner(String guild) throws CommandFailedException;

    /**
     * Creates a new {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void createGuild(String guild) throws CommandFailedException;

    /**
     * Creates a new voice channel in the given {@code guild}.
     *
     * @param channel
     *        The channel name
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void createGuildChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Joins the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void joinGuild(String guild) throws CommandFailedException;

    /**
     * Leaves the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void leaveGuild(String guild) throws CommandFailedException;

    /**
     * Drops {@code guild.channel}.
     *
     * @param channel
     *        The channel name
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void dropGuildChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Drops {@code guild}.
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    void dropGuild(String guild) throws CommandFailedException;

    /**
     * Destroys the client.
     */
    void destroy();

    /**
     * Gets the server medium ping.
     * @return {@code ping}
     */
    int getPing();

    /**
     * Checks if the server is reachable.
     * @return {@code true} if server is reachable;
     *         {@code false} otherwise;
     */
    boolean isServerReachable();

    /**
     * Gets all members of the given {@code guild}.
     *
     * @return String array that contains the username of all members.
     */
    String[] getGuildMemers(String guild) throws CommandFailedException;

    /**
     * Gets all member of the {@code guild} connected to {@code channel}.
     *
     * @param channel
     *        Voice channel name
     * @param guild
     *        Guild name
     *
     * @return String array that contains the username of all members connected to {@code channel}
     */
    String[] getChannelConnectedMembers(String channel, String guild) throws CommandFailedException;
}
