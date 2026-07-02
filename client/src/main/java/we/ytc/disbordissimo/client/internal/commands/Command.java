package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.client.DisbordissimoClient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * <h1>Abstract Command class</h1>
 * This class represents a generic command. <br>
 * Every "effective" command must be a child of this class. <br>
 * <br>
 * Commands can use TCP sockets by default. The request can be sent by calling
 * {@doce send(..)} method. The response can be caught by calling {@code recv()} method.<br>
 * Important: The architecture is base on the concept of token server. This means that is possible
 * calling {@code send(..)} and {@code recv} methods only one time each.<br>
 * <br>
 * It's required to implement the {@code onActionPerform} method to define the "logic"
 * of the relative command. <br>
 * Calling the {@code execute} method will perform the command.<br>
 * <br>
 * Child only visible methods:<br>
 *  - send(..)<br>
 *  - recv()<br>
 *  - onActionPerform(..)<br>
 *  <br>
 *  Publics methods:<br>
 *  - execute(..)<br>
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

    /**
     * The Command logic. <br>
     * <br>
     * This method must be implemented by the developer.
     *
     * @param params
     *        Calling params
     *
     * @return {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes}
     */
    public abstract int onActionPerformed(String ...params);

    /**
     * Executes the command.
     *
     * @param params
     *        Calling params
     *
     * @return {@link we.ytc.disbordissimo.common.jsonio.ReturnCodes}
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
