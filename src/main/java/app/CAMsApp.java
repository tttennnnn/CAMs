package app;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import user.Staff;
import user.Student;
import user.User;
import util.AppUtil;
import util.UserList;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class CAMsApp {
    private static User user;
    private static final String KEYFILE = "data/keys.csv";
    private static final String USERFILE = "data/users.csv";
    private static final String DEFAULTKEY = AppUtil.sha256("password");
    public static String getKeyFileName() { return KEYFILE; }

    public static void main(String[] args) throws IOException, CsvException {
        System.out.println("Starting CAMs...");

        // login page
        Scanner sc = new Scanner(System.in);
        String id, key;
        while (true) {
            System.out.print("Enter userID: ");
            id = sc.nextLine();
            System.out.print("Enter password: ");
            key = AppUtil.sha256(sc.nextLine());
            if (userLogin(id, key))
                break;
            else
                System.out.println("Invalid credentials");
        }
        System.out.println("Logged in.");
        System.out.println();

        // initialise users list
        UserList userList = readUsers();
        String[] userInfo;
        if (userList.hasStudent(id)) {
            userInfo = userList.getStudent(id);
            user = new Student(id, userInfo[0], userInfo[1], userInfo[2]);
        } else {
            userInfo = userList.getStaff(id);
            user = new Staff(id, userInfo[0], userInfo[1], userInfo[2]);
        }

        // to main page
        user.UserApp();
    }
    private static boolean userLogin(String id, String key) throws IOException, CsvException {
        CSVReader csvReader = AppUtil.getCSVReader(KEYFILE, 1);

        String[] line;
        while ((line = csvReader.readNext()) != null) {
            if (!id.equals(line[0]))
                continue;
            if (line[1].isEmpty())
                return key.equals(DEFAULTKEY);
            else
                return key.equals(line[1]);
        }
        csvReader.close();
        return false;
    }

    private static UserList readUsers() throws IOException, CsvException {
        CSVReader csvReader = AppUtil.getCSVReader(USERFILE, 1);

        List<String[]> lines = csvReader.readAll();
        csvReader.close();

        UserList userList = new UserList();
        for (String[] line : lines) {
            String id = line[0].split("@")[0];
            if (line[3].equals("1"))
                userList.putStudent(id, new String[]{line[0], line[1], line[2]});
            else
                userList.putStaff(id, new String[]{line[0], line[1], line[2]});
        }

        return userList;
    }
}
