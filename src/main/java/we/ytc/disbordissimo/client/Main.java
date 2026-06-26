package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.Command;
import we.ytc.disbordissimo.client.commands.SignUpCommand;
import we.ytc.disbordissimo.TempConfig;
import we.ytc.disbordissimo.common.logger.Logger;

public class Main {

    private static Logger logger = null;

    public static class Config {
        public static String TCP_HOST = TempConfig.TCP_HOST;
        public static int TCP_PORT = TempConfig.TCP_PORT;
    }

    public static void main(String[] args) throws Exception {
        Command cmd = new SignUpCommand();
        cmd.onPerformed("ciao1", "ciao2");
    }

    public static Logger getLogger() {
        if(logger == null) {
            logger = new Logger(); //TODO: log setup conf
        }
        return logger;
    }
}
