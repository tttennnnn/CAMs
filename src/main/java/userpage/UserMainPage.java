package userpage;

import com.opencsv.exceptions.CsvException;
import util.AppUtil;
import util.exceptions.PageTerminatedException;

import java.io.IOException;

public abstract class UserMainPage extends User implements ApplicationPage {
    // abstract members
    protected abstract void showProfile();

    // class members
    public UserMainPage(String userID, String email, String name, String faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        showProfile();
        AppUtil.printSectionLine();
        showUsage();
    }

    protected void changePassword() throws PageTerminatedException, IOException, CsvException {
        ChangePasswordPage changePasswordPage = new ChangePasswordPage(getUserID(), getEmail(), getName(), getFaculty());
        changePasswordPage.runPage();
    }
}
