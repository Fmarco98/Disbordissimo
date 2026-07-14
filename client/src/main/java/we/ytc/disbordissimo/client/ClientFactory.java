package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.internal.Client;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.NullLogger;

public class ClientFactory {
    private ClientFactory() {}

    /**
     * <h1>Config class</h1>
     *
     * It represents the {@link DisbordissimoClient} config. <br>
     * <br>
     * Contains:<br>
     *  - serverAddress<br>
     *  - serverPort<br>
     *  - UDP sockets Timeout<br>
     *  - Kbps TargetRate<br>
     *  - Ping interval<br>
     * <br>
     * Methods:<br>
     *  - getters<br>
     */
    public static class Config {
        private String serverAddress;
        private int serverPort;
        private int UDPTimeOut;
        private int pingInterval;

        //non implementato attualmente
        private int kbps;

        /**
         * Contract Constructor. The {@code kbps} and {@code UDPTimeOut} values are set to their default.
         *
         * @param serverAddress
         *        Disbordissimo Server address
         * @param serverPort
         *        Disbordissimo Server port
         */
        public Config(String serverAddress, int serverPort) {
            this(serverAddress, serverPort, 64, 500, 180000 /*3min*/);
        }

        /**
         * Constructor.
         *
         * @param serverAddress
         *        Disbordissimo Server address
         * @param serverPort
         *        Disbordissimo Server port
         * @param kbps
         *        kbps target
         * @param UDPTimeOut
         *        Receiving socket time out
         * @param pingInterval
         *        Ping interval
         */
        public Config(String serverAddress, int serverPort, int kbps , int UDPTimeOut, int pingInterval) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.UDPTimeOut = UDPTimeOut;
            this.kbps = kbps;
            this.pingInterval = pingInterval;
        }

        /**
         * Gets the kbps target rate.
         * @return {@code kbps}
         */
        public int getKbps() {
            return kbps;
        }

        /**
         * Gets the UDP receiving socket time out.
         * @return {@code UDPTimeOut}
         */
        public int getUDPTimeOut() {
            return UDPTimeOut;
        }

        /**
         * Gets the DisbordissimoServer address.
         * @return {@code serverAddress}
         */
        public String getServerAddress() {
            return serverAddress;
        }

        /**
         * Gets the DisbordissimoServer port.
         * @return {@code serverPort}
         */
        public int getServerPort() {
            return serverPort;
        }

        /**
         * Gets the Ping interval.
         * @return {@code pingInterval}
         */
        public int getPingInterval() {
            return pingInterval;
        }
    }

    /**
     * Creates a {@link DisbordissimoClient}. The default associated {@link Logger} is {@link NullLogger}.
     *
     * @param config
     *        The {@link Config}.
     *
     * @return client instance
     */
    public static DisbordissimoClient create(Config config) {
        return create(config, new NullLogger());
    }

    /**
     * Creates a {@link DisbordissimoClient}.
     *
     * @param config
     *        The {@link Config}.
     * @param logger
     *        A {@link Logger}
     *
     * @return client instance
     */
    public static DisbordissimoClient create(Config config, Logger logger) {
        return new Client(config, logger);
    }
}
