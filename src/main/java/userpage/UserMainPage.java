package userpage;

import app.CAMsApp;
import camp.Faculty;
import util.AppUtil;
import util.exceptions.PageTerminatedException;

import java.util.List;

public abstract class UserMainPage extends User implements ApplicationPage {
    // abstract
    protected abstract void showProfile();

    // non-abstract
    public UserMainPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        showProfile();
        AppUtil.printSectionLine();
        showUsage();
    }

    protected void openChangePasswordPage() throws PageTerminatedException {
        ChangePasswordPage changePasswordPage = new ChangePasswordPage(getUserID(), getEmail(), getName(), getFaculty());
        changePasswordPage.runPage();
    }

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
