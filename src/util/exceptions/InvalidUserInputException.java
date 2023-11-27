package util.exceptions;

/**
 * InvalidUserInputExceptions are thrown when users give invalid input during application runtime.
 * @author Anqi
 * @date 2023/11/22
 */
public class InvalidUserInputException extends Exception {
    public InvalidUserInputException(String s) { super(s); }
}
