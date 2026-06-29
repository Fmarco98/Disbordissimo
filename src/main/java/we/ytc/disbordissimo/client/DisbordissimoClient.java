package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

import java.net.InetAddress;

public abstract sealed class DisbordissimoClient permits Client {

    public static class Config {
        private InetAddress serverAddress;
        private int serverPort;
        private int UDPTimeOut;

        public Config(InetAddress serverAddress, int serverPort) {
            this(serverAddress, serverPort, 500);
        }
        public Config(InetAddress serverAddress, int serverPort, int UDPTimeOut) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.UDPTimeOut = UDPTimeOut;
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

    public abstract boolean signUp(String username, String password) throws CommandFailedException;

    public abstract boolean login(String username, String password) throws CommandFailedException;

    public abstract boolean join(long channelID) throws CommandFailedException;

    public abstract boolean quit(long channelID) throws CommandFailedException;

    //Verifica se connesso ad una voice chat
    public abstract boolean isConnectedTo(long channelID) throws CommandFailedException;

    public abstract boolean getGuilds() throws CommandFailedException;

    public abstract boolean getGuildChannels(long guildID) throws CommandFailedException;

}
