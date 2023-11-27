package util.exceptions;

/**
 * IllegalPasswordExceptions are thrown when password given as a new password violates its new password requirements.
 * @author Anqi
 * @date 2023/11/22
 */
public class IllegalPasswordException extends Exception {
    /**
     * constructor of IllegalPasswordException
     * @param s
     */
    public IllegalPasswordException(String s) {
        super(s);
    }
}
