package user;

import app.CAMsApp;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import utils.IOHandler;
import utils.InvalidPasswordException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public abstract class User {
    private final String userID, email, name, faculty;
    public User(String userID, String email, String name, String faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
    }
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getFaculty() { return faculty; }

    void resetPW() throws IOException, CsvException {
        showPasswordPolicy();

        Scanner sc = new Scanner(System.in);
        String newPW;
        while (true) {
            System.out.println("Enter new password: ");
            newPW = sc.nextLine();
            try {
                validatePassword(newPW);
                break;
            } catch (InvalidPasswordException e) {
                System.out.println(e.getMessage());
            }
        }

        String keyFile = CAMsApp.getKeyFileName();
        CSVReader csvReader = new CSVReaderBuilder(
            IOHandler.getFileReader(keyFile)
        ).build();

        List<String[]> lines = csvReader.readAll();
        csvReader.close();
        for (int row = 1; row < lines.size(); row++) {
            if (lines.get(row)[0].equals(userID)){
                lines.get(row)[1] = IOHandler.sha256(newPW);
                break;
            }
        }
        IOHandler.overwriteCSV(keyFile, lines);
    }
    private void showPasswordPolicy() {
        System.out.println("Password must:");
        System.out.println("\t- be of length 8 - 15 characters.");
        System.out.println("\t- not contain whitespace.");
        System.out.println();
    }
    private void validatePassword(String pw) throws InvalidPasswordException {
        if (pw.length() < 8 || pw.length() > 15)
            throw new InvalidPasswordException("Invalid length.");
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isWhitespace(pw.charAt(i)))
                throw new InvalidPasswordException("Invalid characters.");
        }
    }

    abstract void showMenu();
    public abstract void UserApp() throws IOException, CsvException;
}
