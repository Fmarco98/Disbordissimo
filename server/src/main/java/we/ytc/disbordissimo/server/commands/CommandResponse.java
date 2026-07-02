package we.ytc.disbordissimo.server.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;

//TODO: documentatio

/**
 * <h1>Command response interface</h1>
 *
 */
public interface CommandResponse {

    /**
     * Gets the command name.
     * @return command name
     */
    String getCommandName();

    /** //TODO: documentatio
     * Command response action
     *
     * @param params
     * @return
     */
    JsonIO.Resp onPerformed(String ...params);
}
