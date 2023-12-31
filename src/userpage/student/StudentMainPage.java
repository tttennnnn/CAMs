package userpage.student;

import camp.meta.Faculty;
import camp.slots.CampSlotsManager;
import userpage.UserMainPage;
import util.exceptions.PageTerminatedException;

import java.util.Scanner;

/**
 * class that represent the user page of student
 * @author Anqi
 * @date 2023/11/22
 */
public class StudentMainPage extends UserMainPage {
    /**
     * constructor of StudentMainPage
     * @param userID
     * @param email
     * @param name
     * @param faculty
     */
    public StudentMainPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    /**
     * the UI for student
     * @throws PageTerminatedException
     */
    @Override
    public void runPage() throws PageTerminatedException {
        printHeader();
        Scanner sc = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("[Input]: ");
            input = sc.nextLine();
            switch (input) {
                case ("1") -> showUsage();
                case ("2") -> showProfile();
                case ("3") -> {
                    try {
                        openCampPage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                }
                case ("4") -> {
                    // check committee status
                    if (CampSlotsManager.getUserCommitteeStatus(getUserID()).equals("-")) {
                        System.out.println("You must be a camp committee to use this feature.");
                        break;
                    }
                    try {
                        openCampCommitteePage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                }
                case ("5") -> {
                    try {
                        openChangePasswordPage();
                    } catch (PageTerminatedException e) {
                        runPage();
                    }
                }
                case ("6") -> {
                    System.out.println("You have been logged out.");
                    throw new PageTerminatedException();
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    /**
     * print down the operation options for user
     */
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

    /**
     * show the profile of user
     */
    @Override
    protected void showProfile() {
        System.out.println("[Student Main Menu] " + getFirstLoginPrompt());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.print("Camp Committee Status: " + CampSlotsManager.getUserCommitteeStatus(getUserID()));
        if (!CampSlotsManager.getUserCommitteeStatus(getUserID()).equals("-")) {
            System.out.print(" -> " + CampSlotsManager.getUserCommitteePoint(getUserID()) + " point(s)");
        }
        System.out.println();
    }

    /**
     * direct to the student Camp Page
     * @throws PageTerminatedException
     */
    private void openCampPage() throws PageTerminatedException {
        StudentCampPage campPage = new StudentCampPage(getUserID(), getEmail(), getName(), getFaculty());
        campPage.runPage();
    }

    /**
     * direct to the camp committee Page if user have authority
     * @throws PageTerminatedException
     */
    private void openCampCommitteePage() throws  PageTerminatedException {
        CampCommitteePage campCommitteePage = new CampCommitteePage(
            getUserID(), getEmail(), getName(), getFaculty(), CampSlotsManager.getUserCommitteeStatus(getUserID())
        );
        campCommitteePage.runPage();
    }
}