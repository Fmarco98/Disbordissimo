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
import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.common.logger.Logger;

import java.util.List;

import static we.ytc.disbordissimo.client.ClientFactory.Config;

/**
 * <h1>Client class</h1>
 * It's an implementation of {@link DisbordissimoClient}<br>
 * <br>
 * Features:<br>
 *  - Thread-safe
 */
public final class Client implements DisbordissimoClient {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;

    private String username;
    private long userID = -1;

    private WebRTCClient rtcClient;

    private Logger logger;
    private Config config;
    private PingThread pingThread;

    private boolean lastBoolResult = false;
    private List<String> lastStringList = null;
    private String lastJoinedChannelCh = "";
    private String lastJoinedChannelGuild = "";

    public Client(Config conf, Logger logger) {
        config = conf;
        this.logger = logger;

        pingThread = new PingThread(conf.getPingInterval(), this);
        pingThread.start();
    }

    @Override
    public synchronized void signUp(String username, String password) throws CommandFailedException {
        int exit = new SignUpCommand()
                .setCurrentClient(this)
                .execute(username, password);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void login(String username, String password) throws CommandFailedException {
        int exit = new LoginCommand()
                .setCurrentClient(this)
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

        int exit = new JoinChannelCommand()
                .setCurrentClient(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        lastJoinedChannelCh = channel;
        lastJoinedChannelGuild = guild;
    }

    @Override
    public synchronized void quitChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new QuitChannelCommand()
                .setCurrentClient(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized boolean isConnectedTo(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new TestVoiceChatConnectionCommand()
                .setCurrentClient(this)
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

        int exit = new GetGuildsCommand()
                .setCurrentClient(this)
                .execute();

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized String getGuildOwner(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildOwnerCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.get(0);
    }

    @Override
    public synchronized String[] getGuildChannels(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildChannelsCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized void createGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new CreateGuildCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void createGuildChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new CreateGuildChannelCommand()
                .setCurrentClient(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void joinGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new JoinGuildCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void leaveGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new LeaveGuildCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void dropGuildChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new DropGuildChannelCommand()
                .setCurrentClient(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void dropGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new DropGuildCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized int getPing() {
        return pingThread.getMediumPing();
    }

    @Override
    public synchronized boolean isServerReachable() {
        try {
            pingThread.makePing();
            getPing();
            return true;
        } catch (UnreachableServerException e) {
            return false;
        }
    }

    @Override
    public synchronized void destroy() {
        this.logout();

        this.pingThread.stopThread();
    }

    @Override
    public synchronized String[] getGuildMemers(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildMembersCommand()
                .setCurrentClient(this)
                .execute(guild);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized String[] getChannelConnectedMembers(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildChannelConnectedMembersCommand()
                .setCurrentClient(this)
                .execute(guild, channel);

        if (exit == ReturnCodes.SERVER_UNREACHABLE) throw new UnreachableServerException();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
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
    public PingThread getPingThread() {
        return pingThread;
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

    private void checksLoggedIn() {
        if(!isLoggedIn()) throw new NotLoggedInException();
    }
}
