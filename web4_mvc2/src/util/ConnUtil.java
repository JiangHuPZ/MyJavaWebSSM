package util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public class ConnUtil {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConn() {
        try {
            InputStream is = ConnUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);

            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            return dataSource.getConnection();
            
        } catch (Exception e) {
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
