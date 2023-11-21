package userpage.student;

import camp.*;
import camp.convo.Enquiry;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;
import util.CampList;
import util.exceptions.InvalidUserInputException;
import util.exceptions.PageTerminatedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class StudentCampPage extends User implements ApplicationPage {

    public StudentCampPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    @Override
    public void runPage() throws PageTerminatedException {
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
                    try {
                        showCamps();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("3"):
                    try {
                        showCampDetail();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("4"):
                    try {
                        register();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("5"):
                    try {
                        withdraw();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("6"):
                    viewEnquiries();
                    break;
                case ("7"):
                    try {
                        changeEnquiries();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("8"):
                    try {
                        submitEnquiry();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("9"):
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
        System.out.println("\t6. View my enquiries");
        System.out.println("\t7. Edit/Delete an enquiry");
        System.out.println("\t8. Submit an enquiry");
        System.out.println("\t9. Return to previous page");
    }

    private void showCamps() throws InvalidUserInputException {
        System.out.print("Choose filter type [all, location, date]: ");
        Scanner sc = new Scanner(System.in);
        String filterType = sc.nextLine();
        String filter = null;

        // get filter value
        switch (filterType) {
            case ("all"):
                break;
            case ("location"):
                System.out.print("Enter location " + Arrays.asList(Location.values()) + ": ");
                filter = sc.nextLine();
                try {
                    Location.valueOf(filter);
                } catch (IllegalArgumentException e) {
                    throw new InvalidUserInputException("Invalid location.");
                }
                break;
            case ("date"):
                System.out.println("Enter date for camps to contain [y/m/d]: ");
                filter = sc.nextLine();
                try {
                    CampDates.getDateAsLocalDate(filter);
                } catch (NumberFormatException e) {
                    throw new InvalidUserInputException("Invalid date.");
                }
                break;
            default:
                throw new InvalidUserInputException("Invalid filter.");
        }

        //add camps according to filters
        CampList campList = getVisibleCampList();
        ArrayList<Camp> filteredCamps = new ArrayList<>();
        for (Camp camp : campList.getSortedCampSet()) {
            switch (filterType) {
                case ("all"):
                    filteredCamps.add(camp);
                    break;
                case ("location"):
                    if (camp.getLocation().name().equals(filter))
                        filteredCamps.add(camp);
                    break;
                case ("date"):
                    if (camp.containsDate(CampDates.getDateAsLocalDate(filter)))
                        filteredCamps.add(camp);
                    break;
            }
        }

        System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
            "Name", "TotalSlot", "CommitteeSlot", "Location", "Faculty", "Status"
        );
        for (Camp camp : filteredCamps) {
            System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
                camp.getName(),
                camp.getTotalSlotAsString(),
                camp.getCommitteeSlotAsString(),
                camp.getLocation(),
                camp.getFaculty(),
                camp.getCampStatus(getUserID())
            );
        }
    }
    private void showCampDetail() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

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
    private void register() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        // if already registered
        CampList registeredCampList = getRegisteredCampList();
        if (registeredCampList.hasCamp(campName))
            throw new InvalidUserInputException("You have already registered for this camp.");

        CampList campList = getVisibleCampList();
        // if camp not visible
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        Camp camp = campList.getCamp(campName);
        // check registration deadline
        if (LocalDate.now().isAfter(camp.getCampDates().getRegistrationDeadline()))
            throw new InvalidUserInputException("It is past the registration deadline.");
        // check withdrawals
        if (camp.hasWithdrawn(getUserID()))
            throw new InvalidUserInputException("You have already withdrawn from this camp.");
        // check time clashes
        for (Camp registeredCamp : getRegisteredCampList().getSortedCampSet()) {
            if (registeredCamp.hasTimeClash(camp))
                throw new InvalidUserInputException("Time clashes with " + registeredCamp.getName());
        }

        System.out.print("Enter role [Attendee, Committee]: ");
        String role = sc.nextLine();
        switch (role) {
            case ("Attendee"):
                if (camp.getTotalVacancy() == 0)
                    throw new InvalidUserInputException("Camp is full.");
                camp.addAttendee(getUserID());
                break;
            case ("Committee"):
                if (camp.getTotalVacancy() == 0 || camp.getCommitteeVacancy() == 0)
                    throw new InvalidUserInputException("Camp is full.");

                String committeeStatus = StudentMainPage.getCommitteeStatusForUser(getUserID());
                if (!committeeStatus.equals("-"))
                    throw new InvalidUserInputException("You are already registered as a committee for camp: " + committeeStatus);
                camp.addCommittee(getUserID(), 0);
                break;
            default:
                throw new InvalidUserInputException("Invalid role.");
        }

        CampSlot.updateCampSlotToFile(campName, camp.getCampSlot());
        System.out.println("Registered as " + role);
    }
    private void withdraw() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getRegisteredCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        Camp camp = campList.getCamp(campName);
        if (camp.getCampSlot().getAttendees().contains(getUserID()))
            camp.getCampSlot().getAttendees().remove(getUserID());
        else
            throw new InvalidUserInputException("Cannot withdraw from camp committee role.");

        // add to withdrawal list
        camp.getCampSlot().getWithdrawns().add(getUserID());

        CampSlot.updateCampSlotToFile(campName, camp.getCampSlot());
        System.out.println("Withdrawn from " + camp.getName());
    }
    private void viewEnquiries() {
        System.out.printf("%-10s | %-10s | %-30s | %s\n",
            "Index", "Camp", "Enquiry", "Response"
        );

        CampList campList = getVisibleCampList();
        int index = 0;
        for (Camp camp : campList.getSortedCampSet()) {
            for (Enquiry enquiry : camp.getEnquiries()) {
                if (enquiry.getQuestion().getOwner().equals(getUserID())) {
                    String response = (enquiry.isProcessed()) ? enquiry.getAnswer().getContent() : "-";
                    System.out.printf("%-10s | %-10s | %-30s | %s\n",
                        index, camp.getName(), enquiry.getQuestion().getContent(), response
                    );
                    index++;
                }
            }
        }
    }
    private void changeEnquiries() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose action [edit, delete]: ");
        String action = sc.nextLine();
        if (!action.equals("edit") && !action.equals("delete")) {
            throw new InvalidUserInputException("Invalid action choice.");
        }

        CampList campList = getVisibleCampList();
        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        String campName = null;
        int i = 0;
        for (Camp camp : campList.getSortedCampSet()) {
            if (campName != null)
                break;

            ArrayList<Enquiry> enquiries = camp.getEnquiries();
            for (i = 0; i < enquiries.size(); i++) {
                if (enquiries.get(i).getQuestion().getOwner().equals(getUserID())) {
                    if (index == 0) {
                        campName = camp.getName();
                        break;
                    }
                    index--;
                }
            }
        }
        if (campName == null)
            throw new InvalidUserInputException("Invalid index.");

        ArrayList<Enquiry> enquiries = campList.getCamp(campName).getEnquiries();

        // if enquiry is processed -> cannot edit/delete
        if (enquiries.get(i).isProcessed())
            throw new InvalidUserInputException("This enquiry is already processed.");

        if (action.equals("edit")) {
            Enquiry enquiry = enquiries.get(i);
            System.out.print("Enter new enquiry: ");
            enquiry.editQuestion(sc.nextLine());
        } else {
            enquiries.remove(i);
        }

        // writ to csv
        Enquiry.updateEnquiriesToFile(campName, enquiries);
        if (action.equals("edit"))
            System.out.println("Enquiry updated.");
        else
            System.out.println("Enquiry deleted");
    }
    private void submitEnquiry() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter camp name: ");
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        ArrayList<Enquiry> enquiries = campList.getCamp(campName).getEnquiries();
        System.out.print("Enter new enquiry: ");
        enquiries.add(Enquiry.createEnquiry(getUserID(), sc.nextLine()));

        Enquiry.updateEnquiriesToFile(campName, enquiries);
        System.out.println("Enquiry submitted.");
    }
    private CampList getVisibleCampList() {
        CampList campList = AppUtil.readCamps();
        CampList visibleCampList = new CampList();
        for (Camp camp : campList.getSortedCampSet()) {
            if (!camp.getVisibility())
                continue;
            if (camp.getFaculty() != Faculty.NTU && camp.getFaculty() != getFaculty())
                continue;
            visibleCampList.putCamp(camp.getName(), camp);
        }
        return visibleCampList;
    }
    private CampList getRegisteredCampList() {
        CampList campList = AppUtil.readCamps();
        CampList registeredCampList = new CampList();
        for (Camp camp : campList.getSortedCampSet()) {
            if (!camp.getCampSlot().getAttendees().contains(getUserID()) &&
                !camp.getCampSlot().getCommittees().containsKey(getUserID()))
                continue;
            registeredCampList.putCamp(camp.getName(), camp);
        }
        return registeredCampList;
    }
}