package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.JsonIO;
import we.ytc.disbordissimo.server.commands.CommandResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

//TODO: documentation

/**
 * <h1>TCP Response class</h1>
 *
 *
 *
 */
public class TCPResponse extends Thread {

    private Socket client;
    private Scanner in;
    private PrintStream out;
    private List<TCPResponse> activeResponses;
    private List<CommandResponse> commandHandlers;

    /**
     * Constructor.
     *
     * @param client
     *        TCP Client
     * @param activeResponses
     *        TCP Server active responses list
     * @param commandsHandlers
     *        {@link CommandResponse} list
     */
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

        try {
            in = new Scanner(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            Main.getLogger().logError("An Error occurred while opening I/O streams: " + e.getMessage());
            throw new RuntimeException(e);
        }

        JsonIO.Req request = JsonIO.deserializeReq(in.nextLine());

        var ref = new Object() {
            boolean commandFound = false;
            JsonIO.Resp response;
        };
        this.commandHandlers.stream().forEach(command -> {
            if(command.getCommandName().equals(request.cmdName)) {
                ref.commandFound = true;
                ref.response = command.onPerformed(toArray(request.params));
            }
        });

        String jsonResponse = ref.commandFound ? JsonIO.serializeResp(ref.response) : JsonIO.CMD_NOT_FOUND_RESPONSE;
        out.println(jsonResponse);

        this.closeTCPResponse();
    }

    private void closeTCPResponse() {
        String address = String.valueOf(client.getInetAddress());
        int port = client.getPort();
        try {
            out.close();
            in.close();
            client.close();
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
