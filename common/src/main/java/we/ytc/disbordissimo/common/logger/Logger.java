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

package we.ytc.disbordissimo.common.logger;

/**
 * <h1>Logger interface</h1>
 *
 * Log message level are defined in {@link Logger.Type}
 * <br><br>
 * Methods:<br>
 *  - log(..)<br>
 *  - logln(..)<br>
 *  - logMsg(..)<br>
 *  - logDebug(..)<br>
 *  - logWarning(..)<br>
 *  - logError(..)<br>
 */
public interface Logger {
    /**
     * <h1>Log Types enum</h1>
     * Message log level types:<br>
     * - INFO<br>
     * - DEBUG<br>
     * - WARNING<br>
     * - ERROR<br>
     */
     enum Type {
        INFO("INFO"),
        ERROR("ERROR"),
        WARNING("WARNING"),
        DEBUG("DEBUG");

        private String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    /**
     * Logs a message (without new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    void log(Type level, String msg);

    /**
     * Logs a message (with new line). The log operation is performed at the given {@code level}.
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     */
    void logln(Type level, String msg);

    /**
     * Logs a {@code Logger.Type.INFO} message.
     *
     * @param msg
     *        The message
     */
    void logMsg(String msg);

    /**
     * Logs a {@code Logger.Type.ERROR} message.
     *
     * @param msg
     *        The message
     */
    void logError(String msg);

    /**
     * Logs a {@code Logger.Type.DEBUG} message.
     *
     * @param msg
     *        The message
     */
    void logDebug(String msg);

    /**
     * Logs a {@code Logger.Type.WARNING} message.
     *
     * @param msg
     *        The message
     */
    void logWarning(String msg);

    /**
     * Logs a message. The log operation is performed at the given {@code level}.
     * If {@code nl} is true, the {@code msg} will be printed with a new line char.
     *
     *
     * @param level
     *        Logging message level type
     * @param msg
     *        The message
     * @param nl
     *        New line flag
     */
    void log(Type level, String msg, boolean nl);

    /**
     * Checks if the {@link Logger} is closed.
     *
     * @return {@code true} if the {@link Logger} is closed;
     *         {@code false} otherwise
     */
    boolean isClosed();

    /**
     * Closes the {@code Logger}. When closed, it's no longer possible to perform any operation.
     */
    void close();
}
