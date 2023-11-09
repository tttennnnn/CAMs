package userpage.student;

import camp.Camp;
import camp.Faculty;
import com.opencsv.exceptions.CsvException;
import userpage.UserMainPage;
import util.AppUtil;
import util.CampList;
import util.exceptions.PageTerminatedException;

import java.io.IOException;
import java.util.Scanner;

public class StudentMainPage extends UserMainPage {
    public StudentMainPage(String userID, String email, String name, Faculty faculty) {
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
                    try {
                        openCampPage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("4"):
                    try {
                        openChangePasswordPage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("5"):
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
        System.out.println("\t3. To Camp page");
        System.out.println("\t4. To Enquiry page");
        System.out.println("\t5. Change password");
        System.out.println("\t6. Log out");
    }

    @Override
    protected void showProfile() {
        System.out.println("[Student Main Page]");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.println("Committee Status: " + getUserStatus());
    }

    private String getUserStatus() {
        CampList campList = null;
        try {
            campList = AppUtil.readCamps();
        } catch (IOException | CsvException e) {;
            System.exit(1);
        }

        for (Camp camp : campList.getCampSet()) {
            if (camp.getStatus(getUserID()).equals("Committee"))
                return camp.getName();
        }
        return "-";
    }
    private void openCampPage() throws PageTerminatedException, IOException, CsvException {
        StudentCampPage campPage = new StudentCampPage(getUserID(), getEmail(), getName(), getFaculty());
        campPage.runPage();
    }
}