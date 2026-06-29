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

    public abstract void signUp(String username, String password) throws CommandFailedException;

    public abstract void login(String username, String password) throws CommandFailedException;

    public abstract void join(long channelID) throws CommandFailedException;

    public abstract void quit(long channelID) throws CommandFailedException;

    //Verifica se connesso ad una voice chat
    public abstract boolean isConnectedTo(long channelID) throws CommandFailedException;

    public abstract List<String> getGuilds() throws CommandFailedException;

    public abstract List<String> getGuildChannels(long guildID) throws CommandFailedException;

}
