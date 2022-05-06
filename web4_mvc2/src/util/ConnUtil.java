package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public class ConnUtil {
    public static final String DRIVER = "com.mysql.jdbc.Driver" ;
    public static final String URL = "jdbc:mysql://localhost:3306/fruitdb?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public static final String USER = "root";
    public static final String PWD = "abc123";

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConn() {
        try {
            //1.加载驱动
            Class.forName(DRIVER);
            //2.通过驱动管理器获取连接对象
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static Connection createConn() {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = getConn();
            threadLocal.set(connection);
        }
        return threadLocal.get();
    }

    public static void closeConn() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            return;
        }
        if (!connection.isClosed()) {
            connection.close();
            threadLocal.set(null);
        }
    }
}
