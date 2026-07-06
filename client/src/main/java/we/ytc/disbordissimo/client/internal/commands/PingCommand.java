package we.ytc.disbordissimo.client.internal.commands;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.TimeUtils;
import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <h1>Ping Command</h1>
 * Makes a ping to the server.
 */
public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public int onActionPerformed(String... params) {
        JsonIO.Req request = new JsonIO.Req(super.getCommandName(), null);
        String jsonRequest = JsonIO.serializeReq(request);

        long t0 = TimeUtils.currentTimestamp();
        super.send(jsonRequest);

        JsonIO.Resp response = JsonIO.deserializeResp(super.recv());
        switch (response.code) {
            case ReturnCodes.SUCCESS:
                long t1 = Long.valueOf(response.result.get(0));
                Client.getPingThread().setLastPing((int)(t1 - t0));
                return ReturnCodes.SUCCESS;

            case ReturnCodes.COMMAND_NOT_FOUND:
                Client.getLogger().logWarning("An invalid command was sent.");
                return ReturnCodes.COMMAND_NOT_FOUND;

            case ReturnCodes.ERROR:
                Client.getLogger().logError("A server error occurred");
                return ReturnCodes.ERROR;

            default:
                Client.getLogger().logWarning("Unknown response code; response=" + response);
                return ReturnCodes.ERROR;
        }
    }
}
