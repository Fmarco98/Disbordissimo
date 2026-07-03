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
public abstract sealed class DisbordissimoClient permits Client {

    /**
     * <h1>Config class</h1>
     *
     * It represents the {@link DisbordissimoClient} config. <br>
     * <br>
     * Contains:<br>
     *  - serverAddress<br>
     *  - serverPort<br>
     *  - UDP sockets Timeout<br>
     *  - Kbps TargetRate<br>
     *  - Ping inteval<br>
     * <br>
     * Methods:<br>
     *  - getters<br>
     */
    public static class Config {
        private InetAddress serverAddress;
        private int serverPort;
        private int UDPTimeOut;
        private int kbps;
        private int pingInterval;

        /**
         * Contract Constructor. The {@code kbps} and {@code UDPTimeOut} values are set to their default.
         *
         * @param serverAddress
         *        Disbordissimo Server address
         * @param serverPort
         *        Disbordissimo Server port
         */
        public Config(InetAddress serverAddress, int serverPort) {
            this(serverAddress, serverPort, 64, 500, 180000 /*3min*/);
        }

        /**
         * Constructor.
         *
         * @param serverAddress
         *        Disbordissimo Server address
         * @param serverPort
         *        Disbordissimo Server port
         * @param kbps
         *        kbps target
         * @param UDPTimeOut
         *        Receiving socket time out
         * @param pingInterval
         *        Ping interval
         */
        public Config(InetAddress serverAddress, int serverPort, int kbps , int UDPTimeOut, int pingInterval) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.UDPTimeOut = UDPTimeOut;
            this.kbps = kbps;
            this.pingInterval = pingInterval;
        }

        /**
         * Gets the kbps target rate.
         * @return {@code kbps}
         */
        public int getKbps() {
            return kbps;
        }

        /**
         * Gets the UDP receiving socket time out.
         * @return {@code UDPTimeOut}
         */
        public int getUDPTimeOut() {
            return UDPTimeOut;
        }

        /**
         * Gets the DisbordissimoServer address.
         * @return {@code serverAddress}
         */
        public InetAddress getServerAddress() {
            return serverAddress;
        }

        /**
         * Gets the DisbordissimoServer port.
         * @return {@code serverPort}
         */
        public int getServerPort() {
            return serverPort;
        }

        /**
         * Gets the Ping interval.
         * @return {@code pingInterval}
         */
        public int getPingInterval() {
            return pingInterval;
        }
    }

    /**
     * Creates a {@link DisbordissimoClient}. The default associated {@link Logger} is {@link NullLogger}.
     *
     * @param config
     *        The {@link Config}.
     *
     * @return client instance
     */
    public static DisbordissimoClient create(Config config) {
        return create(config, new NullLogger());
    }

    /**
     * Creates a {@link DisbordissimoClient}.
     *
     * @param config
     *        The {@link Config}.
     * @param logger
     *        A {@link Logger}
     *
     * @return client instance
     */
    public static DisbordissimoClient create(Config config, Logger logger) {
        return new Client(config, logger);
    }

    /**
     * Sets the {@link PacketSendingHandler}. It defines all operation that will be performed
     * when the client tries to send a UDP packet. <br>
     * <br>
     * Important: All UDP packet contains audio data.
     *
     * @param sending
     *        The function
     */
    public abstract void setPacketSendingHandler(PacketSendingHandler sending);

    /**
     * Sets the {@link PacketReceivedHandler}. It defines all operation that will be performed
     * when the client received a UDP packet. <br>
     * <br>
     * Important: All UDP packet contains audio data.
     *
     * @param received
     *        The function
     */
    public abstract void setPacketReceivedHandler(PacketReceivedHandler received);

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
    public abstract void signUp(String username, String password) throws CommandFailedException;

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
    public abstract void login(String username, String password) throws CommandFailedException;

    /**
     * Logs out the user.
     */
    public abstract void logout();

    /**
     * Checks if the user is logged in.
     *
     * @return {@code true} if a user logged in;
     *         {@code false} otherwise;
     */
    public abstract boolean isLoggedIn();

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
    public abstract void joinChannel(String channel, String guild) throws CommandFailedException;

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
    public abstract void quitChannel(String channel, String guild) throws CommandFailedException;

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
    public abstract void reconnectToChannel(String channel, String guild) throws CommandFailedException;

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
    public abstract boolean isConnectedTo(String channel, String guild) throws CommandFailedException;

    /**
     * Gets all {@code guilds} where the logged user is member.
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract String[] getGuilds() throws CommandFailedException;

    /**
     * Gets all voice channel of the given {@code guild}.
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract String[] getGuildChannels(String guild) throws CommandFailedException;

    /**
     * Gets the owner username of the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract String getGuildOwner(String guild) throws CommandFailedException;

    /**
     * Creates a new {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract void createGuild(String guild) throws CommandFailedException;

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
    public abstract void createGuildChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Joins the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract void joinGuild(String guild) throws CommandFailedException;

    /**
     * Leaves the given {@code guild}
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract void leaveGuild(String guild) throws CommandFailedException;

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
    public abstract void dropGuildChannel(String channel, String guild) throws CommandFailedException;

    /**
     * Drops {@code guild}.
     *
     * @param guild
     *        The guild name
     *
     * @throws CommandFailedException
     *         If the command doesn't end with {@code ReturnCodes.SUCCESS}
     */
    public abstract void dropGuild(String guild) throws CommandFailedException;

    /**
     * Destroys the client.
     */
    public abstract void destroy();

    /**
     * Gets the server medium ping.
     * @return {@code ping}
     */
    public abstract int getPing();

    /**
     * Checks if the server is reachable.
     * @return {@code true} if server is reachable;
     *         {@code false} otherwise;
     */
    public abstract boolean isServerReachable();
}
