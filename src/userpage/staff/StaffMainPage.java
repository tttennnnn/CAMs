package userpage.staff;

import camp.*;
import camp.dates.CampDatesFormatter;
import camp.dates.CampDatesManager;
import camp.meta.Faculty;
import camp.meta.Location;
import camp.slots.CampSlotsManager;
import userpage.ViewerOfCampInfo;
import userpage.ViewerOfSomeCamps;
import userpage.UserMainPage;
import util.AppUtil;
import util.exceptions.InvalidUserInputException;
import util.exceptions.PageTerminatedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * class that represent the main user page of staff
 * @author Anqi
 * @date 2023/11/22
 */
public class StaffMainPage extends UserMainPage implements ViewerOfCampInfo, ViewerOfSomeCamps {
    /**
     * constructor of StaffMainPage
     * @param userID
     * @param email
     * @param name
     * @param faculty
     */
    public StaffMainPage(String userID, String email, String name, Faculty faculty) {
        super(userID, email, name, faculty);
    }

    /**
     * the UI for Camp staff
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
                    case ("2") -> showProfile();
                    case ("3") -> showCamps();
                    case ("4") -> showCampDetail();
                    case ("5") -> createCamp();
                    case ("6") -> {
                        try {
                            openCampCreatorPage();
                        } catch (PageTerminatedException e) {
                            runPage();
                        }
                    }
                    case ("7") -> {
                        try {
                            openChangePasswordPage();
                        } catch (PageTerminatedException e) {
                            runPage();
                        }
                    }
                    case ("8") -> {
                        System.out.println("You have been logged out.");
                        throw new PageTerminatedException();
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (InvalidUserInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * print down the operation options for user
     */
    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View profile");
        System.out.println("\t3. View all camps");
        System.out.println("\t4. View additional camp info");
        System.out.println("\t5. Create camp");
        System.out.println("\t6. To my created camps");
        System.out.println("\t7. Change password");
        System.out.println("\t8. Log out");
    }

    /**
     * show the profile of user
     */
    @Override
    public void showProfile() {
            System.out.println("[Staff Main Menu] " + getFirstLoginPrompt());
            System.out.println("Name: " + getName());
            System.out.println("Email: " + getEmail());
            System.out.println("Faculty: " + getFaculty());
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

        System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s | %s\n",
            "Name", "TotalSlot", "CommitteeSlot", "Location", "Faculty", "Visibility", "Creator"
        );
        for (CampManager camp : filteredCamps) {
            System.out.printf("%-10s | %-10s | %-15s | %-10s | %-10s | %-10s | %s\n",
                camp.getName(),
                camp.getSlotsManager().getTotalSlotAsString(),
                camp.getSlotsManager().getCommitteeSlotAsString(),
                camp.getMetaDataManager().getLocation(),
                camp.getMetaDataManager().getFaculty(),
                (camp.getMetaDataManager().isVisible()) ? "T" : "F",
                camp.getMetaDataManager().getStaffID()
            );
        }
    }
    
    /**
     * show the detail of certain camp
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
        CampDatesManager datesManager = camp.getDatesManager();

        System.out.printf("%-10s | %-15s | %-15s | %-20s | Description\n",
            "Name", "StartDate", "EndDate", "RegistrationDeadline"
        );
        System.out.printf("%-10s | %-15s | %-15s | %-20s | %s\n",
            camp.getName(),
            CampDatesFormatter.getDateAsString(datesManager.getStartDate()),
            CampDatesFormatter.getDateAsString(datesManager.getEndDate()),
            CampDatesFormatter.getDateAsString(datesManager.getRegistrationDeadline()),
            camp.getMetaDataManager().getDescription()
        );
    }
    
    /**
     * create a new camp
     * @throws InvalidUserInputException
     */
    private void createCamp() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        CampList campList = AppUtil.readCamps();
        // get camp name
        System.out.print("Enter camp name: ");
        String campName = sc.nextLine();
        if (campList.hasCamp(campName))
            throw new InvalidUserInputException("This camp name already exists");
        // get description
        System.out.print("Enter description: ");
        String description = sc.nextLine();
        // get faculty
        System.out.print("Choose faculty " + Arrays.asList(Faculty.values()) + ": ");
        Faculty faculty;
        try {
            faculty = Faculty.valueOf(sc.nextLine());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException("Invalid faculty.");
        }
        // get location
        System.out.print("Choose location " + Arrays.asList(Location.values()) + ": ");
        Location location;
        try {
            location = Location.valueOf(sc.nextLine());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException("Invalid location.");
        }
        // get committee slot
        System.out.print("Enter camp committee slots: ");
        int maxCommittee;
        try {
            maxCommittee = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid slot number.");
        }
        // get total slot
        System.out.print("Enter total slots: ");
        int maxTotal;
        try {
            maxTotal = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid slot number.");
        }
        try {
            CampSlotsManager.isValidSlots(maxTotal, maxCommittee);
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
        // get start date
        System.out.print("Enter camp start date [y/m/d]: ");
        LocalDate startDate;
        try {
            startDate = CampDatesFormatter.getDateAsLocalDate(sc.nextLine());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
        // get end date
        System.out.print("Enter camp end date [y/m/d]: ");
        LocalDate endDate;
        try {
            endDate = CampDatesFormatter.getDateAsLocalDate(sc.nextLine());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
        // get registration deadline
        System.out.print("Enter registration deadline [y/m/d]: ");
        LocalDate registrationDeadline;
        try {
            registrationDeadline = CampDatesFormatter.getDateAsLocalDate(sc.nextLine());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
        try {
            CampDatesManager.isValidDates(startDate, endDate, registrationDeadline);
        } catch (IllegalArgumentException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
        // get visibility
        System.out.print("Enter visibility [T, F]: ");
        String visibilityStr = sc.nextLine();
        boolean visibility;
        if (visibilityStr.equals("T"))
            visibility = true;
        else if (visibilityStr.equals("F"))
            visibility = false;
        else
            throw new InvalidUserInputException("Invalid visibility choice.");

        // create camp
        CampManager camp = CampManager.createInstance(
            campName, getUserID(), description,
            visibility,
            faculty,
            location,
            maxTotal, maxCommittee,
            startDate, endDate, registrationDeadline
        );

        camp.getMetaDataManager().updateToFile(campName);
        camp.getSlotsManager().updateToFile(campName);
        camp.getDatesManager().updateToFile(campName);
        camp.getEnquiryManager().updateToFile(campName);
        camp.getSuggestionManager().updateToFile(campName);

        System.out.println("Camp " + campName + " created.");
    }
    
    /**
     * go to the camp creator page
     * @throws PageTerminatedException
     */
    private void openCampCreatorPage() throws PageTerminatedException {
        StaffCampCreatorPage campCreatorPage = new StaffCampCreatorPage(getUserID(), getEmail(), getName(), getFaculty());
        campCreatorPage.runPage();
    }
    
    /**
     * get List of Visible Camp
     * @return {@link CampList}
     */
    @Override
    public CampList getVisibleCampList() {
        return AppUtil.readCamps();
    }
}