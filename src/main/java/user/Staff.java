package user;

import com.opencsv.exceptions.CsvException;
import util.exceptions.IllegalCommandException;

import java.io.IOException;

public class Staff extends User {
    public Staff(String userID, String email, String name, String faculty) {
        super(userID, email, name, faculty);

        // set commands

    }

    @Override
    public void UserApp() throws IOException, CsvException {

    }

    @Override
    void parseCommand(String input) throws IOException, CsvException, IllegalCommandException {

    }

    @Override
    String[] validateCommand(String input) throws IllegalCommandException {
        return new String[0];
    }

    @Override
    void showProfile() {

    }

    @Override
    void showHelp() {

    }


}
