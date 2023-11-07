package userpage.student;

import com.opencsv.exceptions.CsvException;
import userpage.UserMainPage;
import util.exceptions.PageTerminatedException;

import java.io.IOException;
import java.util.Scanner;

public class StudentMainPage extends UserMainPage {
    public StudentMainPage(String userID, String email, String name, String faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void runPage() throws PageTerminatedException, IOException, CsvException {
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
                    showProfile();
                    break;
                case ("3"):

                    break;
                case ("4"):
                    break;
                case ("5"):
                    try {
                        changePassword();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("6"):
                    throw new PageTerminatedException();
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View profile");
        System.out.println("\t3. To Camps menu");
        System.out.println("\t4. To Enquiry menu");
        System.out.println("\t5. Change password");
        System.out.println("\t6. Log out");
    }

    @Override
    public void showProfile() {
        System.out.println("[Student Main Page]");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.println("Committee Status: ");
    }
}