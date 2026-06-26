package we.ytc.disbordissimo.Server;

import we.ytc.disbordissimo.Common.JsonIO;
import we.ytc.disbordissimo.Server.commands.CommandResponse;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TCPResponse extends Thread {

    private Socket client;
    private List<TCPResponse> activeResponses;
    private List<CommandResponse> commandHandlers;

    public TCPResponse(Socket client, List<TCPResponse> activeResponses, List<CommandResponse> commandsHandlers) {
        this.client = client;
        this.activeResponses = activeResponses;
        this.commandHandlers = commandsHandlers;

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

        JsonIO.Req request = JsonIO.deserializeReq(in.nextLine());

        var ref = new Object() {JsonIO.Resp response;};
        this.commandHandlers.stream().forEach(command -> {
            if(command.getCommandName().equals(request.cmdName)) {
                ref.response = command.onPerformed((String[]) request.params.toArray());
            }
        });

        out.println(JsonIO.serializeResp(ref.response));

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
