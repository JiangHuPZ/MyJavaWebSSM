package trans;

import util.ConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public class TransactionManager {

    public static void beginTrans() throws SQLException {

        ConnUtil.createConn().setAutoCommit(false);
    }

    public static void commit() throws SQLException {

        Connection connection = ConnUtil.createConn();
        connection.commit();
        ConnUtil.closeConn();
    }

    public static void rollback() throws SQLException {

        Connection connection = ConnUtil.createConn();
        connection.rollback();
        ConnUtil.closeConn();
    }

}
