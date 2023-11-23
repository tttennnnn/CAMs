package userpage;

import util.exceptions.InvalidUserInputException;

public interface ViewerOfCampInfo {
    void showCamps() throws InvalidUserInputException;
    void showCampDetail() throws InvalidUserInputException;
}
