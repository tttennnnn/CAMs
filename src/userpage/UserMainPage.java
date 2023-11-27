package userpage;

import app.CAMsApp;
import camp.meta.Faculty;
import util.AppUtil;
import util.exceptions.PageTerminatedException;

import java.util.List;

/**
 * abstract class that represent the User Main Page
 * @author Anqi
 * @date 2023/11/22
 */
public abstract class UserMainPage extends User implements ApplicationPage {
    /**
     * show Profile
     */// abstract
    protected abstract void showProfile();

    /**
     * constructor of UserMainPage
     * @param userID
     * @param email
     * @param name
     * @param faculty
     */// non-abstract
    public UserMainPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    /**
     * print the header, user information and operation options for user
     */
    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        showProfile();
        AppUtil.printSectionLine();
        showUsage();
    }

    /**
     * direct to the page to change password
     * @throws PageTerminatedException
     */
    protected void openChangePasswordPage() throws PageTerminatedException {
        ChangePasswordPage changePasswordPage = new ChangePasswordPage(getUserID(), getEmail(), getName(), getFaculty());
        changePasswordPage.runPage();
    }

    /**
     * sign up as first login
     * @return prompt
     */
    protected String getFirstLoginPrompt() {
        String prompt = "";
        List<String[]> lines = AppUtil.getDataFromCSV(CAMsApp.getKeyFile());
        for (int row = 1; row < lines.size(); row++) {
            String[] line = lines.get(row);
            if (getUserID().equals(line[0])) {
                if (line[1].isEmpty())
                    prompt = "*** Your password is default. Please change it. ***";
                break;
            }
        }
        return prompt;
    }
}
