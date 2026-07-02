package we.ytc.disbordissimo.server.internal.networking;

import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.commands.CommandResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/** //TODO: documentation
 *
 */
public class TCPServer extends Thread {

    private boolean running;
    private ServerSocket server;
    private List<TCPResponse> activeResponses;
    private List<CommandResponse> commandsHandlers;

    public TCPServer(int port, List<CommandResponse> commandHandlers) throws IOException {
        super("TCP-Server");
        server = new ServerSocket(port);
        activeResponses = new ArrayList<>();
        this.commandsHandlers = commandHandlers;

        running = true;
    }

    @Override
    public void run() {
        while(running) {
            Socket client;
            try {
                client = server.accept();
            } catch (IOException e) {
                DisbordissimoServer.getServer().getLogger().logError("An Error occurred while accepting a client");
                continue;
            }

            TCPResponse response = new TCPResponse(client, this);
            response.start();
        }

        synchronized (activeResponses) {
            activeResponses.stream().forEach(response -> {
                try {
                    response.join();
                } catch (InterruptedException e) {
                    DisbordissimoServer.getServer().getLogger().logError("TCPResponses joining: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the list of TCP active responses
     *
     * @return TCO active responses
     */
    public synchronized List<TCPResponse> getActiveResponses() {
        return activeResponses;
    }

    public synchronized List<CommandResponse> getCommandHandlers() {
        return commandsHandlers;
    }

    public synchronized void stopServer() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}
