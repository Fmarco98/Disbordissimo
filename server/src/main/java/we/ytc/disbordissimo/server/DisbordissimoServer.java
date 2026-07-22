package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.server.exceptions.AlreadyLaunchedException;
import we.ytc.disbordissimo.server.internal.VoiceChannelsManager;
import we.ytc.disbordissimo.server.internal.commands.*;
import we.ytc.disbordissimo.server.internal.TCPServer;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.server.internal.utils.db.DBUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TODO: documentation

/**
 * <h1>Disbordissimo Server class</h1>
 *
 */
public class DisbordissimoServer extends Thread {
    private static DisbordissimoServer INSTANCE = null;

    private Config config;
    private Logger logger;
    private boolean running = true;

    private VoiceChannelsManager voiceChannels;
    private List<CommandResponse> commandsHandlers;
    private TCPServer tcpServer;

    /**
     * Constructor.
     *
     * @param config
     *        The {@link Config}
     *
     * @param logger
     *        The {@link Logger}
     *
     * @throws IOException
     */
    public DisbordissimoServer(Config config, Logger logger) throws IOException {
        super("Disbordissimo-Server");

        if (INSTANCE != null) throw new AlreadyLaunchedException();
        INSTANCE = this;

        this.logger = logger;
        this.config = config;

//        voiceChannels = new VoiceChannelsManager(
//                config.activeClassCleanerConfig.userTimeout,
//                config.activeClassCleanerConfig.cleaningSleep
//        );

        commandsHandlers = new ArrayList<>();
        commandsHandlers.add(new PingCommandResponse());
        commandsHandlers.add(new SignUpCommandResponse());
        commandsHandlers.add(new LoginCommandResponse());
        commandsHandlers.add(new JoinChannelCommandResponse());
        commandsHandlers.add(new QuitChannelCommandResponse());
        commandsHandlers.add(new TestVoiceChatConnectionCommandResponse());
        commandsHandlers.add(new GetGuildsCommandResponse());
        commandsHandlers.add(new GetGuildChannelsCommandResponse());
        commandsHandlers.add(new CreateGuildCommandResponse());
        commandsHandlers.add(new CreateGuildChannelCommandResponse());
        commandsHandlers.add(new JoinGuildCommandResponse());
        commandsHandlers.add(new GetGuildOwnerCommandResponse());
        commandsHandlers.add(new DropGuildChannelCommandResponse());
        commandsHandlers.add(new DropGuildCommandResponse());
        commandsHandlers.add(new LeaveGuildCommandResponse());
        commandsHandlers.add(new GetGuildMembersCommandResponse());
        commandsHandlers.add(new GetGuildChannelConnectedMembersCommandResponse());

        tcpServer = new TCPServer(config.tcpServerConfig.port, commandsHandlers);
    }

    @Override
    public void run() {
        tcpServer.start();
        getLogger().logDebug("TCP server opened on: %:" + config.tcpServerConfig.port);

        try {
            tcpServer.join();
        } catch (InterruptedException e) {}
    }

    /**
     * Stops the Disbordissimo Server.
     */
    public void stopServer() {
        tcpServer.stopServer();
        try {
            this.join();
        } catch (InterruptedException e) {}

        INSTANCE = null;
    }

    /**
     * Gets a DB connection.
     * @return
     */
    public Connection getDB() {
        try {
            Connection conn = DBUtils.connect(
                    config.sqlConnectionConfig.host,
                    config.sqlConnectionConfig.user,
                    config.sqlConnectionConfig.password,
                    config.sqlConnectionConfig.dbName
            );
            getLogger().logDebug("DB connected");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the {@link Logger}.
     *
     * @return {@link Logger} object
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the global {@link Config}.
     *
     * @return {@link Config} object
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Gets the active voice channels manager.
     *
     * @return {@link VoiceChannelsManager}
     */
    public VoiceChannelsManager getActiveVoiceChannels() {
        return voiceChannels;
    }

    /**
     * Gets the active instance of {@link DisbordissimoServer}
     *
     * @return The {@link DisbordissimoServer} instance if the server is created;
     *         {@code null} otherwise;
     */
    public static DisbordissimoServer getServer() {
        return INSTANCE;
    }
}
