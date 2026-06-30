package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

import java.net.InetAddress;
import java.util.List;

public abstract sealed class DisbordissimoClient permits Client {

    public static class Config {
        private InetAddress serverAddress;
        private int serverPort;
        private int UDPTimeOut;
        private int kbps;

        public Config(InetAddress serverAddress, int serverPort) {
            this(serverAddress, serverPort, 64, 500);
        }
        public Config(InetAddress serverAddress, int serverPort, int kbps , int UDPTimeOut) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.UDPTimeOut = UDPTimeOut;
            this.kbps = kbps;
        }

        public int getKbps() {
            return kbps;
        }
        public int getUDPTimeOut() {
            return UDPTimeOut;
        }
        public InetAddress getServerAddress() {
            return serverAddress;
        }
        public int getServerPort() {
            return serverPort;
        }
    }

    public static DisbordissimoClient create(Config config) {
        return create(config, new YtcLogger());
    }
    public static DisbordissimoClient create(Config config, Logger logger) {
        return new Client(config, logger);
    }

    public abstract void setPacketSendingHandler(PacketSendingHandler sending);
    public abstract void setPacketReceivedHandler(PacketReceivedHandler received);

    public abstract void signUp(String username, String password) throws CommandFailedException;

    public abstract void login(String username, String password) throws CommandFailedException;
    public abstract void logout();
    public abstract boolean isLoggedIn();

    public abstract void join(String channel, String guild) throws CommandFailedException;

    public abstract void quit(String channel, String guild) throws CommandFailedException;

    //Verifica se connesso ad una voice chat
    public abstract boolean isConnectedTo(String channel, String guild) throws CommandFailedException;

    public abstract String[] getGuilds() throws CommandFailedException;

    public abstract String[] getGuildChannels(String guild) throws CommandFailedException;

    public abstract void createGuild(String guild) throws CommandFailedException;
    public abstract void createGuildChannel(String channel, String guild) throws CommandFailedException;

}
