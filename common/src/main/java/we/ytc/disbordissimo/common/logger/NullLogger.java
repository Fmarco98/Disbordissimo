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
 * <h1>NullLogger class</h1>
 * It's an implementation of {@link Logger}.<br>
 * This logger do nothing.
 */
public class NullLogger implements Logger {

    private boolean open;

    /**
     * Constructor.
     */
    public NullLogger() {
        open = true;
    }

    @Override
    public void log(Type level, String msg) {}

    @Override
    public void logln(Type level, String msg) {}

    @Override
    public void logMsg(String msg) {}

    @Override
    public void logError(String msg) {}

    @Override
    public void logDebug(String msg) {}

    @Override
    public void logWarning(String msg) {}

    @Override
    public void log(Type level, String msg, boolean nl) {}

    @Override
    public boolean isClosed() {
        return !open;
    }

    @Override
    public void close() {
        open = false;
    }
}
