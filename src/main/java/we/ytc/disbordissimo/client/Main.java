package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.JoinCommand;
import we.ytc.disbordissimo.client.commands.QuitCommand;
import we.ytc.disbordissimo.common.socketmanager.SocketManager.SocketContainer;
import we.ytc.disbordissimo.TempConfig;
import we.ytc.disbordissimo.common.logger.Logger;

public class Main {

    private static Logger logger = null;

    public static class Config {
        public static String TCP_HOST = TempConfig.TCP_HOST;
        public static int TCP_PORT = TempConfig.TCP_PORT;
    }

    public static void main(String[] args) throws Exception {
        JoinCommand join = new JoinCommand();
        SocketContainer s = join.onPerformed("1234", "9876");

        Thread.sleep(5000);

        QuitCommand quit = new QuitCommand();
        quit.onPerformed(s);
    }

    public static Logger getLogger() {
        if(logger == null) {
            logger = new Logger(); //TODO: log setup conf
        }
        return logger;
    }
}
