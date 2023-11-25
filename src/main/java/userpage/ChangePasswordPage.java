package userpage;

import app.CAMsApp;
import camp.meta.Faculty;
import util.AppUtil;
import util.exceptions.IllegalPasswordException;
import util.exceptions.InvalidUserInputException;
import util.exceptions.PageTerminatedException;

import java.util.List;
import java.util.Scanner;

public class ChangePasswordPage extends User implements ApplicationPage {
    public ChangePasswordPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void runPage() throws PageTerminatedException {
        printHeader();

        Scanner sc = new Scanner(System.in);
        String input;
         while (true) {
            System.out.print("[Input]: ");
            input = sc.nextLine();
            switch (input) {
                case ("1"):
                    showUsage();
                    break;
                case ("2"):
                    showPasswordPolicy();
                    break;
                case ("3"):
                    try {
                        changePassword();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("4"):
                    throw new PageTerminatedException();
                default:
                    System.out.println("Invalid input.");
            }
         }
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Change Password]");
        showUsage();
        AppUtil.printSectionLine();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View password policy");
        System.out.println("\t3. Change password");
        System.out.println("\t4. Return to previous page");
    }
    private void showPasswordPolicy() {
        System.out.println("Password must:");
        System.out.println("\t- be of length 8 - 15 characters.");
        System.out.println("\t- not contain whitespace.");
    }
    private void changePassword() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new password: ");
        String newPW = sc.nextLine();

        try {
            validatePassword(newPW);
        } catch (IllegalPasswordException e) {
            throw new InvalidUserInputException(e.getMessage());
        }

        String keyFile = CAMsApp.getKeyFile();
        List<String[]> lines = AppUtil.getDataFromCSV(keyFile);
        for (int row = 1; row < lines.size(); row++) {
            if (lines.get(row)[0].equals(getUserID())){
                lines.get(row)[1] = AppUtil.sha256(newPW);
                break;
            }
        }
        AppUtil.overwriteCSV(keyFile, lines);
        System.out.println("Password changed.");
    }
    private void validatePassword(String pw) throws IllegalPasswordException {
        if (pw.length() < 8 || pw.length() > 15)
            throw new IllegalPasswordException("Invalid length.");
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isWhitespace(pw.charAt(i)))
                throw new IllegalPasswordException("Invalid characters.");
        }
    }
}
