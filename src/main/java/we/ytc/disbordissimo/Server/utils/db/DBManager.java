package we.ytc.disbordissimo.Server.utils.db;

import we.ytc.disbordissimo.Server.utils.db.exceptions.ClosedException;
import we.ytc.disbordissimo.Server.utils.db.exceptions.NotBoundParamsException;
import we.ytc.disbordissimo.Server.utils.logger.Logger;

import java.sql.*;

/**
 * <h1>DBManager class</h1>
 * An interaction Interface for a MySQL DataBase using jdbc protocol.<br>
 * Features:<br>
 *  - Thread-safe<br>
 * <br>
 * Methods:<br>
 *  - constructor(..)<br>
 *  - execute(..)<br>
 *  - startTransaction(..)<br>
 *  - commit(..)<br>
 *  - rollback(..)<br>
 *  - close(..)<br>
 *  <br>
 *  It's suggested to close the DBManager at the end of use (To avoid resource leaks).
 */
public class DBManager {
    private static final String PROTOCOL = "jdbc";
    private static final String SUBPROTOCOL = "mysql";

    private class Defaults {
        protected static final String HOST = "localhost";
        protected static final int PORT = 3306;
    }

    private Connection mysql;
    private boolean isClosed = false;

    /**
     * Constructor. The connection will use the defaults {@code host} and {@code port}.
     *
     * @param user
     *        DB user
     * @param pwd
     *        DB password
     * @param db
     *        DB name
     *
     * @throws SQLException
     */
    public DBManager(String user, String pwd, String db) throws SQLException {
        this(Defaults.HOST, user, pwd, db);
    }

    /**
     * Constructor. The connection will use the default {@code port}.
     *
     * @param host
     *        DB hostname
     * @param user
     *        DB user
     * @param pwd
     *        DB password
     * @param db
     *        DB name
     *
     * @throws SQLException
     */
    public DBManager(String host, String user, String pwd, String db) throws SQLException {
        this(host, Defaults.PORT, user, pwd, db);
    }

    /**
     * Constructor.
     *
     * @param host
     *        DB hostname
     * @param port
     *        DB port
     * @param user
     *        DB user
     * @param pwd
     *        DB password
     * @param db
     *        DB name
     *
     * @throws SQLException
     */
    public DBManager(String host, int port, String user, String pwd, String db) throws SQLException {
        String url = PROTOCOL+":"+SUBPROTOCOL+"://"+host+":"+port+"/"+db+"?";

        mysql = DriverManager.getConnection(url, user, pwd);
    }

    /**
     * Makes a query. The {@code query} must be a prepared statement, the query params ara into the {@code params} argument.<br>
     * <br>
     * Data formats:<br>
     *  - s -> string<br>
     *  - i -> int<br>
     *  - d -> double<br>
     *  - f -> float<br>
     *  - D -> date<br>
     *  - t -> timestamp<br>
     *  - T -> time<br>
     *
     * @param query
     *        A prepared statement query
     * @param types
     *        A string of query params types
     * @param params
     *        Actual query params
     *
     * @return DB response result
     */
    public synchronized ResultSet execute(String query, String types, Object... params) {
        if(this.isClosed) {
            Logger.logError("DB has already been closed");
            throw new ClosedException();
        }
        if(types.length() != params.length) {
            Logger.logError("Query params don't bound with their types");
            throw new NotBoundParamsException();
        }

        try {
            PreparedStatement stmt = this.mysql.prepareStatement(query);
            char[] varTypes = types.toCharArray();

            for(int i=0; i < varTypes.length; i++) {
                char type = varTypes[i];

                switch (type) {
                    case 's':
                        stmt.setString(i+1, (String) params[i]);
                        break;
                    case 'i':
                        stmt.setInt(i+1, (int) params[i]);
                        break;
                    case 'd':
                        stmt.setDouble(i+1, (double) params[i]);
                        break;
                    case 'f':
                        stmt.setFloat(i+1, (float)params[i]);
                        break;
                    case 'D':
                        stmt.setDate(i+1, new Date(((java.util.Date)params[i]).getTime()));
                        break;
                    case 't':
                        stmt.setTimestamp(i+1, (Timestamp) params[i]);
                        break;
                    case 'T':
                        stmt.setTime(i+1, (Time) params[i]);
                        break;
                }
            }

            stmt.execute();
            Logger.logMsg("Query executed");
            return stmt.getResultSet();
        } catch (SQLException e) {
            Logger.logError("DBManager: SQL exception");
            throw new RuntimeException(e);
        }
    }

    /**
     * Makes a query.
     *
     * @param query
     *        The query
     *
     * @return DB response result
     */
    public synchronized ResultSet execute(String query) {
        return this.execute(query, "", new String[]{});
    }

    /**
     * Starts a Transaction.
     */
    public synchronized void startTransaction() {
        this.execute("START TRANSACTION;");
    }

    /**
     * Commits a Transaction. It's represent the transaction good end.
     */
    public synchronized void commit() {
        this.execute("COMMIT;");
    }

    /**
     * Rollbacks a Transaction. It's represent the transaction bad end. If a rollback is executed, it undoes all queries of the transaction.
     */
    public synchronized void rollback() {
        this.execute("ROLLBACK;");
    }

    /**
     * Closes the {@code DBManager}. When it was closed, you won't be able to perform any operation.
     */
    public synchronized void close() {
        if(this.isClosed) {
            Logger.logError("DB has already been closed");
            throw new ClosedException();
        }

        try {
            mysql.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.mysql = null;
            System.gc();
        }

        this.isClosed = true;
        Logger.logDebug("DB closed");
    }
}
