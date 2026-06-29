package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpError;
import we.ytc.disbordissimo.common.logger.YtcLogger;
import we.ytc.disbordissimo.server.commands.*;
import we.ytc.disbordissimo.server.networking.TCPServer;
import we.ytc.disbordissimo.server.networking.UDPServer;
import we.ytc.disbordissimo.server.utils.db.DBManager;
import we.ytc.disbordissimo.common.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TODO: documentation

/**
 * Server launcher class
 */
public class Main {

    private static Config config;
    private static Logger logger = null;
    private static DBManager db = null;
    private static VoiceChannelsManager voiceChannels;

    /**
     * Server main
     */
    public static void main(String[] args) throws Exception {
        //Server setup

        if (Config.configFileExists()) {
            config = Config.loadConfig();
        } else {
            Main.getLogger().logWarning("Couldn't find config file. Creating one...");
            config = Config.defaultConfig();
        }

        //Logger setup
        Main.getLogger().logMsg("Setting up logger based on config...");
        changeLogger();
        Main.getLogger().logMsg("Logger loaded!");

        voiceChannels = new VoiceChannelsManager(config.activeClassCleanerConfig.userTimeout);

        // Commands setup
        List<CommandResponse> commandsHandlers = new ArrayList<>();
        commandsHandlers.add(new SignUpCommandResponse());
        commandsHandlers.add(new JoinCommandResponse());
        commandsHandlers.add(new QuitCommandResponse());
        commandsHandlers.add(new TestVoiceChatConnectionCommandResponse());

//        Thread t = new Thread(()->{
//            while(true) {
//                synchronized (Main.getActiveVoiceChannels()) {
//                    Main.getLogger().logMsg(Main.getActiveVoiceChannels().toString());
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        t.start();

        //Server UDP setup
        int udpPort = config.udpServerConfig.port;
        UDPServer udpServer = new UDPServer(udpPort);
        udpServer.start();
        Main.getLogger().logDebug("UDP server opened on: %:" + udpPort);

        //-------------------------------------------

        //Server TCP setup
        int tcpPort = config.tcpServerConfig.port;
        TCPServer tcpServer = new TCPServer(tcpPort, commandsHandlers);
        Main.getLogger().logDebug("UDP server opened on: %:" + tcpPort);

        // Calling 'run' to reuse the main thread.
        tcpServer.run();

        tcpServer.stopServer();
        udpServer.stopSever();

    }

    /** //TODO: documentation
     * Gets the DB access interface.
     *
     * @return {@link DBManager}
     */
    public static DBManager getDB() {
        if(db == null) {
            try {
                String db_host = config.sqlConnectionConfig.host;
                String db_user = config.sqlConnectionConfig.user;
                String db_pwd = config.sqlConnectionConfig.password;
                String db_name = config.sqlConnectionConfig.dbName;

                db = new DBManager(db_host, db_user, db_pwd, db_name);
                Main.getLogger().logDebug("Connected to SQL DB: "+db_name+"@"+db_user);
            } catch (SQLException e) {
                Main.getLogger().logError("An Error occurred while connecting to DB : " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return db;
    }

    /**
     * Gets the global {@link Logger}.
     *
     * @return {@link Logger} object
     */
    public static Logger getLogger() {
        if(logger == null) {
            logger = new YtcLogger();
        }
        return logger;
    }

    /** //TODO: documentation
     *
     */
    public static void changeLogger() {
        if(logger != null) logger.close();
        try {
            if (config.loggerConfig.isFileEnabled) {
                if (config.loggerConfig.isDefaultLogFile) {
                    logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, true);
                } else {
                    logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, config.loggerConfig.filePath);
                }
            } else {
                logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, false);
            }
        } catch (FileSetUpError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the global {@link Config}.
     *
     * @return {@link Config} object
     */
    public static Config getConfig() {
        return config;
    }

    /** //TODO: documentation
     * Gets the active voice channels.
     *
     * @return {@link VoiceChannelsManager}
     */
    public static VoiceChannelsManager getActiveVoiceChannels() {
        return voiceChannels;
    }
}
