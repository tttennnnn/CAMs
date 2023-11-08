package userpage.staff;

import camp.Faculty;
import userpage.UserMainPage;

public class StaffMainPage extends UserMainPage {
    public StaffMainPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void runPage() {
        printHeader();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View profile");
        System.out.println("\t3. To Camp menu");
        System.out.println("\t4. To Enquiry menu");
        System.out.println("\t5. Change password");
        System.out.println("\t6. Log out");
    }

    @Override
    public void showProfile() {
        System.out.println("[Staff Main Page]");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());

    }

}
