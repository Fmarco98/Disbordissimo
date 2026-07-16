package we.ytc.disbordissimo.server.internal.networking;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.server.DisbordissimoServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>TCP Response class</h1>
 *
 * This class represents the core logic of a TCP Response.<br>
 */
public class TCPResponse extends Thread {
    private static Long ResponseID = 0L;

    private Socket client;
    private Scanner in;
    private PrintStream out;
    private TCPServer server;

    /**
     * Constructor.
     *
     * @param client
     *        TCP Client
     * @param server
     *        TCP Server
     */
    public TCPResponse(Socket client, TCPServer server) {
        synchronized (ResponseID) {
            this.setName("TCP-Response-" + ResponseID);
            ResponseID++;
        }

        this.client = client;
        this.server = server;

        var activeResponses = server.getActiveResponses();
        synchronized (activeResponses) {
            activeResponses.add(this);
        }
    }

    //Token response
    @Override
    public void run() {
//        DisbordissimoServer.getServer().getLogger().logDebug(
//                "Responding to " + client.getInetAddress() + ":" + client.getPort()
//        );

        try {
            in = new Scanner(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            DisbordissimoServer.getServer().getLogger().logError(
                    "An Error occurred while opening I/O streams: " + e.getMessage()
            );
            throw new RuntimeException(e);
        }

        JsonIO.Req request = JsonIO.deserializeReq(in.nextLine());

        var ref = new Object() {
            boolean commandFound = false;
            JsonIO.Resp response;
        };
        server.getCommandHandlers().stream().forEach(command -> {
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

        var activeResponses = server.getActiveResponses();
        try {
            out.close();
            in.close();
            client.close();
            synchronized (activeResponses) {
                activeResponses.remove(this);
            }
        } catch (IOException e) {
            DisbordissimoServer.getServer().getLogger().logError(
                    "An Error occurred while closing the client socket("+address+":"+port+") : " + e.getMessage()
            );
            synchronized (activeResponses) {
                activeResponses.remove(this);
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
