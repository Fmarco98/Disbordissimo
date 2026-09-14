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

package we.ytc.disbordissimo.client.internal;

import we.ytc.disbordissimo.client.DisbordissimoClient;
import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.client.internal.commands.*;
import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.exceptions.NotLoggedInException;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.common.logger.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static we.ytc.disbordissimo.client.ClientFactory.Config;

/**
 * <h1>Client class</h1>
 * It's an implementation of {@link DisbordissimoClient}<br>
 * <br>
 * Features:<br>
 *  - Thread-safe
 */
public final class Client implements DisbordissimoClient {
    private String username;
    private long userID = -1;

    private WebRTCClient rtcClient;
    private Socket clientSoc;
    private Scanner socIN;
    private PrintStream socOUT;

    private Logger logger;
    private Config config;

    private boolean lastBoolResult = false;
    private List<String> lastStringList = null;
    private int lastIntResult = 0;
    private String lastJoinedChannelCh = "";
    private String lastJoinedChannelGuild = "";

    public Client(Config conf, Logger logger) {
        config = conf;
        this.logger = logger;

        try {
            clientSoc = new Socket(conf.getServerAddress(), conf.getServerPort());
            socIN = new Scanner(clientSoc.getInputStream());
            socOUT = new PrintStream(clientSoc.getOutputStream());
        } catch (IOException e) {
            throw new UnreachableServerException();
        }
    }

    @Override
    public synchronized void signUp(String username, String password) throws CommandFailedException {
        int exit = new SignUpCommand(this)
                .execute(username, password);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void login(String username, String password) throws CommandFailedException {
        int exit = new LoginCommand(this)
                .execute(username, password);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void logout() {
        if(!this.isLoggedIn()) return;

        try {
            quitChannel(lastJoinedChannelCh, lastJoinedChannelGuild);
        } catch (CommandFailedException e) {}

        this.userID = -1;
    }

    @Override
    public synchronized boolean isLoggedIn() {
        return userID != -1;
    }

    @Override
    public synchronized void joinChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new JoinChannelCommand(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        lastJoinedChannelCh = channel;
        lastJoinedChannelGuild = guild;
    }

    @Override
    public synchronized void quitChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new QuitChannelCommand(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized boolean isConnectedTo(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new TestVoiceChatConnectionCommand(this)
                .execute(channel, guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastBoolResult;
    }

    @Override
    public synchronized void reconnectToChannel(String channel, String guild) throws CommandFailedException {
        quitChannel(channel, guild);
        joinChannel(channel, guild);
    }

    @Override
    public synchronized String[] getGuilds() throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildsCommand(this)
                .execute();

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized String getGuildOwner(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildOwnerCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.get(0);
    }

    @Override
    public synchronized String[] getGuildChannels(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildChannelsCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized void createGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new CreateGuildCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void createGuildChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new CreateGuildChannelCommand(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void joinGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new JoinGuildCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void leaveGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new LeaveGuildCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void dropGuildChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new DropGuildChannelCommand(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void dropGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new DropGuildCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void destroy() {
        this.logout();

        try {
            socIN.close();
            socOUT.close();
            clientSoc.close();
        } catch (IOException e) {
            logger.logError("An error occurred while closing the client: "+ e);
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String[] getGuildMemers(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildMembersCommand(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized String[] getChannelConnectedMembers(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildChannelConnectedMembersCommand(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized int ping(){
        int exit = new PingCommand(this)
                .execute();

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();

        return lastIntResult;
    }

    public void setLastBooleanResult(boolean r) {
        lastBoolResult = r;
    }
    public void setLastStringList(List<String> r) {
        lastStringList = r;
    }
    public void setWebRTCClient(WebRTCClient client) {
        rtcClient = client;
    }
    public WebRTCClient getWebRTCClient() {
        return rtcClient;
    }
    public Config getConfig() {
        return config;
    }
    public Logger getLogger() {
        return logger;
    }
    public void setUserID(long id) {
        userID = id;
    }
    public long getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public PrintStream getSocketOUT() {
        return socOUT;
    }
    public Scanner getSocketIN() {
        return socIN;
    }
    public void setLastInt(int n) {
        lastIntResult = n;
    }

    private void checksLoggedIn() {
        if(!isLoggedIn()) throw new NotLoggedInException();
    }

}
