package util.exceptions;

public class IllegalCommandException extends Exception {
    public IllegalCommandException() {
        super("Invalid command.");
    }
    public IllegalCommandException(String s) {
        super(s);
    }
}
