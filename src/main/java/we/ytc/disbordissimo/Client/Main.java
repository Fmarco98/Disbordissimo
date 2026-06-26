package we.ytc.disbordissimo.Client;

import we.ytc.disbordissimo.Client.commands.Command;
import we.ytc.disbordissimo.Client.commands.SignUpCommand;
import we.ytc.disbordissimo.TempConfig;

public class Main {

    public static class Config {
        public static String TCP_HOST = TempConfig.TCP_HOST;
        public static int TCP_PORT = TempConfig.TCP_PORT;
    }

    public static void main(String[] args) throws Exception {
        Command cmd = new SignUpCommand();
        cmd.onPerformed("ciao1", "ciao2");
    }
}
