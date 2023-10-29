package app;

import user.Staff;
import user.Student;
import user.User;

import java.util.Scanner;

public class CAMsApp {
    private static User user;
    public static void main(String[] args) {
        System.out.println("Starting CAMs...");
        String FILEPATH = "src/main/resources/",
               keyFile = "key.csv",
               userFile = "users.csv",
               id, key;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter userID: ");
            id = sc.nextLine();
            System.out.print("Enter password: ");
            key = utils.sha256(sc.nextLine());
            if (utils.userLogin(FILEPATH + keyFile, id, key))
                break;
            else
                System.out.println("Invalid userID or password.");
        }
        System.out.println("Logged in.");
        System.out.println();

        // initialise users list
        UserList userList = utils.readUsers(FILEPATH + userFile);
        String[] userInfo;
        if (userList.hasStudent(id)) {
            userInfo = userList.getStudent(id);
            user = new Student(id, userInfo[0], userInfo[1], userInfo[2]);
        } else {
            userInfo = userList.getStaff(id);
            user = new Staff(id, userInfo[0], userInfo[1], userInfo[2]);
        }

        user.UserApp();
    }

}
