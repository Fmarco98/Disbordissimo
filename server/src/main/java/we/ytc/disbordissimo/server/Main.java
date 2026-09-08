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
import we.ytc.disbordissimo.server.internal.config.Config;
import we.ytc.disbordissimo.server.internal.config.Operations;

/**
 * Disbordissimo Server main launcher class.
 */
public class Main {
    private static Logger logger;

    /**
     * Main.
     */
    public static void main(String[] args) throws Exception {
        Config config = Operations.load();

        System.out.println("Setting up logger based on config...");
        try {
            if (config.logger.isFileEnabled) {
                if (config.logger.isDefaultLogFile) {
                    logger = new YtcLogger(config.logger.isConsoleEnabled, true);
                } else {
                    logger = new YtcLogger(config.logger.isConsoleEnabled, config.logger.filePath);
                }
            } else {
                logger = new YtcLogger(config.logger.isConsoleEnabled, false);
            }
        } catch (FileSetUpException e) {
            throw new RuntimeException(e);
        }

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
}
