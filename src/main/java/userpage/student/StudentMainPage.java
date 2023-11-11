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
                        openMyEnquiries();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("5"):
                    // check committee status
                    if (getCommitteeStatus().equals("-")) {
                        System.out.println("You must be a camp committee to use this feature.");
                        break;
                    }

                    try {
                        openCampCommitteePage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("6"):
                    try {
                        openChangePasswordPage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                    break;
                case ("7"):
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
        System.out.println("\t4. View/Edit/Delete my enquiries"); // view/edit/delete my enquiry
        System.out.println("\t5. To Camp Committee Page");
        System.out.println("\t6. Change password");
        System.out.println("\t7. Log out");
    }

    @Override
    protected void showProfile() {
        try {
            System.out.println("[Student Main Page] " + getFirstLoginPrompt());
            System.out.println("Name: " + getName());
            System.out.println("Email: " + getEmail());
            System.out.println("Faculty: " + getFaculty());
            System.out.println("Committee Status: " + getCommitteeStatus());
        } catch (IOException | CsvException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private String getCommitteeStatus() throws IOException, CsvException {
        CampList campList = AppUtil.readCamps();
        for (Camp camp : campList.getCampSet()) {
            if (camp.getCampStatus(getUserID()).equals("Committee"))
                return camp.getName();
        }
        return "-";
    }
    public static String getCommitteeStatusForUser(String ID) throws IOException, CsvException {
        StudentMainPage page = new StudentMainPage(ID, null, null, null);
        return page.getCommitteeStatus();
    }
    private void openCampPage() throws PageTerminatedException, IOException, CsvException {
        StudentCampPage campPage = new StudentCampPage(getUserID(), getEmail(), getName(), getFaculty());
        campPage.runPage();
    }
    private void openMyEnquiries() throws PageTerminatedException {
        //view edit delete
        //for student
    }

    private void openCampCommitteePage() throws  PageTerminatedException, IOException, CsvException {
        CampCommitteePage campCommitteePage = new CampCommitteePage(getUserID(), getEmail(), getName(), getFaculty(), getCommitteeStatus());
        campCommitteePage.runPage();
    }
}