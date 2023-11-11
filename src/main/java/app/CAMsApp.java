package app;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import userpage.User;
import userpage.staff.StaffMainPage;
import userpage.student.StudentMainPage;
import userpage.UserMainPage;
import util.AppUtil;
import util.UserList;
import util.exceptions.PageTerminatedException;

import java.io.*;
import java.util.Scanner;

public class CAMsApp {
    private static UserMainPage userMainPage;
    private static final String KEY_FILE = "data/keys.csv";
    private static final String USER_FILE = "data/users.csv";
    private static final String CAMP_INFO_FILE = "data/camp/camps.csv";
    private static final String CAMP_SLOT_FILE = "data/camp/slots.csv";
    private static final String CAMP_DATE_FILE = "data/camp/dates.csv";
    private static final String CAMP_ENQUIRY_FILE = "data/camp/enquiries.csv";
    private static final String CAMP_SUGGESTION_FILE = "data/camp/suggestions.csv";
    private static final String DEFAULTKEY = AppUtil.sha256("password");

    public static String getUserFile() { return USER_FILE; }
    public static String getKeyFile() { return KEY_FILE; }
    public static String getCampInfoFile() { return CAMP_INFO_FILE; }
    public static String getCampDateFile() { return CAMP_DATE_FILE; }
    public static String getCampSlotFile() { return CAMP_SLOT_FILE; }
    public static String getCampEnquiryFile() { return CAMP_ENQUIRY_FILE; }
    public static String getCampSuggestionFile() { return CAMP_SUGGESTION_FILE; }

    public static void main(String[] args) throws IOException, CsvException {
        System.out.println("========== CAMs Login Page ==========");

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

        // read user list
        UserList userList = AppUtil.readUsers();
        User thisUser;
        if (userList.hasStudent(id)) {
            thisUser = userList.getStudent(id);
            userMainPage = new StudentMainPage(thisUser.getUserID(), thisUser.getEmail(), thisUser.getName(), thisUser.getFaculty());
        } else if (userList.hasStaff(id)){
            thisUser = userList.getStaff(id);
            userMainPage = new StaffMainPage(thisUser.getUserID(), thisUser.getEmail(), thisUser.getName(), thisUser.getFaculty());
        }

        // to main page
        try {
            userMainPage.runPage();
        } catch (PageTerminatedException e) {
            main(args);
        }

    }
    private static boolean userLogin(String id, String key) throws IOException, CsvException {
        CSVReader csvReader = AppUtil.getCSVReader(KEY_FILE, 1);

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
}
