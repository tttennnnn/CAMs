package app;

import userpage.User;
import userpage.staff.StaffMainPage;
import userpage.student.StudentMainPage;
import userpage.UserMainPage;
import util.AppUtil;
import util.UserList;
import util.exceptions.PageTerminatedException;

import java.util.List;
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

    public static void main(String[] args) {
        AppUtil.printSectionLine();
        System.out.println("  .oooooo.         .o.       ooo        ooooo          \n" +
                           " d8P'  `Y8b       .888.      `88.       .888'          \n" +
                           "888              .8\"888.      888b     d'888   .oooo.o \n" +
                           "888             .8' `888.     8 Y88. .P  888  d88(  \"8 \n" +
                           "888            .88ooo8888.    8  `888'   888  `\"Y88b.  \n" +
                           "`88b    ooo   .8'     `888.   8    Y     888  o.  )88b \n" +
                           " `Y8bood8P'  o88o     o8888o o8o        o888o 8\"\"888P'");
        AppUtil.printSectionLine();
        System.out.println("Welcome to CAMs Login Page!");

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
    private static boolean userLogin(String id, String key) {
        List<String[]> lines = AppUtil.getDataFromCSV(KEY_FILE);
        for (int row = 1; row < lines.size(); row++) {
            String[] line = lines.get(row);
            if (!id.equals(line[0]))
                continue;
            if (line[1].isEmpty())
                return key.equals(DEFAULTKEY);
            else
                return key.equals(line[1]);
        }
        return false;
    }
}
