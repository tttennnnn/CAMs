package userpage.student;

import camp.Camp;
import camp.Faculty;
import userpage.UserMainPage;
import util.AppUtil;
import util.CampList;
import util.exceptions.PageTerminatedException;

import java.util.Scanner;

public class StudentMainPage extends UserMainPage {
    public StudentMainPage(String userID, String email, String name, Faculty faculty) {
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
                case ("5"):
                    try {
                        openChangePasswordPage();
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
        System.out.println("\t3. To Camp page");
        System.out.println("\t4. To Camp Committee Page");
        System.out.println("\t5. Change password");
        System.out.println("\t6. Log out");
    }

    @Override
    protected void showProfile() {
        System.out.println("[Student Main Page] " + getFirstLoginPrompt());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.print("Camp Committee Status: " + getCommitteeStatus());
        if (!getCommitteeStatus().equals("-"))
            System.out.print(" -> " + getCommitteePoint() + " point(s)");
        System.out.println();
    }

    private String getCommitteeStatus() {
        CampList campList = AppUtil.readCamps();
        for (Camp camp : campList.getSortedCampSet()) {
            if (camp.getCampStatus(getUserID()).equals("Committee"))
                return camp.getName();
        }
        return "-";
    }
    private int getCommitteePoint() {
        CampList campList = AppUtil.readCamps();
        for (Camp camp : campList.getSortedCampSet()) {
            if (camp.getCampStatus(getUserID()).equals("Committee"))
                return camp.getCommitteePoint(getUserID());
        }
        return 0;
    }
    public static String getCommitteeStatusForUser(String ID) {
        StudentMainPage page = new StudentMainPage(ID, null, null, null);
        return page.getCommitteeStatus();
    }
    private void openCampPage() throws PageTerminatedException {
        StudentCampPage campPage = new StudentCampPage(getUserID(), getEmail(), getName(), getFaculty());
        campPage.runPage();
    }
    private void openCampCommitteePage() throws  PageTerminatedException {
        CampCommitteePage campCommitteePage = new CampCommitteePage(getUserID(), getEmail(), getName(), getFaculty(), getCommitteeStatus());
        campCommitteePage.runPage();
    }
}