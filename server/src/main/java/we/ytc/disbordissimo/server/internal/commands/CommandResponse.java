package we.ytc.disbordissimo.server.internal.commands;

import we.ytc.disbordissimo.common.jsonio.JsonIO;

/**
 * <h1>Command response interface</h1>
 *
 * This interface represents every TCP command response. Each command is defined by a name,
 * to handle the command the method {@code getCommandName} must be equal.
 */
public interface CommandResponse {

    /**
     * Gets the command name.
     * @return command name
     */
    String getCommandName();

    /**
     * Performs the command response.
     *
     * @param params
     *        {@link JsonIO.Req} params
     *
     * @return {@link JsonIO.Resp}
     */
    JsonIO.Resp onPerformed(String ...params);
}
