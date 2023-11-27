package userpage;

import util.exceptions.InvalidUserInputException;

/**
 * interface of camp information viewer
 * @author Anqi
 * @date 2023/11/22
 */
public interface ViewerOfCampInfo {
    /**
     * show list of Camps with different sorting option
     * @throws InvalidUserInputException
     */
    void showCamps() throws InvalidUserInputException;

    /**
     * show the detail of certain camp
     * @throws InvalidUserInputException
     */
    void showCampDetail() throws InvalidUserInputException;
}
