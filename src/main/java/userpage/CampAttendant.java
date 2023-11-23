package userpage;

import util.exceptions.InvalidUserInputException;

public interface CampAttendant {
    void viewEnquiries() throws InvalidUserInputException;
    void answerEnquiry() throws InvalidUserInputException;
    void generateReport() throws InvalidUserInputException;
}
