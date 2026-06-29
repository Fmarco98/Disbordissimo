package we.ytc.disbordissimo.common.logger;

public class NullLogger implements Logger {

    private boolean open;

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
