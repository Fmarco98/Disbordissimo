package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.JoinCommand;
import we.ytc.disbordissimo.client.commands.QuitCommand;
import we.ytc.disbordissimo.client.commands.SignUpCommand;
import we.ytc.disbordissimo.client.exceptions.AlreadyLaunchedException;
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

    protected Client(Config conf, Logger logger) {
        if(INSTANCE != null) {
            throw new AlreadyLaunchedException();
        }
        INSTANCE = this;
        config = conf;
        this.logger = logger;
    }

    @Override
    public boolean signUp(String username, String password) {
        return new SignUpCommand().execute(username, password);
    }

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean join(long channelID) throws Exception {
        //TODO checks

        socket = new DatagramSocket();
        receiverThread = new UDPReceiver(socket);
        senderThread = new UDPSender(socket, config.getServerAddress(), config.getServerPort());

        boolean exit_status = new JoinCommand().execute();

        receiverThread.start();
        senderThread.start();

        return exit_status;
    }

    @Override
    public boolean quit(long channelID) {
        boolean exit_status = new QuitCommand().execute();

        senderThread.stopThread();
        receiverThread.stopThread();
        socket.close();

        return exit_status;
    }

    @Override
    public boolean isConnectedTo(long channelID) {
        return false;
    }

    @Override
    public boolean getGuilds() {
        return false;
    }

    @Override
    public boolean getGuildChannels(long guildID) {
        return false;
    }

    public Config getConfig() {
        return config;
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
