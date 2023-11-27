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

/**
 * This class provides an app with user interface (UI) for the user to login.
 */
public class CAMsApp {
    /**
     * the main user page.
     */
    private static UserMainPage userMainPage;
    /**
     * this is a csv file with user id with their hashed keys.
     */
    private static final String KEY_FILE = "data/keys.csv";
    /**
     * this is a csv file with user's information.
     */
    private static final String USER_FILE = "data/users.csv";
    /**
     * this is a csv file with camp information.
     */
    private static final String CAMP_INFO_FILE = "data/camp/camps.csv";
    /**
     * this is a csv file with camp slots.
     */
    private static final String CAMP_SLOT_FILE = "data/camp/slots.csv";
    /**
     * this is a csv file with "StartDate","EndDate" and "RegistrationDeadline" of each camp.
     */
    private static final String CAMP_DATE_FILE = "data/camp/dates.csv";
    /**
     * this is a csv file including users' enquiries and reply from staff.
     */
    private static final String CAMP_ENQUIRY_FILE = "data/camp/enquiries.csv";
    /**
     * this is a csv file including users' suggestions.
     */
    private static final String CAMP_SUGGESTION_FILE = "data/camp/suggestions.csv";
    /**
     * the default password for users is "password".
     */
    private static final String DEFAULTKEY = AppUtil.sha256("password");

    /**
     * Get the File of user
     * @return the File of User
     */
    public static String getUserFile() { return USER_FILE; }
    /**
     * Get the Key File of the User.
     * @return Key File of the User
     */
    public static String getKeyFile() { return KEY_FILE; }
    /**
     * Get camp information file of the camp
     * @return camp information file of the camp
     */
    public static String getCampInfoFile() { return CAMP_INFO_FILE; }
    /**
     * Get the Date file of the camp.
     * @return Date file of the camp
     */
    public static String getCampDateFile() { return CAMP_DATE_FILE; }
    /**
     * Get the Slot file of the camp.
     * @return Slot file of the camp
     */
    public static String getCampSlotFile() { return CAMP_SLOT_FILE; }
    /**
     * Get the Enquiry file of the camp.
     * @return Enquiry file of the camp
     */
    public static String getCampEnquiryFile() { return CAMP_ENQUIRY_FILE; }
    /**
     * Get the Suggestion file of the camp.
     * @return Suggestion file of the camp
     */
    public static String getCampSuggestionFile() { return CAMP_SUGGESTION_FILE; }

    /**
     * Displays a login page.
     * @param args
     */
    public static void main(String[] args) {
        // login page
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

        // read in users information
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

    /**
     * check with user's id and passwords
     * @param id
     * @param key
     * @return boolean
     */
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
