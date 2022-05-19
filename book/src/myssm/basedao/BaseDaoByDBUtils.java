package myssm.basedao;

import myssm.util.ConnUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Jayce Xu
 * @create 2022519
 */
public abstract class BaseDaoByDBUtils {
    private QueryRunner queryRunner = new QueryRunner();

    public int update(String sql,Object... args){
        Connection conn = ConnUtil.getConn();
        try {
            return queryRunner.update(conn,sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnUtil.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public <T> T queryForOne(Class<T> type,String sql,Object... args){
        Connection conn = ConnUtil.getConn();
        try {
            return queryRunner.query(conn,sql,new BeanHandler<T>(type),args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnUtil.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public <T> List<T> queryForList(Class<T> type, String sql, Object... args){
        Connection conn = ConnUtil.getConn();
        try {
            return queryRunner.query(conn,sql,new BeanListHandler<T>(type),args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnUtil.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object queryForSingleValue(String sql, Object... args){
        Connection conn = ConnUtil.getConn();
        try {
            return queryRunner.query(conn,sql,new ScalarHandler(),args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnUtil.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
