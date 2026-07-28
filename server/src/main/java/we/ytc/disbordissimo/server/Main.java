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

package we.ytc.disbordissimo.server;

import we.ytc.disbordissimo.common.fm.exceptions.FileSetUpException;
import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

/**
 * Disbordissimo Server main launcher class.
 */
public class Main {
    private static Logger logger;

    /**
     * Main.
     */
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

    /**
     * Creates a new logger with the proprieties specified in {@link Config}. <br>
     * The previous logger is closed during the {@code changeLogger} operation.
     *
     * @param config
     *        The {@link Config}
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
        } catch (FileSetUpException e) {
            throw new RuntimeException(e);
        }
    }
}
