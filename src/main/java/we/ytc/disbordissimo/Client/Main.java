package we.ytc.disbordissimo.Client;

import we.ytc.disbordissimo.TempConfig;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    @Test
    public static void main(String[] args) throws Exception {
        String host = TempConfig.TCP_HOST;
        int port = TempConfig.TCP_PORT;
        Socket socket = new Socket(host, port);
        Logger.logDebug("connected to " + host + ":" + port);

        Scanner in;
        PrintStream out;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out.println("ciao");
        Logger.logMsg(in.nextLine());
    }
}
