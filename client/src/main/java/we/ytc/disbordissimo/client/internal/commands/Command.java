/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.ClientFactory;
import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.client.DisbordissimoClient;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * <h1>Abstract Command class</h1>
 * This class represents a generic command. <br>
 * Every "effective" command must be a child of this class. <br>
 * <br>
 * Commands can use TCP sockets by default. The request can be sent by calling
 * {@code send(..)} method. The response can be caught by calling {@code recv()} method.<br>
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
    private Client myClient = null;

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
            ClientFactory.Config conf = getClient().getConfig();
            Socket socket = new Socket(conf.getServerAddress(), conf.getServerPort());
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());

            int exit = this.onActionPerformed(params);

            in.close();
            out.close();
            socket.close();

            return exit;
        } catch (IOException e) {
            return ReturnCodes.SERVER_UNREACHABLE;
        }
    }

    /**
     * Sets the current client.
     *
     * @param client
     *        Current {@link Client}
     *
     * @return {@link Command} itself
     */
    public Command setCurrentClient(Client client) {
        this.myClient = client;
        return this;
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

    /**
     * Gets the client.
     *
     * @return {@link Client}
     */
    protected Client getClient() {
        return myClient;
    }
}
