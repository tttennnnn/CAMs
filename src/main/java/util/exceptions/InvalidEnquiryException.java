package util.exceptions;

public class InvalidEnquiryException extends Exception {
    public InvalidEnquiryException() { super("Invalid index."); }
    public InvalidEnquiryException(String s) { super(s); }
}
