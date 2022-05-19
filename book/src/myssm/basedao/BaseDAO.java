package myssm.basedao;

import myssm.util.ConnUtil;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected Connection conn ;
    protected PreparedStatement psmt ;
    protected ResultSet rs ;

    //T的Class对象
    private Class entityClass ;


    public BaseDAO() {
        
        Type genericType = getClass().getGenericSuperclass();
        
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        
        Type actualType = actualTypeArguments[0];

        try {
            entityClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO 构造方法出错了，可能的原因是没有指定<>中的类型");
        }

    }

    protected Connection getConn(){
        return ConnUtil.getConn();
    }

    protected void close(ResultSet rs , PreparedStatement psmt , Connection conn){

    }

    private void setParams(PreparedStatement psmt , Object... params) throws SQLException {
        if(params!=null && params.length>0){
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i+1,params[i]);
            }
        }
    }

    protected int executeUpdate(String sql , Object... params) {
        boolean insertFlag = false ;
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");

        conn = getConn();
        try{
            if(insertFlag){
                psmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            }else {
                psmt = conn.prepareStatement(sql);
            }
            setParams(psmt,params);
            int count = psmt.executeUpdate() ;

            if(insertFlag){
                rs = psmt.getGeneratedKeys();
                if(rs.next()){
                    return ((Long)rs.getLong(1)).intValue();
                }
            }
            return 0 ;
        }catch (SQLException e){
            e.printStackTrace();
            throw new DAOException("BaseDAO executeUpdate出错了");
        }
    }


    private void setValue(Object obj ,  String property , Object propertyValue) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class clazz = obj.getClass();

      
        Field field = clazz.getDeclaredField(property);
        if(field!=null){


            String typeName = field.getType().getName();


            if(isMyType(typeName)){
               
                Class typeNameClass = Class.forName(typeName);
                Constructor constructor = typeNameClass.getDeclaredConstructor(Integer.class);
                propertyValue = constructor.newInstance(propertyValue);
            }
            field.setAccessible(true);
            field.set(obj,propertyValue);
        }
    }

    private static boolean isNotMyType(String typeName){
        return "java.lang.Integer".equals(typeName)
                || "java.lang.String".equals(typeName)
                || "java.util.Date".equals(typeName)
                || "java.sql.Date".equals(typeName)
                ||"java.lang.Double".equals(typeName);
    }

    private static boolean isMyType(String typeName){
        return !isNotMyType(typeName);
    }

  
    protected Object[] executeComplexQuery(String sql , Object... params){
        conn = getConn() ;
        try{
            psmt = conn.prepareStatement(sql);
            setParams(psmt,params);
            rs = psmt.executeQuery();

           

            ResultSetMetaData rsmd = rs.getMetaData();
          
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];
            
            if(rs.next()){
                for(int i = 0 ; i<columnCount;i++){
                    Object columnValue = rs.getObject(i+1);     //33    苹果      5
                    columnValueArr[i]=columnValue;
                }
                return columnValueArr ;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("BaseDAO executeComplexQuery出错了");
        }

        return null ;
    }

   
    protected T load(String sql , Object... params){
        conn = getConn() ;
        try{
            psmt = conn.prepareStatement(sql);
            setParams(psmt,params);
            rs = psmt.executeQuery();

         

            ResultSetMetaData rsmd = rs.getMetaData();
           
            int columnCount = rsmd.getColumnCount();
          
            if(rs.next()){
                T entity = (T)entityClass.newInstance();

                for(int i = 0 ; i<columnCount;i++){
                    String columnName = rsmd.getColumnLabel(i+1);            //fid   fname   price
                    Object columnValue = rs.getObject(i+1);     //33    苹果      5
                    setValue(entity,columnName,columnValue);
                }
                return entity ;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new DAOException("BaseDAO load出错了");
        }

        return null ;
    }

    
    protected List<T> executeQuery(String sql , Object... params){
        List<T> list = new ArrayList<>();
        conn = getConn() ;
        try{
            psmt = conn.prepareStatement(sql);
            setParams(psmt,params);
            rs = psmt.executeQuery();

           
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int columnCount = rsmd.getColumnCount();
           
            while(rs.next()){
                T entity = (T)entityClass.newInstance();

                for(int i = 0 ; i<columnCount;i++){
                    String columnName = rsmd.getColumnLabel(i+1);           
                    Object columnValue = rs.getObject(i+1);   
                    setValue(entity,columnName,columnValue);
                }
                list.add(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new DAOException("BaseDAO executeQuery出错了");
        }
        return list ;
    }
}
