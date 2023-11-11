package userpage;

import app.CAMsApp;
import camp.Faculty;
import com.opencsv.CSVReader;
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

    protected String getFirstLoginPrompt() throws IOException, CsvException {
        String prompt = "";
        CSVReader csvReader = AppUtil.getCSVReader(CAMsApp.getKeyFile(), 1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            if (getUserID().equals(line[0])) {
                if (line[1].isEmpty())
                    prompt = "*** Your password is default. Please change it. ***";
                break;
            }

        }
        return prompt;
    }
}
