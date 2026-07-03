package we.ytc.disbordissimo.client.internal;

import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.client.internal.commands.PingCommand;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

public class PingThread extends Thread {

    private int pingInteval;
    private int sumPings;
    private int nPings;

    private boolean running;
    private boolean serverReachable;
    private boolean waitPing;

    private int lastPing;

    public PingThread(int interval) {
        if(interval <= 0) throw new IllegalArgumentException("interval <= 0");

        this.pingInteval = interval;
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
                Thread.sleep(pingInteval);
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void stopThread() {
        running = false;
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {}
    }

    public synchronized void setLastPing(int ping) {
        this.lastPing = ping;
    }

    public synchronized int getMediumPing() {
        waitPing();

        if(!serverReachable) throw new UnreachableServerException();
        return Math.round( (float)sumPings / nPings );
    }

    public synchronized int getLastPing() {
        waitPing();

        if(!serverReachable) throw new UnreachableServerException();
        return lastPing;
    }

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
