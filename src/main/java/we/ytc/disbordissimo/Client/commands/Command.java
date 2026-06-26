package we.ytc.disbordissimo.Client.commands;

import we.ytc.disbordissimo.Client.utils.SocketManager;

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
 *
 * @param <ReturnType>
 *        The onPerformed method return type
 */
public abstract class Command<ReturnType> {

    private String commandName;
    private SocketManager sm;

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
     * Logic of the command. <br>
     * This function will be implemented into the specific command
     *
     * @param params
     *        Calling params
     *
     * @return something
     */
    public abstract ReturnType onPerformed(Object ...params);

    /**
     * Gets the SocketManager
     *
     * @return SocketManger
     */
    protected SocketManager getSocketManager() {
        return sm;
    }

    /**
     * Opens a new socket.
     *
     * @param host
     *        Server host
     * @param port
     *        Server port
     */
    protected void openSocket(String host, int port) {
        this.closeSocket();
        sm = new SocketManager(host, port);
    }

    /**
     * Sends a {@code request} to the sever.
     *
     * @param request
     *        String request
     *
     * @return {@code true} if operation completed;
     *         otherwise {@code false}
     */
    protected boolean send(String request) {
        if(sm != null) {
            sm.send(request);
            return true;
        }
        return false;
    }

    /**
     * Receives the response message from the sever.
     *
     * @return {@code response} if operation completed;
     *         otherwise {@code null}
     */
    protected String recv() {
        return sm != null ? sm.recv() : null;
    }

    /**
     * Closes the SocketManager
     */
    protected void closeSocket() {
        if(sm != null) {
            sm.close();
        }
    }
}
