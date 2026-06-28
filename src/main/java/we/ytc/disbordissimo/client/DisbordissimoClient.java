package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.common.logger.Logger;

import java.net.InetAddress;

public abstract sealed class DisbordissimoClient permits Client {

    public static class Config {
        public static Config create() {
            return new Config();
        }

        private Config() {}

        private InetAddress serverAddress;
        private int serverPort;

        public InetAddress getServerAddress() {
            return serverAddress;
        }
        public int getServerPort() {
            return serverPort;
        }
    }

    public static DisbordissimoClient create(Config config, Logger logger) {
        return new Client(config, logger);
    }

    public abstract boolean signUp(String username, String password);

    public abstract boolean login(String username, String password);

    public abstract boolean join(long channelID) throws Exception;

    public abstract boolean quit(long channelID);

    //Verifica se connesso ad una voice chat
    public abstract boolean isConnectedTo(long channelID);

    public abstract boolean getGuilds();

    public abstract boolean getGuildChannels(long guildID);

}
