package user;

import app.CAMsApp;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import util.AppUtil;
import util.exceptions.IllegalCommandException;
import util.exceptions.IllegalPasswordException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public abstract class User {
    // abstract members
    public abstract void UserApp() throws IOException, CsvException;
    abstract void parseCommand(String input) throws IOException, CsvException, IllegalCommandException;
    abstract String[] validateCommand(String input) throws IllegalCommandException;
    abstract void showProfile();
    abstract void showHelp();

    // class members
    final HashMap<String, HashMap<String, Integer>> commands;
    private final String userID, email, name, faculty;
    public User(String userID, String email, String name, String faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
        commands = new HashMap<>();
    }
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getFaculty() { return faculty; }

    void changePassword(String newPW) throws IOException, CsvException {
        try {
            validatePassword(newPW);
        } catch (IllegalPasswordException e) {
            System.out.println(e.getMessage());
            return;
        }

        String keyFile = CAMsApp.getKeyFileName();
        CSVReader csvReader = AppUtil.getCSVReader(keyFile);

        List<String[]> lines = csvReader.readAll();
        csvReader.close();
        for (int row = 1; row < lines.size(); row++) {
            if (lines.get(row)[0].equals(userID)){
                lines.get(row)[1] = AppUtil.sha256(newPW);
                break;
            }
        }
        AppUtil.overwriteCSV(keyFile, lines);
    }
    void showPasswordPolicy() {
        System.out.println("Password must:");
        System.out.println("\t- be of length 8 - 15 characters.");
        System.out.println("\t- not contain whitespace.");
        System.out.println();
    }
    void validatePassword(String pw) throws IllegalPasswordException {
        if (pw.length() < 8 || pw.length() > 15)
            throw new IllegalPasswordException("Invalid length.");
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isWhitespace(pw.charAt(i)))
                throw new IllegalPasswordException("Invalid characters.");
        }
    }
}
