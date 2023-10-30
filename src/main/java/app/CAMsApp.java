package app;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import user.Staff;
import user.Student;
import user.User;
import utils.InputHandler;
import utils.UserList;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class CAMsApp {
    private static User user;
    private static final String defaultKey = InputHandler.sha256("password");
    public static void main(String[] args) throws IOException, CsvException {
        String keyFile = "data/key.csv", userFile = "data/users.csv",
               id, key;
        Scanner sc = new Scanner(System.in);


        System.out.println("Starting CAMs...");
        FileReader keyStream = InputHandler.getFile(keyFile);
        while (true) {
            System.out.print("Enter userID: ");
            id = sc.nextLine();
            System.out.print("Enter password: ");
            key = InputHandler.sha256(sc.nextLine());
            if (userLogin(keyStream, id, key))
                break;
            else
                System.out.println("Invalid userID or password.");
        }
        System.out.println("Logged in.");
        System.out.println();

        // initialise users list
        UserList userList = readUsers(InputHandler.getFile(userFile));
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

    private static boolean userLogin(FileReader keyReader, String id, String key) throws IOException, CsvException {
        CSVReader csvReader = new CSVReaderBuilder(keyReader)
                              .withSkipLines(1).build();
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            if (!id.equals(line[0]))
                continue;
            if (line[1].isEmpty())
                return key.equals(defaultKey);
            else
                return key.equals(InputHandler.sha256(key));
        }
        csvReader.close();
        return false;
    }

    private static UserList readUsers(FileReader userReader) throws IOException, CsvException {
        CSVReader csvReader = new CSVReaderBuilder(userReader)
                              .withSkipLines(1).build();

        List<String[]> userdata = csvReader.readAll();
        csvReader.close();

        UserList userList = new UserList();
        for (String[] lines : userdata) {
            String id = lines[0].split("@")[0];
            if (lines[3].equals("1"))
                userList.putStudent(id, new String[]{lines[0], lines[1], lines[2]});
            else
                userList.putStaff(id, new String[]{lines[0], lines[1], lines[2]});
        }

        return userList;
    }
}
