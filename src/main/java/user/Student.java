package user;

import java.util.Scanner;

public class Student extends User {
    public Student(String userID, String email, String name, String faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    void showMenu() {
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.println("___Committee Status___");
        /* show committee status */

    }

    @Override
    public void UserApp() {
        Scanner sc = new Scanner(System.in);

        showMenu();


    }
}
