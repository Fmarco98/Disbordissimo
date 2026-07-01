package we.ytc.disbordissimo.server.utils.db;

import we.ytc.disbordissimo.server.utils.db.exceptions.NotBoundParamsException;

import java.sql.*;

public class DBUtils {
    private static final String PROTOCOL = "jdbc";
    private static final String SUBPROTOCOL = "mysql";

    private class Default {
        protected static final String HOST = "localhost";
        protected static final int PORT = 3306;
    }

    public static Connection connect(String user, String pwd, String db) throws SQLException {
        return connect(Default.HOST, user, pwd, db);
    }

    public static Connection connect(String host, String user, String pwd, String db) throws SQLException {
        return connect(host, Default.PORT, user, pwd, db);
    }

    public static Connection connect(String host, int port, String user, String pwd, String db) throws SQLException {
        String url = PROTOCOL+":"+SUBPROTOCOL+"://"+host+":"+port+"/"+db+"?";

        return DriverManager.getConnection(url, user, pwd);
    }

    /**
     * Binds the query params. The {@code query} must be a prepared statement, the query params are into the {@code params} argument.<br>
     * <br>
     * Data formats:<br>
     *  - s -> string<br>
     *  - i -> int<br>
     *  - l -> long<br>
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
    public static PreparedStatement bindParams(Connection con, String query, String types, Object... params) throws SQLException {
        if(types.length() != params.length) {
            throw new NotBoundParamsException();
        }

        PreparedStatement stmt = con.prepareStatement(query);
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
                case 'l':
                    stmt.setLong(i+1, (long) params[i]);
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

        return stmt;
    }

    public static void startTransaction(Connection con) {
        try {
            con.createStatement().execute("START TRANSACTION;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit(Connection con) {
        try {
            con.createStatement().execute("COMMIT; ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollback(Connection con){
        try {
            con.createStatement().execute("ROLLBACK; ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {}
    }
}
