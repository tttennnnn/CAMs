package userpage.student;

import app.CAMsApp;
import camp.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;
import util.CampList;
import util.exceptions.InvalidCampException;
import util.exceptions.PageTerminatedException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class StudentCampPage extends User implements ApplicationPage {

    public StudentCampPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void runPage() throws PageTerminatedException, IOException, CsvException {
        printHeader();

        Scanner sc = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("[Input]: ");
            input = sc.nextLine();
            switch (input) {
                case ("1"):
                    showUsage();
                    break;
                case ("2"):
                    showCamps();
                    break;
                case ("3"):
                    try {
                        showCampDetail();
                    } catch (InvalidCampException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("4"):
                    try {
                        register();
                    } catch (InvalidCampException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("5"):
                    withdraw();
                    break;
                case ("6"):
                    throw new PageTerminatedException();
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Menu]");
        showUsage();
        AppUtil.printSectionLine();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View camps");
        System.out.println("\t3. View additional camp info");
        System.out.println("\t4. Register for a camp");
        System.out.println("\t5. Withdraw from a camp");
        System.out.println("\t6. Return to previous page");
    }

    private void showCamps() throws IOException, CsvException {
        // choose filter
        System.out.print("Choose filter " + Arrays.asList(CampViewFilter.values()) + ": ");
        Scanner sc = new Scanner(System.in);
        CampViewFilter viewFilter;
        try {
            viewFilter = CampViewFilter.valueOf(sc.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid filter.");
            return;
        }

        // read in camps
        CampList campList = getVisibleCampList();

        TreeMap<Object, Camp> campTreeMap = new TreeMap<>();
        for (Camp camp : campList.getCampSet()) {
            switch (viewFilter) {
                case Name:
                    campTreeMap.put(camp.getName(), camp);
                    break;
                case Location:
                    campTreeMap.put(camp.getLocation(), camp);
                    break;
                case TotalVacancy:
                    campTreeMap.put(camp.getTotalVacancy(), camp);
                    break;
                case CommitteeVacancy:
                    campTreeMap.put(camp.getCommitteeVacancy(), camp);
                    break;
            }
        }

        System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
            "Name", "TotalSlot", "CommitteeSlot", "Location", "Faculty", "Status"
        );
        for (Object key : campTreeMap.keySet()) {
            Camp camp = campTreeMap.get(key);
            System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
                camp.getName(),
                camp.getTotalSlotAsString(),
                camp.getCommitteeSlotAsString(),
                camp.getLocation(),
                camp.getFaculty(),
                camp.getStatus(getUserID())
            );
        }
    }

    private void showCampDetail() throws InvalidCampException, IOException, CsvException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidCampException();

        Camp camp = campList.getCamp(campName);

        System.out.printf("%-10s | %-15s | %-15s | %-20s | Description\n",
            "Name", "StartDate", "EndDate", "RegistrationDeadline"
        );
        System.out.printf("%-10s | %-15s | %-15s | %-20s | %s\n",
            camp.getName(),
            CampDates.getDateAsString(camp.getCampDates().getStartDate()),
            CampDates.getDateAsString(camp.getCampDates().getEndDate()),
            CampDates.getDateAsString(camp.getCampDates().getRegistrationDeadline()),
            camp.getDescription()
        );
    }

    private void register() throws InvalidCampException, IOException, CsvException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidCampException();

        Camp camp = campList.getCamp(campName);
        // check registration deadline
        if (LocalDate.now().isAfter(camp.getCampDates().getRegistrationDeadline()))
            throw new InvalidCampException("It is past the registration deadline.");
        // check withdrawals
        if (camp.hasWithdrawn(getUserID()))
            throw new InvalidCampException("You have already withdrawn from this camp.");
        // check time clashes
        for (Camp registeredCamp : getRegisteredCampList().getCampSet()) {
            if (registeredCamp.hasTimeClash(camp))
                throw new InvalidCampException("Time clashes with " + registeredCamp.getName());
        }

        System.out.print("Enter role [Attendee, Committee]: ");
        String role = sc.nextLine();
        switch (role) {
            case ("Attendee"):
                if (camp.getTotalVacancy() == 0)
                    throw new InvalidCampException("Camp is full.");
                camp.addStudent(getUserID());
                break;
            case ("Committee"):
                if (camp.getTotalVacancy() == 0 || camp.getCommitteeVacancy() == 0)
                    throw new InvalidCampException("Camp is full.");
                camp.addCommittee(getUserID());
                break;
            default:
                throw new InvalidCampException("Invalid role.");
        }

        // update csv
        CSVReader campSlotReader = AppUtil.getCSVReader(CAMsApp.getCampSlotFile(), 1);
        List<String[]> slotLines = campSlotReader.readAll();
        campSlotReader.close();
        for (int row = 1; row < slotLines.size(); row++) {
            if (slotLines.get(row)[0].equals(campName)) {
                slotLines.get(row)[2] = CampSlot.getAttendeeListAsString(camp.getCampSlot().getStudents());
                slotLines.get(row)[3] = CampSlot.getAttendeeListAsString(camp.getCampSlot().getCommittees());
                break;
            }

        }
        AppUtil.overwriteCSV(CAMsApp.getCampSlotFile(), slotLines);
        System.out.println("Registered as " + role);
    }

    private void withdraw() {

    }

    private CampList getVisibleCampList() throws IOException, CsvException {
        CampList campList = AppUtil.readCamps();
        CampList visibleCampList = new CampList();
        for (Camp camp : campList.getCampSet()) {
            if (!camp.getVisibility())
                continue;
            if (camp.getFaculty() != Faculty.NTU && camp.getFaculty() != getFaculty())
                continue;
            visibleCampList.putCamp(camp.getName(), camp);
        }
        return visibleCampList;
    }
    private CampList getRegisteredCampList() throws IOException, CsvException {
        CampList campList = AppUtil.readCamps();
        CampList registeredCampList = new CampList();
        for (Camp camp : campList.getCampSet()) {
            if (!camp.getCampSlot().getStudents().contains(getUserID()) &&
                !camp.getCampSlot().getCommittees().contains(getUserID()))
                continue;
            registeredCampList.putCamp(camp.getName(), camp);
        }
        return registeredCampList;
    }
}