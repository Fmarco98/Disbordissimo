package we.ytc.disbordissimo.server.utils.db;

import we.ytc.disbordissimo.server.utils.db.exceptions.ClosedException;
import we.ytc.disbordissimo.server.utils.db.exceptions.NotBoundParamsException;

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
     * Constructor. The connection will use the default {@code host} and {@code port}.
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
     * Executes a query. The {@code query} must be a prepared statement, the query params are into the {@code params} argument.<br>
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
     * @return the DB's response
     */
    public synchronized ResultSet execute(String query, String types, Object... params) throws SQLException {
        if(this.isClosed) {
            throw new ClosedException();
        }
        if(types.length() != params.length) {
            throw new NotBoundParamsException();
        }

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
        return stmt.getResultSet();
    }

    /**
     * Executes a query.
     *
     * @param query
     *        The query
     *
     * @return the DB's response
     */
    public synchronized ResultSet execute(String query) throws SQLException {
        return this.execute(query, "", new String[]{});
    }

    /**
     * Starts a Transaction.
     */
    public synchronized void startTransaction() {
        try {
            this.execute("START TRANSACTION;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commits a Transaction. Used when the transaction ended as intended;
     */
    public synchronized void commit() {
        try {
            this.execute("COMMIT;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rollbacks a Transaction. Used when the transaction didn't end as intended; If a rollback is executed, all queries of the transaction are reversed
     */
    public synchronized void rollback() {
        try {
            this.execute("ROLLBACK;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the {@code DBManager}. When closed, it's no longer possible to perform any operation.
     */
    public synchronized void close() {
        if(this.isClosed) {
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
    }
}
