package util.exceptions;

/**
 * PageTerminatedExceptions are thrown when users prompt to return to previous page or logout, resulting in the termination of the current page.
 * @author Anqi
 * @date 2023/11/22
 */
public class PageTerminatedException extends Exception {
    public PageTerminatedException() { super(); }
}
