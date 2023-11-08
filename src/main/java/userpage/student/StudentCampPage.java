package userpage.student;

import camp.Camp;
import camp.CampDates;
import camp.CampViewFilter;
import camp.Faculty;
import com.opencsv.exceptions.CsvException;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;
import util.CampList;
import util.exceptions.InvalidCampException;
import util.exceptions.PageTerminatedException;

import java.io.IOException;
import java.util.Arrays;
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
                    register();
                    break;
                case ("5"):
                    withdraw();
                    break;
                case ("7"):
                    throw new PageTerminatedException();
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
        System.out.println("\t6. To Enquiry page");
        System.out.println("\t7. Return to previous page");
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
        CampList campList = AppUtil.readCamps();
        TreeMap<Object, Camp> campTreeMap = new TreeMap<>();

        for (Camp camp : campList.getCampSet()) {
            if (camp.getVisibility() &&
                (camp.getFaculty() == Faculty.NTU || camp.getFaculty() == getFaculty())) {
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

        CampList campList = AppUtil.readCamps();
        if (!campList.hasCamp(campName))
            throw new InvalidCampException();

        Camp camp = campList.getCamp(campName);
        if (camp.getFaculty() != Faculty.NTU && camp.getFaculty() != getFaculty()) {
            throw new InvalidCampException();
        }

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

    private void register() {

    }

    private void withdraw() {

    }
}