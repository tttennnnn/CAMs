package user;

import com.opencsv.exceptions.CsvException;
import util.AppUtil;
import util.exceptions.IllegalCommandException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Student extends User {
    @Override
    void showHelp() {
        System.out.println("Usage:");
        System.out.println("\tprofile");
        System.out.println("\tpw [-h] [-c PASSWORD]");
        System.out.println("\tcamps [-v] [-r NAME ROLE] [-w NAME]");
        System.out.println("\tquery [-v] [-s NAME MESSAGE]");
    }
    public Student(String userID, String email, String name, String faculty) {
        super(userID, email, name, faculty);
        // set commands
        commands.put("help", new HashMap<>());
        commands.put("profile", new HashMap<>());

        commands.put("pw", new HashMap<>());
        commands.get("pw").put("-h", 0);
        commands.get("pw").put("-c", 1);

        commands.put("camps", new HashMap<>());
        commands.get("camps").put("-v", 0);
        commands.get("camps").put("-r", 2);
        commands.get("camps").put("-w", 1);

        commands.put("query", new HashMap<>());
        commands.get("query").put("-v", 0);
        commands.get("query").put("-s", 2);
    }
    @Override
    public void UserApp() throws IOException, CsvException {
        AppUtil.printHeader();
        showProfile();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String input;
            do {
                System.out.print(getEmail() + "$ ");
            } while ((input = sc.nextLine()).isEmpty());

            try {
                parseCommand(input);
            } catch (IllegalCommandException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
    }
    @Override
    void parseCommand(String input) throws IOException, CsvException, IllegalCommandException {
        String[] args = validateCommand(input);

        switch (args[0]) {
            case ("help"):
                showHelp();
                break;
            case ("profile"):
                showProfile();
                break;
            case ("pw"):
                if (args[1].equals("-h"))
                    showPasswordPolicy();
                else if (args[1].equals("-c"))
                    changePassword(args[2]);
                break;
            case ("camps"):
                break;
            case ("query"):
        }
    }
    @Override
    String[] validateCommand(String input) throws IllegalCommandException {
        String[] args = input.split(" ");

        for (String arg : args) {
            if (arg.isBlank())
                throw new IllegalCommandException();
        }

        if (!commands.containsKey(args[0]))
            throw new IllegalCommandException("Invalid command: " + args[0]);

        if (args[0].equals("help") || args[0].equals("profile")) {
            if (args.length != 1)
                throw new IllegalCommandException();
        } else {
            if (args.length < 2)
                throw new IllegalCommandException();
            if (!commands.get(args[0]).containsKey(args[1]))
                throw new IllegalCommandException("Invalid argument: " + args[1] + " for command: " + args[0]);
            if (args.length == 2 + commands.get(args[0]).get(args[1]))
                throw new IllegalCommandException();
        }

        return args;
    }
    @Override
    void showProfile() {
        System.out.println("---Student Profile---");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Faculty: " + getFaculty());
        System.out.println("Committee Status: ");
        /* show committee status */
        System.out.println("");
    }
}