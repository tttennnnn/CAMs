package userpage;

import camp.Faculty;
import com.opencsv.exceptions.CsvException;
import util.AppUtil;
import util.exceptions.PageTerminatedException;

import java.io.IOException;

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

    protected void openChangePasswordPage() throws PageTerminatedException, IOException, CsvException {
        ChangePasswordPage changePasswordPage = new ChangePasswordPage(getUserID(), getEmail(), getName(), getFaculty());
        changePasswordPage.runPage();
    }
}
