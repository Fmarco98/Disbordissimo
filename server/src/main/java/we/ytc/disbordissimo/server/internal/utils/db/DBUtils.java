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

package we.ytc.disbordissimo.server.internal.utils.db;

import we.ytc.disbordissimo.server.internal.utils.db.exceptions.NotBoundParamsException;

import java.sql.*;

/**
 * <h1>DBUtils static class </h1>
 *
 * This class offers the basic functions to manage a {@link Connection} to any SQL DB.<br>
 * <br>
 * Functions:<br>
 *  - connect(..)<br>
 *  - bindParams(..)<br>
 *  - startTransaction(..)<br>
 *  - commit(..)<br>
 *  - rollback(..)<br>
 *  - close(..)<br>
 */
public class DBUtils {
    private static final String PROTOCOL = "jdbc";
    private static final String SUBPROTOCOL = "mysql";

    private class Default {
        protected static final String HOST = "localhost";
        protected static final int PORT = 3306;
    }

    /**
     * Creates a {@link Connection} to the DB using the default host and port.
     *
     * @param user
     *        DB user
     * @param pwd
     *        DB password, if the password isn't set up in the DBMS, use ""
     * @param db
     *        DB name
     *
     * @return {@link Connection}
     * @throws SQLException
     */
    public static Connection connect(String user, String pwd, String db) throws SQLException {
        return connect(Default.HOST, user, pwd, db);
    }

    /**
     * Creates a {@link Connection} to the DB using the default port.
     *
     * @param host
     *        DB host
     * @param user
     *        DB user
     * @param pwd
     *        DB password, if the password isn't set up in the DBMS, use ""
     * @param db
     *        DB name
     *
     * @return {@link Connection}
     * @throws SQLException
     */
    public static Connection connect(String host, String user, String pwd, String db) throws SQLException {
        return connect(host, Default.PORT, user, pwd, db);
    }

    /**
     * Creates a {@link Connection} to the DB.
     *
     * @param host
     *        DB host
     * @param port
     *        DB port
     * @param user
     *        DB user
     * @param pwd
     *        DB password, if the password isn't set up in the DBMS, use ""
     * @param db
     *        DB name
     *
     * @return {@link Connection}
     * @throws SQLException
     */
    public static Connection connect(String host, int port, String user, String pwd, String db) throws SQLException {
        String url = PROTOCOL+":"+SUBPROTOCOL+"://"+host+":"+port+"/"+db+"?";

        return DriverManager.getConnection(url, user, pwd);
    }

    /**
     * Binds the query params. The {@code query} must be written according to prepared statement format.<br>
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
     *        The query
     * @param types
     *        String of query params types
     * @param params
     *        Actual query params
     *
     * @return The {@link PreparedStatement} query.
     */
    public static PreparedStatement bindParams(Connection con, String query, String types, Object... params) throws SQLException {
        if(types.length() != params.length) {
            throw new NotBoundParamsException();
        }

        PreparedStatement stmt = con.prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
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

    /**
     * Makes a transaction starts.
     *
     * @param con
     *        DB {@link Connection}
     */
    public static void startTransaction(Connection con) {
        try {
            con.createStatement().execute("START TRANSACTION;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commits the transaction.
     *
     * @param con
     *        DB {@link Connection}
     */
    public static void commit(Connection con) {
        try {
            con.createStatement().execute("COMMIT; ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rollbacks the transaction.
     *
     * @param con
     *        DB {@link Connection}
     */
    public static void rollback(Connection con){
        try {
            con.createStatement().execute("ROLLBACK; ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the DB {@link Connection}.
     *
     * @param con
     *        DB {@link Connection}
     */
    public static void close(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {}
    }
}
