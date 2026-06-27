package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.common.socketmanager.SocketManager;
import we.ytc.disbordissimo.common.socketmanager.SocketManager.SocketContainer;
import we.ytc.disbordissimo.server.commands.CommandResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class TCPResponse extends Thread {

    private Socket client;
    private SocketManager sm;
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
        Main.getLogger().logDebug("Responding to " + client.getInetAddress() + ":" + client.getPort());

        Scanner in;
        PrintStream out;
        try {
            in = new Scanner(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            Main.getLogger().logError("An Error occurred while opening I/O streams: " + e.getMessage());
            throw new RuntimeException(e);
        }
        sm = new SocketManager(new SocketContainer(client, in, out));


        JsonIO.Req request = JsonIO.deserializeReq(sm.recv());

        var ref = new Object() {
            boolean commandFound = false;
            JsonIO.Resp response;
        };
        this.commandHandlers.stream().forEach(command -> {
            if(command.getCommandName().equals(request.cmdName)) {
                ref.commandFound = true;
                ref.response = command.onPerformed(sm, toArray(request.params));
            }
        });

        String jsonResponse = ref.commandFound ? JsonIO.serializeResp(ref.response) : JsonIO.CMD_NOT_FOUND_RESPONSE;
        sm.send(jsonResponse);

        this.closeTCPResponse();
    }

    private void closeTCPResponse() {
        String address = String.valueOf(client.getInetAddress());
        int port = client.getPort();
        try {
            this.sm.close();
            synchronized (this.activeResponses) {
                this.activeResponses.remove(this);
            }
        } catch (IOException e) {
            Main.getLogger().logError("An Error occurred while closing the client socket("+address+":"+port+") : " + e.getMessage());
            synchronized (this.activeResponses) {
                this.activeResponses.remove(this);
            }
            throw new RuntimeException(e);
        }
    }

    private String[] toArray(List<String> list) {
        var arr = new String[list.size()];
        for(int i=0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return  arr;
    }
}
