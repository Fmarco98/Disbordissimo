package we.ytc.disbordissimo.server.internal;

import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.commands.CommandResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <h1>TCP Server class</h1>
 *
 * The class is a TCP Server, the server runs on its own thread after the construction.<br>
 * The TCP server is implemented as a Token server. It aims to response the DisbordissimoClient API Requests.<br>
 * <br>
 * Request structure: {@link we.ytc.disbordissimo.common.jsonio.JsonIO.Req}<br>
 * Response structure: {@link we.ytc.disbordissimo.common.jsonio.JsonIO.Resp}<br>
 */
public class TCPServer extends Thread {
    private static final int POOL_N_THREADS = 4;

    private boolean running;
    private ServerSocket server;
    private List<TCPResponse> activeResponses;
    private List<CommandResponse> commandsHandlers;

    /**
     * Constructor.
     *
     * @param port
     * @param commandHandlers
     * @throws IOException
     */
    public TCPServer(int port, List<CommandResponse> commandHandlers) throws IOException {
        super("TCP-Server");
        server = new ServerSocket(port);
        activeResponses = new ArrayList<>();
        this.commandsHandlers = commandHandlers;

        running = true;
    }

    @Override
    public void run() {
        ExecutorService threadPool = Executors.newFixedThreadPool(POOL_N_THREADS);

        while(running) {
            Socket client;
            try {
                client = server.accept();
            } catch (IOException e) {
                DisbordissimoServer.getServer().getLogger().logError("An Error occurred while accepting a client");
                continue;
            }

            threadPool.submit(() -> {
                TCPResponse.respond(client, commandsHandlers);
            });
        }

        threadPool.close();
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the list of TCP active responses
     *
     * @return TCP active responses
     */
    public synchronized List<TCPResponse> getActiveResponses() {
        return activeResponses;
    }

    /**
     * Gets the list of Commands Handlers.
     *
     * @return {@code commandsHandlers}
     */
    public synchronized List<CommandResponse> getCommandHandlers() {
        return commandsHandlers;
    }

    /**
     * Stops the TCP server.
     */
    public synchronized void stopServer() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException e) {}
    }
}
