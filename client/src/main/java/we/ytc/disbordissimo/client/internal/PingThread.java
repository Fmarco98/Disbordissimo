package we.ytc.disbordissimo.client.internal;

import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
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
    public PingThread(int interval) {
        if(interval <= 0) throw new IllegalArgumentException("interval <= 0");

        this.pingInterval = interval;
        sumPings = 0;
        nPings = 0;
        lastPing = -1;

        running = true;
        serverReachable = true;
        waitPing = true;
    }

    @Override
    public void run() {
        while (running) {

            int exit = new PingCommand().execute();

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
        if(waitPing)
            try {
                this.wait();
            } catch (InterruptedException e) {}
    }
}
