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
    private boolean firstPing;

    private int lastPing;

    public PingThread(int interval) {
        if(interval <= 0) throw new IllegalArgumentException("interval <= 0");

        this.pingInteval = interval;
        sumPings = 0;
        nPings = 0;
        lastPing = -1;

        running = true;
        serverReachable = true;
        firstPing = true;
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
                firstPing = false;
                this.notify();
            }

            try {
                Thread.sleep(pingInteval);
            } catch (InterruptedException e) {}
        }
    }

    public void stopThread() {
        running = false;
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {}
    }

    public void setLastPing(int ping) {
        this.lastPing = ping;
    }

    public int getMediumPing() {
        //Waits the first ping result
        synchronized (this) {
            if(firstPing) {
                try {
                    this.wait();
                } catch (InterruptedException e) {}
            }
        }

        if(!serverReachable) throw new UnreachableServerException();
        return Math.round( (float)sumPings / nPings );
    }

    public int getLastPing() {
        //Waits the first ping result
        synchronized (this) {
            if(firstPing) {
                try {
                    this.wait();
                } catch (InterruptedException e) {}
            }
        }

        if(!serverReachable) throw new UnreachableServerException();
        return lastPing;
    }
}
