package util.exceptions;

public class InvalidSuggestionException extends Exception {
    public InvalidSuggestionException() { super("Invalid index."); }
    public InvalidSuggestionException(String s) { super(s); }
}
