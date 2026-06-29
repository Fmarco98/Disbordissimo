package we.ytc.disbordissimo.client.commands;

import we.ytc.disbordissimo.client.Client;
import we.ytc.disbordissimo.client.DisbordissimoClient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * <h1>Abstract Command</h1>
 * This class represents a generic command. <br>
 * Every "real" command must be a child of this class. <br>
 * <br>
 * Commands can use TCP sockets by default. To init a socket you can call
 * {@code openSocket(host, port)} method or you can set up the socket from the
 * {@code SocketManager} using a {@code SocketManager.SocketContainer}. <br>
 * <br>
 * Is required to implement the {@code onPerform} method to define the "logic"
 * of the relative command. <br>
 * <br>
 * Child only visible methods:<br>
 *  - getSocketManager()<br>
 *  - openSocket(..)<br>
 *  - send(..)<br>
 *  - recv()<br>
 *  - closeSocket()<br>
 *  <br>
 *  Publics methods:<br>
 *  - onPerformed(..)<br>
 *  - getCommandName()<br>
 */
public abstract class Command {

    private String commandName;
    private Socket socket;
    private Scanner in;
    private PrintStream out;

    /**
     * Constructor.
     * @param name
     *        Command name
     */
    protected Command(String name) {
        this.commandName = name;
    }

    /**
     * Gets the command name.
     *
     * @return command name
     */
    public String getCommandName() {
        return this.commandName;
    }

    /** //TODO: documentation
     * Logic of the command. <br>
     * This function will be implemented into the specific command
     *
     * @param params
     *        Calling params
     *
     * @return exit code
     */
    public abstract int onActionPerformed(String ...params);

    /** //TODO: documentation
     *
     * @param params
     * @return exit code
     */
    public int execute(String ...params) {
        try {
            DisbordissimoClient.Config conf = Client.getConfig();
            socket = new Socket(conf.getServerAddress(), conf.getServerPort());
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int exit = this.onActionPerformed(params);

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exit;
    }

    /**
     * Sends a {@code request} to the sever.
     *
     * @param request
     *        String request
     */
    protected void send(String request) {
        out.println(request);
    }

    /**
     * Receives the response message from the sever.
     *
     * @return {@code response}
     */
    protected String recv() {
        return in.nextLine();
    }
}
