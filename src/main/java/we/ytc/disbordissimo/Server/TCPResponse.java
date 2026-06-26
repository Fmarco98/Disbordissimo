package we.ytc.disbordissimo.Server;

import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class TCPResponse extends Thread {

    private Socket client;
    private List<TCPResponse> activeResponses;

    public TCPResponse(Socket client, List<TCPResponse> activeResponses) {
        this.client = client;
        this.activeResponses = activeResponses;

        synchronized (this.activeResponses) {
            this.activeResponses.add(this);
        }
    }

    @Override
    public void run() {
        //Token response
        Logger.logDebug("Responding to " + client.getInetAddress() + ":" + client.getPort());

        Scanner in;
        PrintStream out;
        try {
            in = new Scanner(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String request = in.nextLine();
        Logger.logMsg(request);
        //compute the request
        String result = "result";

        out.println(result);
        in.close();
        out.close();

        this.closeTCPResponse();
    }

    private void closeTCPResponse() {
        try {
            this.client.close();
        } catch (IOException e) { throw new RuntimeException(e); }
        synchronized (this.activeResponses) {
            this.activeResponses.remove(this);
        }
    }
}
