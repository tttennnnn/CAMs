package userpage.staff;

import camp.CampManager;
import camp.meta.Faculty;

import camp.meta.Location;
import camp.chat.EnquiryListManager;
import camp.chat.SuggestionListManager;
import camp.dates.CampDatesFormatter;
import camp.meta.MetaDataManager;
import camp.slots.CampSlotsManager;
import userpage.*;
import util.AppUtil;
import camp.CampList;
import util.exceptions.InvalidUserInputException;
import util.exceptions.PageTerminatedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StaffCampCreatorPage extends User implements CampAttendant, ApplicationPage, ViewerOfCampInfo, ViewerOfSomeCamps {
    StaffCampCreatorPage(String userID, String email, String name, Faculty faculty) {
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
            try {
                switch (input) {
                    case ("1") -> showUsage();
                    case ("2") -> showCamps();
                    case ("3") -> showCampDetail();
                    case ("4") -> editCampDetail();
                    case ("5") -> toggleCampVisibility();
                    case ("6") -> deleteCamp();
                    case ("7") -> viewSuggestions();
                    case ("8") -> answerSuggestion();
                    case ("9") -> viewEnquiries();
                    case ("10") -> answerEnquiry();
                    case ("11") -> generateReport();
                    case ("12") -> generatePerformanceReport();
                    case ("13") -> throw new PageTerminatedException();
                    default -> System.out.println("Invalid input.");
                }
            } catch ( InvalidUserInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Creator Menu]");
        showUsage();
        AppUtil.printSectionLine();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. List camps I created");
        System.out.println("\t3. View camp info");
        System.out.println("\t4. Edit camp detail");
        System.out.println("\t5. Toggle camp visibility");
        System.out.println("\t6. Delete camp");
        System.out.println("\t7. View suggestions");
        System.out.println("\t8. Approve/Reject Suggestion");
        System.out.println("\t9. View enquiries");
        System.out.println("\t10. Answer to enquiry");
        System.out.println("\t11. Generate report");
        System.out.println("\t12. Generate performance report for camp committee");
        System.out.println("\t13. Return to previous page");
    }
    @Override
    public void showCamps() {
        CampList campList = getVisibleCampList();
        System.out.println("List of camps I created: ");
        for (CampManager camp : campList.getSortedCampSet()) {
            System.out.println("\t" + camp.getName());
        }
    }
    @Override
    public void showCampDetail() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        CampManager camp = campList.getCamp(campName);

        System.out.printf("%-10s | %-15s | %-15s | %-10s | Description\n",
            "Name", "TotalSlot", "CommitteeSlot", "Location"
        );
        System.out.printf("%-10s | %-15s | %-15s | %-10s | %s\n",
            camp.getName(),
            camp.getSlotsManager().getTotalSlotAsString(),
            camp.getSlotsManager().getCommitteeSlotAsString(),
            camp.getMetaDataManager().getLocation(),
            camp.getMetaDataManager().getDescription()
        );
    }
    private void editCampDetail() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");
        CampManager camp = campList.getCamp(campName);

        System.out.print("Choose detail to edit [TotalSlot, CommitteeSlot, Location, Description]: ");
        String detail = sc.nextLine();
        switch (detail) {
            case ("TotalSlot"):
                System.out.print("Enter new total slot: ");
                break;
            case ("CommitteeSlot"):
                System.out.print("Enter new committee slot: ");
                break;
            case ("Location"):
                System.out.print("Enter new location " + Arrays.asList(Location.values()) + ": ");
                break;
            case ("Description"):
                System.out.print("Enter new description: ");
                break;
            default:
                throw new InvalidUserInputException("Invalid detail choice.");
        }

        String newValue = sc.nextLine();
        if (detail.equals("Location") || detail.equals("Description")) {
            try {
                switch (detail) {
                    case ("Location") -> camp.getMetaDataManager().setLocation(Location.valueOf(newValue));
                    case ("Description") -> camp.getMetaDataManager().setDescription(newValue);
                }
            } catch (IllegalArgumentException e) {
                throw new InvalidUserInputException("Invalid " + detail + ".");
            }
        } else {
            try{
                int slot = Integer.parseInt(newValue);
                switch (detail) {
                    case ("TotalSlot") -> {
                        CampSlotsManager.isValidSlots(slot, camp.getSlotsManager().getCommitteeSlot());
                        camp.getSlotsManager().setTotalSlot(slot);
                    }
                    case ("CommitteeSlot") -> {
                        CampSlotsManager.isValidSlots(camp.getSlotsManager().getTotalSlot(), slot);
                        camp.getSlotsManager().setCommitteeSlot(slot);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new InvalidUserInputException(e.getMessage());
            }
        }
        camp.getMetaDataManager().updateToFile(campName);
        camp.getSlotsManager().updateToFile(campName);
        System.out.println("Camp " + detail + " edited.");
    }
    private void toggleCampVisibility() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");
        CampManager camp = campList.getCamp(campName);
        MetaDataManager metaData = camp.getMetaDataManager();

        if (metaData.isVisible() && camp.getSlotsManager().isRegistered())
            throw new InvalidUserInputException("Visibility cannot be off - students are already registered.");

        metaData.toggleVisibility();
        metaData.updateToFile(camp.getName());
        System.out.println("Camp: " + campName + " - visibility toggled to " + ((metaData.isVisible()) ? "T" : "F"));
    }
    private void deleteCamp() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");
        CampManager camp = campList.getCamp(campName);

        if (camp.getSlotsManager().isRegistered())
            throw new InvalidUserInputException("Cannot be deleted - students are already registered.");

        campList.updateToFile(campName);
        System.out.println("Camp deleted.");
    }
    private void viewSuggestions() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");
        SuggestionListManager suggestions = campList.getCamp(campName).getSuggestionManager();

        System.out.println("*** Only unprocessed suggestions are shown. ***");
        System.out.printf("%-10s | %-10s | %s\n",
            "Index", "Committee", "Suggestion"
        );

        for (int index = 0; index < suggestions.size(); index++) {
            System.out.printf("%-10s | %-10s | %s\n",
                index, suggestions.getOwner(index), suggestions.getContent(index)
            );
        }
    }
    private void answerSuggestion() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        CampManager camp = campList.getCamp(campName);
        SuggestionListManager suggestions = camp.getSuggestionManager();
        if (index < 0 || index >= suggestions.size())
            throw new InvalidUserInputException("Invalid index.");

        System.out.print("Select action [approve, reject]: ");
        String action = sc.nextLine();
        String owner = suggestions.getOwner(index);
        CampSlotsManager slotsManager = camp.getSlotsManager();
        if (action.equals("approve")) {
            suggestions.removeSuggestion(index);
            slotsManager.incrementCommitteePoint(owner);
        } else if (action.equals("reject")) {
            suggestions.removeSuggestion(index);
            camp.getSlotsManager().decrementCommitteePoint(owner);
        } else {
            throw new InvalidUserInputException("Invalid action.");
        }
        suggestions.updateToFile(campName);
        slotsManager.updateToFile(campName);
        System.out.println("Suggestion " + action);
    }
    @Override
    public void viewEnquiries() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        EnquiryListManager enquiries = campList
            .getCamp(campName)
            .getEnquiryManager();

        System.out.printf("%-10s | %-30s | %s\n",
            "Index", "Enquiry", "Response"
        );
        for (int index = 0; index < enquiries.size(); index++) {
            String response = (enquiries.isProcessed(index)) ?
                enquiries.getAnswerContent(index) : "-";
            System.out.printf("%-10s | %-30s | %s\n",
                index, enquiries.getQuestionContent(index), response
            );
        }
    }
    @Override
    public void answerEnquiry() throws InvalidUserInputException {
        System.out.print("Enter camp name: ");
        Scanner sc = new Scanner(System.in);
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        CampManager camp = campList.getCamp(campName);
        EnquiryListManager enquiries = camp.getEnquiryManager();
        if (index < 0 || index >= enquiries.size())
            throw new InvalidUserInputException("Invalid index.");
        if (enquiries.isProcessed(index))
            throw new InvalidUserInputException("This enquiry is already processed.");

        System.out.print("Enter response for the chosen enquiry: ");
        String answerContent = sc.nextLine();
        enquiries.setAnswer(index, getUserID(), answerContent);
        enquiries.updateToFile(campName);
        System.out.println("Response submitted.");
    }
    @Override
    public void generateReport() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** This feature always overwrites if the report file already exists. ***");
        System.out.print("Enter camp name: ");
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        System.out.print("Choose filter [all, committee, attendee]: ");
        String filter = sc.nextLine();

        boolean showCommittee = false, showAttendee = false;
        if (filter.equals("all")) {
            showCommittee = true;
            showAttendee = true;
        } else if (filter.equals("committee")) {
            showCommittee = true;
        } else if (filter.equals("attendee")) {
            showAttendee = true;
        } else
            throw new InvalidUserInputException("Invalid filter.");

        // get camp
        CampManager camp = getVisibleCampList().getCamp(campName);

        // write camp details
        List<String> lines = new ArrayList<>();
        lines.add("| Camp Report | Filter: " + filter);
        lines.add("Name: " + camp.getName());
        lines.add("Creator: " + camp.getMetaDataManager().getStaffID());
        lines.add("Visibility: " + ((camp.getMetaDataManager().isVisible()) ? "T" : "F"));
        lines.add("Description: " + camp.getMetaDataManager().getDescription());
        lines.add("Dates: " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getStartDate()) +
            " - " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getEndDate())
        );
        lines.add("Registration deadline: " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getRegistrationDeadline())
        );
        lines.add("Faculty: " + camp.getMetaDataManager().getFaculty());
        lines.add("Location: " + camp.getMetaDataManager().getLocation());
        lines.add("Total slot: " + camp.getSlotsManager().getTotalSlotAsString());
        lines.add("Committee slot: " + camp.getSlotsManager().getCommitteeSlotAsString());

        // write committees & attendees
        if (showCommittee) {
            lines.add("Committees: ");
            for (String committee : camp.getSlotsManager().reportCommitteeList()) {
                lines.add("\t" + committee);
            }
        }
        if (showAttendee) {
            lines.add("Attendees: ");
            for (String attendee : camp.getSlotsManager().reportAttendeeList()) {
                lines.add("\t" + attendee);
            }
        }
        // write to file
        String reportFileName = "report_by_staff/" + campName + "_" + filter + ".txt";
        AppUtil.overwriteTXT(reportFileName, lines);
        System.out.println("Report generated as " + reportFileName);
    }
    private void generatePerformanceReport() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** This feature always overwrites if the report file already exists. ***");
        System.out.print("Enter camp name: ");
        String campName = sc.nextLine();

        CampList campList = getVisibleCampList();
        if (!campList.hasCamp(campName))
            throw new InvalidUserInputException("Invalid camp name.");

        // get camp
        CampManager camp = getVisibleCampList().getCamp(campName);

        // write camp details
        List<String> lines = new ArrayList<>();
        lines.add("| Camp Performance Report |");
        lines.add("Name: " + camp.getName());
        lines.add("Creator: " + camp.getMetaDataManager().getStaffID());
        lines.add("Visibility: " + ((camp.getMetaDataManager().isVisible()) ? "T" : "F"));
        lines.add("Description: " + camp.getMetaDataManager().getDescription());
        lines.add("Dates: " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getStartDate()) +
            " - " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getEndDate())
        );
        lines.add("Registration deadline: " +
            CampDatesFormatter.getDateAsString(camp.getDatesManager().getRegistrationDeadline())
        );
        lines.add("Faculty: " + camp.getMetaDataManager().getFaculty());
        lines.add("Location: " + camp.getMetaDataManager().getLocation());
        lines.add("Total slot: " + camp.getSlotsManager().getTotalSlotAsString());
        lines.add("Committee slot: " + camp.getSlotsManager().getCommitteeSlotAsString());

        // write committees & attendees
        lines.add("Committees: ");
        for (String committee : camp.getSlotsManager().reportCommitteeList()) {
            lines.add(String.format("\t%-15s %d point(s)", committee, camp.getSlotsManager().getCommitteePoint(committee)));
        }
        // write to file
        String reportFileName = "report_by_staff/" + campName + "_performance.txt";
        AppUtil.overwriteTXT(reportFileName, lines);
        System.out.println("Report generated as " + reportFileName);
    }
    @Override
    public CampList getVisibleCampList() {
        CampList campList = AppUtil.readCamps();
        CampList visibleCampList = new CampList();
        for (CampManager camp : campList.getSortedCampSet()) {
            if (camp.getMetaDataManager().getStaffID().equals(getUserID()))
                visibleCampList.putCamp(camp.getName(), camp);
        }
        return visibleCampList;
    }
}
