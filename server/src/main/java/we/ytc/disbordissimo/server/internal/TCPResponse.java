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

package we.ytc.disbordissimo.server.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import we.ytc.disbordissimo.common.jsonio.MsgCodes;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
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
 * Core logic of a TCP Response.<br>
 */
public class TCPResponse implements Runnable {
    private static final Gson gson = new GsonBuilder().create();

    private Socket client;
    private List<CommandResponse> commandsHandlers;

    public TCPResponse(Socket client, List<CommandResponse> commandsHandlers) {
        this.client = client;
        this.commandsHandlers = commandsHandlers;
    }

    @Override
    public void run() {
        //TODO sistemare tutte le risposte
        String address = String.valueOf(client.getInetAddress());
        int port = client.getPort();

        DisbordissimoServer.getServer().getLogger().logDebug("Responding to " + address + ":" + port);

        try (
                Scanner in = new Scanner(client.getInputStream());
                PrintStream out = new PrintStream(client.getOutputStream())
        ) {
            while(in.hasNextLine()) {
                String jsonRequest = in.nextLine();
                DisbordissimoServer.getServer().getLogger().logDebug("RECV: \""+jsonRequest+"\"");
                JsonObject request = gson.fromJson(jsonRequest, JsonObject.class);

                var ref = new Object() {
                    boolean commandFound = false;
                    JsonObject response = null;
                };
                commandsHandlers.stream().forEach(command -> {
                    if(command.getCommandName().equals(request.get("cmd").getAsString())) {
                        ref.commandFound = true;
                        ref.response = command.onPerformed(request);
                    }
                });

                String jsonResponse = "";
                if(ref.commandFound) {
                    ref.response.addProperty("transaction", request.get("transaction").getAsString());
                    jsonResponse = gson.toJson(ref.response);
                } else {
                    JsonObject cmdNotFound = new JsonObject();
                    cmdNotFound.addProperty("code", ReturnCodes.COMMAND_NOT_FOUND);
                    cmdNotFound.addProperty("msgCode", MsgCodes.COMMAND_NOT_FOUND);
                    cmdNotFound.addProperty("transaction", request.get("transaction").getAsString());
                    jsonResponse = gson.toJson(cmdNotFound);
                }

                DisbordissimoServer.getServer().getLogger().logDebug("SEND: \""+jsonResponse+"\"");

                out.println(jsonResponse);
            }

            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            DisbordissimoServer.getServer().getLogger().logError(
                    "An IO Error occurred while responding to client={"+address+":"+port+"}: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
