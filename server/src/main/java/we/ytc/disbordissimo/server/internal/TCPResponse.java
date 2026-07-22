package we.ytc.disbordissimo.server.internal;

import we.ytc.disbordissimo.common.jsonio.JsonIO;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.commands.CommandResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * <h1>TCP Response class</h1>
 *
 * This class represents the core logic of a TCP Response.<br>
 */
public class TCPResponse {

    public static void respond(Socket client, List<CommandResponse> commandsHandlers) {
        String address = String.valueOf(client.getInetAddress());
        int port = client.getPort();

        DisbordissimoServer.getServer().getLogger().logDebug("Responding to " + address + ":" + port);

        try (
                Scanner in = new Scanner(client.getInputStream());
                PrintStream out = new PrintStream(client.getOutputStream())
        ) {
            JsonIO.Req request = JsonIO.deserializeReq(in.nextLine());

            var ref = new Object() {
                boolean commandFound = false;
                JsonIO.Resp response;
            };
            commandsHandlers.stream().forEach(command -> {
                if(command.getCommandName().equals(request.cmdName)) {
                    ref.commandFound = true;
                    ref.response = command.onPerformed(toArray(request.params));
                }
            });

            String jsonResponse = ref.commandFound ? JsonIO.serializeResp(ref.response) : JsonIO.CMD_NOT_FOUND_RESPONSE;
            out.println(jsonResponse);

            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            DisbordissimoServer.getServer().getLogger().logError(
                    "An IO Error occurred while responding to client={"+address+":"+port+"}: " + e.getMessage()
            );
            throw new RuntimeException(e);
        }
    }

    private static String[] toArray(List<String> list) {
        var arr = new String[list.size()];
        for(int i=0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return  arr;
    }
}
