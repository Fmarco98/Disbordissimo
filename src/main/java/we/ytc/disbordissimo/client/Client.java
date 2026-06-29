package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.JoinCommand;
import we.ytc.disbordissimo.client.commands.QuitCommand;
import we.ytc.disbordissimo.client.commands.SignUpCommand;
import we.ytc.disbordissimo.client.commands.TestVoiceChatConnectionCommand;
import we.ytc.disbordissimo.client.exceptions.AlreadyLaunchedException;
import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.common.logger.Logger;

import java.net.DatagramSocket;

public final class Client extends DisbordissimoClient {
    public static final int DATAGRAM_PACKET_SIZE = 8 + AudioUtils.MIC_FRAME_LENGTH;

    private static Client INSTANCE = null;

    private Config config;
    private long userID = -1;
    private Logger logger;

    private DatagramSocket socket;
    private UDPReceiver receiverThread;
    private UDPSender senderThread;

    private boolean lastBoolResult = false;

    protected Client(Config conf, Logger logger) {
        if(INSTANCE != null) {
            throw new AlreadyLaunchedException();
        }
        INSTANCE = this;
        config = conf;
        this.logger = logger;
    }

    @Override
    public synchronized boolean signUp(String username, String password) throws CommandFailedException {
        int exit = new SignUpCommand().execute(username, password);
        if (exit != 0) throw new CommandFailedException(exit);

        return lastBoolResult;
    }

    @Override
    public synchronized boolean login(String username, String password) throws CommandFailedException {
        return false;
    }

    @Override
    public synchronized boolean join(long channelID) throws CommandFailedException {
        //TODO checks
        int exit = new JoinCommand().execute();
        if (exit != 0) throw new CommandFailedException(exit);

        return false;
    }

    @Override
    public synchronized boolean quit(long channelID) throws CommandFailedException {
        int exit = new QuitCommand().execute();
        if (exit != 0) throw new CommandFailedException(exit);

        return false;
    }

    @Override
    public synchronized boolean isConnectedTo(long channelID) throws CommandFailedException {
        int exit = new TestVoiceChatConnectionCommand().execute(String.valueOf(channelID));
        if (exit != 0) throw new CommandFailedException(exit);

        return lastBoolResult;
    }

    @Override
    public synchronized boolean getGuilds() throws CommandFailedException {
        return false;
    }

    @Override
    public synchronized boolean getGuildChannels(long guildID) throws CommandFailedException {
        return false;
    }

    public static void setLastBooleanResult(boolean r) {
        INSTANCE.lastBoolResult = r;
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
}
