package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.*;
import we.ytc.disbordissimo.client.exceptions.AlreadyLaunchedException;
import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.exceptions.NotLoggedInException;
import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;
import we.ytc.disbordissimo.common.logger.Logger;

import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.List;

public final class Client extends DisbordissimoClient {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;

    private static Client INSTANCE = null;

    private Config config;
    private long userID = -1;
    private Logger logger;

    private DatagramSocket socket;
    private UDPReceiver receiverThread;
    private UDPSender senderThread;
    private PacketReceivedHandler onReceived;
    private PacketSendingHandler onSending;

    private boolean lastBoolResult = false;
    private List<String> lastStringList = null;

    protected Client(Config conf, Logger logger) {
        if(INSTANCE != null) {
            throw new AlreadyLaunchedException();
        }
        INSTANCE = this;
        config = conf;
        this.logger = logger;
        onSending = null;
        onReceived = null;
    }

    @Override
    public synchronized void setPacketSendingHandler(PacketSendingHandler sending) {
        onSending = sending;
    }

    @Override
    public synchronized void setPacketReceivedHandler(PacketReceivedHandler received) {
        onReceived = received;
    }

    @Override
    public synchronized void signUp(String username, String password) throws CommandFailedException {
        int exit = new SignUpCommand().execute(username, password);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void login(String username, String password) throws CommandFailedException {
        int exit = new LoginCommand().execute(username, password);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public void logout() {
        this.userID = -1;
    }

    @Override
    public boolean isLoggedIn() {
        return userID != -1;
    }

    @Override
    public synchronized void join(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new JoinCommand().execute(guild, channel);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized void quit(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new QuitCommand().execute(guild, channel);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);
    }

    @Override
    public synchronized boolean isConnectedTo(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new TestVoiceChatConnectionCommand().execute(channel, guild);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastBoolResult;
    }

    @Override
    public synchronized String[] getGuilds() throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildsCommand().execute();
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public synchronized String[] getGuildChannels(String guild) throws CommandFailedException {
        checksLoggedIn();

        int exit = new GetGuildChannelsCommand().execute(guild);
        if (exit != ReturnCodes.SUCCESS) throw new CommandFailedException(exit);

        return lastStringList.toArray(new String[]{});
    }

    @Override
    public void createGuild(String guild) throws CommandFailedException {
        checksLoggedIn();

    }

    @Override
    public void createGuildChannel(String channel, String guild) throws CommandFailedException {
        checksLoggedIn();

    }

    public static void setLastBooleanResult(boolean r) {
        INSTANCE.lastBoolResult = r;
    }
    public static void setLastStringList(List<String> r) {
        INSTANCE.lastStringList = r;
    }

    public static PacketReceivedHandler getOnReceived() {
        return INSTANCE.onReceived;
    }
    public static PacketSendingHandler getOnSending() {
        return INSTANCE.onSending;
    }
    public static UDPSender getSenderThread() {
        return INSTANCE.senderThread;
    }
    public static void setSenderThread(UDPSender senderThread) {
        INSTANCE.senderThread = senderThread;
    }
    public static DatagramSocket getSocket() {
        return INSTANCE.socket;
    }
    public static void setSocket(DatagramSocket socket) {
        INSTANCE.socket = socket;
    }
    public static UDPReceiver getReceiverThread() {
        return INSTANCE.receiverThread;
    }
    public static void setReceiverThread(UDPReceiver receiverThread) {
        INSTANCE.receiverThread = receiverThread;
    }
    public static Config getConfig() {
        return INSTANCE.config;
    }
    public static void setLogger(Logger logger) {
        INSTANCE.logger = logger;
    }
    public static Logger getLogger() {
        return INSTANCE.logger;
    }
    public static void setUserID(long id) {
        INSTANCE.userID = id;
    }
    public static long getUserID() {
        return INSTANCE.userID;
    }
    public static Client getClient() {
        return INSTANCE;
    }

    private void checksLoggedIn() {
        if(!isLoggedIn()) throw new NotLoggedInException();
    }
}
