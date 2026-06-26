package we.ytc.disbordissimo.Server;

import we.ytc.disbordissimo.Server.commands.CommandResponse;
import we.ytc.disbordissimo.Server.utils.db.DBManager;
import we.ytc.disbordissimo.Server.utils.logger.Logger;
import we.ytc.disbordissimo.TempConfig;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Main {

    public static DBManager db;
    public static HashMap<Long, Long> voiceChatUsers = new HashMap<>(); //HashMap: <user, channel>

    public static void main(String[] args) throws Exception {
        //Logger.fileSetUp();
        //Server setup

        //TODO: lettura da config
        String db_user = TempConfig.DB_USER;
        String db_pwd = TempConfig.DB_PWD;
        String db_name = TempConfig.DB_NAME;
        Main.db = new DBManager(db_name, db_pwd, db_name);
        Logger.logDebug("Connected to SQL DB: "+db_name+"@"+db_user);

        //Setup comandi
        List<CommandResponse> commandsHandlers = new ArrayList<>();


        //Server UDP setup

        //-------------------------------------------

        //Server TCP setup
        int port = TempConfig.TCP_PORT;
        ServerSocket server = new ServerSocket(port);
        Logger.logDebug("TCP server opened on: %:" + port);
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
                    Logger.logError("TCPResponses joining: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
