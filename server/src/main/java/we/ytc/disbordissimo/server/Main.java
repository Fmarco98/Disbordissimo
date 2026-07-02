package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpError;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

public class Main {

    private static Logger logger;

    public static void main(String[] args) throws Exception {

        Config config = null;
        if (Config.configFileExists()) {
            config = Config.loadConfig();
        } else {
            getLogger().logWarning("Couldn't find config file. Creating one...");
            config = Config.defaultConfig();
        }

        getLogger().logMsg("Setting up logger based on config...");
        changeLogger(config);
        getLogger().logMsg("Logger loaded!");

        DisbordissimoServer server = new DisbordissimoServer(config, getLogger());
        server.start();

        server.join();
        server.stopServer();
    }

    /**
     * Gets the global {@link Logger}.
     *
     * @return {@link Logger} object
     */
    public static Logger getLogger() {
        if(logger == null) {
            logger = new YtcLogger();
        }
        return logger;
    }

    /** //TODO: documentation
     *
     */
    public static void changeLogger(Config config) {
        if(logger != null) logger.close();
        try {
            if (config.loggerConfig.isFileEnabled) {
                if (config.loggerConfig.isDefaultLogFile) {
                    logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, true);
                } else {
                    logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, config.loggerConfig.filePath);
                }
            } else {
                logger = new YtcLogger(config.loggerConfig.isConsoleEnabled, false);
            }
        } catch (FileSetUpError e) {
            throw new RuntimeException(e);
        }
    }
}
