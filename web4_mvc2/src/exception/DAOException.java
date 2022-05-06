package exception;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public class DAOException extends RuntimeException{
    public DAOException(String msg){
        super(msg);
    }
}
