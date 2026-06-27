package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.server.commands.CommandResponse;
import we.ytc.disbordissimo.server.commands.JoinQuitCommandResponse;
import we.ytc.disbordissimo.server.commands.SignUpCommandResponse;
import we.ytc.disbordissimo.server.utils.db.DBManager;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.TempConfig;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//TODO: documentation

/**
 * Server launcher class
 */
public class Main {

    private static Logger logger = null;
    private static DBManager db = null;
    private static VoiceChannelsManager voiceChannels = new VoiceChannelsManager();

    /**
     * Server main
     */
    public static void main(String[] args) throws Exception {
        //Logger.fileSetUp();
        //Server setup

        //TODO: lettura da config

        //Setup comandi
        List<CommandResponse> commandsHandlers = new ArrayList<>();
        commandsHandlers.add(new SignUpCommandResponse());
        commandsHandlers.add(new JoinQuitCommandResponse());

        Thread t = new Thread(()->{
            while(true) {
                synchronized (Main.getActiveVoiceChannels()) {
                    Main.getLogger().logMsg(Main.getActiveVoiceChannels().toString());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

        //Server UDP setup
        int udpPort = TempConfig.UDP_PORT;
        UDPServer udpServer = new UDPServer(udpPort);
        Main.getLogger().logDebug("UDP server opened on: %:" + udpPort);
        udpServer.start();

        //-------------------------------------------

        //Server TCP setup
        int port = TempConfig.TCP_PORT;
        ServerSocket server = new ServerSocket(port);
        Main.getLogger().logDebug("TCP server opened on: %:" + port);
        List<TCPResponse> activeResponses = new LinkedList<>();

        boolean running = true;
        while(running) {
            Socket client = server.accept();

            TCPResponse response = new TCPResponse(client, activeResponses, commandsHandlers);
            response.start();
        }
        server.close();

        synchronized (activeResponses) {
            activeResponses.stream().forEach(response -> {
                try {
                    response.join();
                } catch (InterruptedException e) {
                    Main.getLogger().logError("TCPResponses joining: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /** //TODO: documentation
     * Gets the DB access interface.
     *
     * @return {@link DBManager}
     */
    public static DBManager getDB() {
        if(db == null) {
            try {
                String db_user = TempConfig.DB_USER;
                String db_pwd = TempConfig.DB_PWD;
                String db_name = TempConfig.DB_NAME;

                db = new DBManager(db_user, db_pwd, db_name);
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
            logger = new Logger(); //TODO: log setup conf
        }
        return logger;
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
