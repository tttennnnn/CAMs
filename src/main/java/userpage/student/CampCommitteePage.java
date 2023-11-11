package userpage.student;

import camp.Faculty;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;

import java.util.Scanner;

public class CampCommitteePage extends User implements ApplicationPage {
    private String myCamp;
    public CampCommitteePage(String userID, String email, String name, Faculty faculty, String myCamp) {
        super(userID, email, name, faculty);
        this.myCamp = myCamp;
    }

    @Override
    public void runPage() {
        printHeader();

        Scanner sc = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("[Input]: ");
            input = sc.nextLine();
            switch (input) {

            }
        }
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Committee Menu]");
        System.out.println("My Camp: " + myCamp);
        showUsage();
        AppUtil.printSectionLine();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View my suggestions");
        System.out.println("\t3. Edit/Delete suggestions");
        System.out.println("\t4. Submit a suggestion");
        System.out.println("\t5. View/Answer enquiries"); // view/answer to all enquiries of my camp
        System.out.println("\t6. Generate report");
        System.out.println("\t7. Return to previous page");
    }
}
