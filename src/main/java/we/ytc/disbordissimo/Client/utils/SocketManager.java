package we.ytc.disbordissimo.Client.utils;

import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketManager {

    /**
     * <h1>Socket Container data class</h1>
     * Represent a set of socket, Scanner and PrintStream
     */
    public class SocketContainer {
        private Socket socket;
        private Scanner in;
        private PrintStream out;

        /**
         * Constructor
         * @param soc
         *        Socket
         * @param in
         *        Scanner of {@code socket.getInputStream()}
         * @param out
         *        PrintStream of {@code socket.getOutputStream()}
         */
        public SocketContainer(Socket soc, Scanner in, PrintStream out) {
            this.socket = soc;
            this.in = in;
            this.out = out;
        }

        /**
         * Gets the Scanner (InputStream)
         * @return Scanner
         */
        public Scanner getIn() {return in;}

        /**
         * Gets the Socket
         * @return Socket
         */
        public Socket getSocket() {return socket;}

        /**
         * Gets the PrintWriter (OutputStream)
         * @return PrintWriter
         */
        public PrintStream getOut() {return out;}
    }

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private boolean open;

    /**
     * Constructor. Create a socket to connect to {@code host:port}
     *
     * @param host
     *        Server hostname
     * @param port
     *        Server port
     */
    public SocketManager(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Logger.logDebug("connected to " + host + ":" + port);

        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        open = true;
    }

    /**
     * Constructor. Creates a SocketManager form a {@code SocketContainer}. <br>
     * Warning: if you create a SocketManger from the {@code SocketContainer}
     * don't close the socket in the other SocketManger instance.
     * @param sc
     *        the Socket Container
     */
    public SocketManager(SocketContainer sc) {
        if(sc == null) throw new IllegalArgumentException("socketContainer == null");

        this.socket = sc.getSocket();
        this.in = sc.getIn();
        this.out = sc.getOut();
        open = true;
    }

    /**
     * Gets the socket, in-stream and out-stream into the form of a {@code SocketContainer}
     * @return socketContainer if the socket is open;
     *         {@code null} otherwise
     */
    public SocketContainer getSocketContainer() {
        return open ? new SocketContainer(this.socket, this.in, this.out) : null;
    }

    /**
     * Closes the {@code socket}. When closed, it's no longer possible to perform any operation.
     */
    public void close() {
        open = false;
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the server.
     * @param msg
     *        message to be sent
     */
    public void send(String msg) {
        if(!open) return; //TODO: custom exption

        out.println(msg);
    }

    /**
     * Receives the server response.<br>
     * This is a blocking function.
     *
     * @return server response if the socket is open;
     *         {@code null} otherwise
     */
    public String recv() {
        if(!open) return null;

        return in.nextLine();
    }

    /**
     * Checks if the socket is open.
     *
     * @return {@code true} if the socket is open;
     *         otherwise {@code false}
     */
    public boolean isOpen() {
        return open;
    }
}
