package util.exceptions;

public class InvalidCampException extends Exception {
    public InvalidCampException() { super("Invalid camp."); }
    public InvalidCampException(String s) { super(s); }
}