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
