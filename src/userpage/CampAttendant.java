package userpage;

import util.exceptions.InvalidUserInputException;

/**
 * interface of Camp Attendant including committees and staffs
 * @author Anqi
 * @date 2023/11/22
 */
public interface CampAttendant {
    /**
     * view enquiries of camp
     * @throws InvalidUserInputException
     */
    void viewEnquiries() throws InvalidUserInputException;

    /**
     * answer to enquiries of camp
     * @throws InvalidUserInputException
     */
    void answerEnquiry() throws InvalidUserInputException;

    /**
     * generate Report of camp
     * @throws InvalidUserInputException
     */
    void generateReport() throws InvalidUserInputException;
}
