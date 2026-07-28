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

package we.ytc.disbordissimo.client.internal;

import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.client.internal.commands.Command;
import we.ytc.disbordissimo.client.internal.commands.PingCommand;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

/**
 * <h1>Ping Thread class</h1>
 * The pinger Thread makes ping to the {@code DisbordissimoServer} every {@code pingInterval}.<br>
 * <br>
 * Methods:<br>
 *  - stopThread(..)<br>
 *  - getMediumPing(..)<br>
 *  - getLastPing(..)<br>
 *  - makePing(..)<br>
 *  <br>
 * Features:<br>
 *  - Thread-safe<br>
 *  - Blocking call: The caller thread will wait the result of the first ping,
 *    or of the ping generate by calling {@code makePing()}
 */
public class PingThread extends Thread {

    private int pingInterval;
    private int sumPings;
    private int nPings;
    private Command command;

    private boolean running;
    private boolean serverReachable;
    private boolean waitPing;

    private int lastPing;

    /**
     * Constructor.
     *
     * @param interval
     *        Ping interval
     */
    public PingThread(int interval, Client c) {
        if(interval <= 0) throw new IllegalArgumentException("interval <= 0");

        this.pingInterval = interval;
        sumPings = 0;
        nPings = 0;
        lastPing = -1;

        running = true;
        serverReachable = true;
        waitPing = true;

        command = new PingCommand().setCurrentClient(c);
    }

    @Override
    public void run() {
        while (running) {

            int exit = command.execute();

            switch (exit) {
                case ReturnCodes.SUCCESS:
                    serverReachable = true;
                    sumPings += lastPing;
                    nPings++;
                    break;

                case ReturnCodes.COMMAND_NOT_FOUND:
                    throw new RuntimeException();

                default:
                    serverReachable = false;
            }

            synchronized (this) {
                waitPing = false;
                this.notify();
            }

            try {
                Thread.sleep(pingInterval);
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Stops the thread.
     */
    public synchronized void stopThread() {
        running = false;
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {}
    }

    // This method can be called in PingCommand.java only
    public synchronized void setLastPing(int ping) {
        this.lastPing = ping;
    }

    /**
     * Gets the medium ping.
     *
     * @return medium ping
     */
    public synchronized int getMediumPing() {
        waitPing();

        if(!serverReachable) throw new UnreachableServerException();
        return Math.round( (float)sumPings / nPings );
    }

    /**
     * Gets last ping.
     *
     * @return last ping
     */
    public synchronized int getLastPing() {
        waitPing();

        if(!serverReachable) throw new UnreachableServerException();
        return lastPing;
    }

    /**
     * Forces the pinger thread to make a ping.<br>
     *
     * The next call of {@code getLastPing()} or {@code getMediumPing()} will wait the ping result.
     */
    public synchronized void makePing() {
        waitPing = true;
        this.interrupt();
    }

    private void waitPing() {
        synchronized (this) {
            if(waitPing)
                try {
                    this.wait();
                } catch (InterruptedException e) {}
        }
    }
}
