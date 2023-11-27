package userpage.student;

import camp.*;
import camp.chat.EnquiryListManager;
import camp.dates.CampDatesFormatter;
import camp.meta.Faculty;
import camp.meta.Location;
import camp.meta.MetaDataManager;
import camp.slots.CampSlotsManager;
import userpage.ApplicationPage;
import userpage.ViewerOfCampInfo;
import userpage.ViewerOfSomeCamps;
import userpage.User;
import util.AppUtil;
import util.exceptions.InvalidUserInputException;
import util.exceptions.PageTerminatedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * class that represents the camp page of student
 * @author Anqi
 * @date 2023/11/22
 */
class StudentCampPage extends User implements ApplicationPage, ViewerOfCampInfo, ViewerOfSomeCamps {
    /**
     * constructor of StudentCampPage
     * @param userID
     * @param email
     * @param name
     * @param faculty
     */
    StudentCampPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    /**
     * the camp UI for student
     * @throws PageTerminatedException
     */
    @Override
    public void runPage() throws PageTerminatedException {
        printHeader();
        Scanner sc = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("[Input]: ");
            input = sc.nextLine();
            try {
                switch (input) {
                    case ("1") -> showUsage();
                    case ("2") -> showCamps();
                    case ("3") -> showCampDetail();
                    case ("4") -> register();
                    case ("5") -> withdraw();
                    case ("6") -> viewEnquiries();
                    case ("7") -> changeEnquiries();
                    case ("8") -> submitEnquiry();
                    case ("9") -> throw new PageTerminatedException();
                    default -> System.out.println("Invalid input.");
                }
            } catch (InvalidUserInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     *  print the header for users to know which page they are using now
     */
    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Menu]");
        showUsage();
        AppUtil.printSectionLine();
    }

    /**
     * print down the operation options for user
     */
    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View camps");
        System.out.println("\t3. View additional camp info");
        System.out.println("\t4. Register for camp");
        System.out.println("\t5. Withdraw from camp");
        System.out.println("\t6. View my enquiries");
        System.out.println("\t7. Edit/Delete enquiry");
        System.out.println("\t8. Submit enquiry");
        System.out.println("\t9. Return to previous page");
    }

    /**
     * show list of Camps with different sorting option
     * @throws InvalidUserInputException
     */
    @Override
    public void showCamps() throws InvalidUserInputException {
        System.out.print("Choose filter type [all, location, date]: ");
        Scanner sc = new Scanner(System.in);
        String filterType = sc.nextLine();
        String filter = null;

        // get filter value
        switch (filterType) {
            case ("all"):
                break;
            case ("location"):
                System.out.print("Choose location " + Arrays.asList(Location.values()) + ": ");
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
                    CampDatesFormatter.getDateAsLocalDate(filter);
                } catch (IllegalArgumentException e) {
                    throw new InvalidUserInputException(e.getMessage());
                }
                break;
            default:
                throw new InvalidUserInputException("Invalid filter.");
        }

        //add camps according to filters
        CampList campList = getVisibleCampList();
        ArrayList<CampManager> filteredCamps = new ArrayList<>();
        for (CampManager camp : campList.getSortedCampSet()) {
            switch (filterType) {
                case ("all"):
                    filteredCamps.add(camp);
                    break;
                case ("location"):
                    if (camp.getMetaDataManager().getLocation().name().equals(filter))
                        filteredCamps.add(camp);
                    break;
                case ("date"):
                    if (camp.getDatesManager().containsDate(CampDatesFormatter.getDateAsLocalDate(filter)))
                        filteredCamps.add(camp);
                    break;
            }
        }

        System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
            "Name", "TotalSlot", "CommitteeSlot", "Location", "Faculty", "Status"
        );
        for (CampManager camp : filteredCamps) {
            System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s\n",
                camp.getName(),
                camp.getSlotsManager().getTotalSlotAsString(),
                camp.getSlotsManager().getCommitteeSlotAsString(),
                camp.getMetaDataManager().getLocation(),
                camp.getMetaDataManager().getFaculty(),
                camp.getSlotsManager().getUserRole(getUserID())
            );
        }
    }
    
    /**
     * show additional detail of certain camps
     * @throws InvalidUserInputException
     */
    @Override
    public void showCampDetail() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        CampManager camp = campList.getCamp(campName);

        System.out.printf("%-10s | %-15s | %-15s | %-20s | Description\n",
            "Name", "StartDate", "EndDate", "RegistrationDeadline"
        );
        System.out.printf("%-10s | %-15s | %-15s | %-20s | %s\n",
            camp.getName(),
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getStartDate()),
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getEndDate()),
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getRegistrationDeadline()),
            camp.getMetaDataManager().getDescription()
        );
    }
    
    /**
     * register a available camp
     * @throws InvalidUserInputException
     */
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

        CampManager camp = campList.getCamp(campName);
        // check registration deadline
        if (LocalDate.now().isAfter(camp.getDatesManager().getRegistrationDeadline()))
            throw new InvalidUserInputException("It is past the registration deadline.");
        // check withdrawals
        if (camp.getSlotsManager().hasWithdrawn(getUserID()))
            throw new InvalidUserInputException("You have already withdrawn from this camp.");
        // check time clashes
        for (CampManager registeredCamp : getRegisteredCampList().getSortedCampSet()) {
            if (registeredCamp.getDatesManager().hasTimeClash(camp.getDatesManager()))
                throw new InvalidUserInputException("Time clashes with " + registeredCamp.getName());
        }

        System.out.print("Enter role [Attendee, Committee]: ");
        String role = sc.nextLine();
        CampSlotsManager slotsManager = camp.getSlotsManager();
        switch (role) {
            case ("Attendee"):
                if (slotsManager.getTotalVacancy() == 0)
                    throw new InvalidUserInputException("Camp is full.");
                slotsManager.addAttendee(getUserID());
                break;
            case ("Committee"):
                if (slotsManager.getTotalVacancy() == 0 || slotsManager.getCommitteeVacancy() == 0)
                    throw new InvalidUserInputException("Camp is full.");

                String committeeStatus = CampSlotsManager.getUserCommitteeStatus(getUserID());
                if (!committeeStatus.equals("-"))
                    throw new InvalidUserInputException("You are already registered as a committee for camp: " + committeeStatus);
                slotsManager.addCommittee(getUserID(), 0);
                break;
            default:
                throw new InvalidUserInputException("Invalid role.");
        }

        slotsManager.updateToFile(campName);
        System.out.println("Registered as " + role);
    }
    
    /**
     * withdraw from a camp
     * @throws InvalidUserInputException
     */
    private void withdraw() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getRegisteredCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        CampManager camp = campList.getCamp(campName);
        CampSlotsManager slotsManager = camp.getSlotsManager();
        if (slotsManager.hasAttendee(getUserID()))
            slotsManager.removeAttendee(getUserID());
        else
            throw new InvalidUserInputException("Cannot withdraw from camp committee role.");

        // add to withdrawal list
        slotsManager.addWithdrawn(getUserID());

        slotsManager.updateToFile(campName);
        System.out.println("Withdrawn from " + camp.getName());
    }
    
    /**
     * view a list of enquiries
     */
    private void viewEnquiries() {
        System.out.printf("%-10s | %-10s | %-30s | %s\n",
            "Index", "Camp", "Enquiry", "Response"
        );
        CampList campList = getVisibleCampList();
        int index = 0;
        for (CampManager camp : campList.getSortedCampSet()) {
            EnquiryListManager enquiries = camp.getEnquiryManager();
            enquiries = enquiries.getListManagerOfOwner(getUserID());
            for (int i = 0; i < enquiries.size(); i++) {
                String response = (enquiries.isProcessed(i)) ? enquiries.getAnswerContent(i) : "-";
                System.out.printf("%-10s | %-10s | %-30s | %s\n",
                    index++, camp.getName(), enquiries.getQuestionContent(i), response
                );
            }
        }
    }
    
    /**
     * edit the enquiries
     * @throws InvalidUserInputException
     */
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
        int realIndex = 0;
        for (CampManager camp : campList.getSortedCampSet()) {
            if (campName != null)
                break;
            EnquiryListManager enquiries = camp.getEnquiryManager();
            for (realIndex = 0; realIndex < enquiries.size(); realIndex++) {
                if (enquiries.getQuestionOwner(realIndex).equals(getUserID())) {
                    if (index-- == 0) {
                        campName = camp.getName();
                        break;
                    }
                }
            }
        }

        if (campName == null)
            throw new InvalidUserInputException("Invalid index.");

        EnquiryListManager enquiries = campList.getCamp(campName).getEnquiryManager();

        // if enquiry is processed -> cannot edit/delete
        if (enquiries.isProcessed(realIndex))
            throw new InvalidUserInputException("This enquiry is already processed.");

        if (action.equals("edit")) {
            System.out.print("Enter new enquiry: ");
            enquiries.editQuestion(realIndex, sc.nextLine());
        } else {
            enquiries.removeEnquiry(realIndex);
        }

        // writ to csv
        enquiries.updateToFile(campName);
        if (action.equals("edit"))
            System.out.println("Enquiry updated.");
        else
            System.out.println("Enquiry deleted");
    }
    
    /**
     * submit the enquiry to system
     * @throws InvalidUserInputException
     */
    private void submitEnquiry() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter camp name: ");
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        EnquiryListManager enquiries = campList.getCamp(campName).getEnquiryManager();
        System.out.print("Enter new enquiry: ");
        enquiries.addEnquiry(getUserID(), sc.nextLine());

        enquiries.updateToFile(campName);
        System.out.println("Enquiry submitted.");
    }
    
    /**
     * get List of Visible Camps
     * @return {@link CampList}
     */
    @Override
    public CampList getVisibleCampList() {
        CampList campList = AppUtil.readCamps();
        CampList visibleCampList = new CampList();
        for (CampManager camp : campList.getSortedCampSet()) {
            MetaDataManager metaData = camp.getMetaDataManager();
            if (!metaData.isVisible())
                continue;
            if (metaData.getFaculty() != Faculty.NTU && metaData.getFaculty() != getFaculty())
                continue;
            visibleCampList.putCamp(camp.getName(), camp);
        }
        return visibleCampList;
    }
    private CampList getRegisteredCampList() {
        CampList campList = AppUtil.readCamps();
        CampList registeredCampList = new CampList();
        for (CampManager camp : campList.getSortedCampSet()) {
            if (!camp.getSlotsManager().hasAttendee(getUserID()) &&
                !camp.getSlotsManager().hasCommittee(getUserID()))
                continue;
            registeredCampList.putCamp(camp.getName(), camp);
        }
        return registeredCampList;
    }
}