package exception;

import myspringmvc.DispatcherServlet;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public class DispatcherServletException extends RuntimeException{
    public DispatcherServletException(String msg) {
        super(msg);
    }
}
