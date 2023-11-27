package userpage.student;

import camp.CampManager;
import camp.meta.Faculty;
import camp.chat.EnquiryListManager;
import camp.chat.SuggestionListManager;
import camp.dates.CampDatesFormatter;
import camp.slots.CampSlotsManager;
import userpage.ApplicationPage;
import userpage.CampAttendant;
import userpage.ViewerOfSomeCamps;
import userpage.User;
import util.AppUtil;
import camp.CampList;
import util.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * class that represent the user page of committee
 * @author Anqi
 * @date 2023/11/22
 */
public class CampCommitteePage extends User implements CampAttendant, ApplicationPage, ViewerOfSomeCamps {
    /**
     * name of camp that this student is registered as a committee
     */
    private final String myCampName;

    /**
     * constructor of CampCommitteePage
     * @param userID
     * @param email
     * @param name
     * @param faculty
     * @param myCampName
     */
    CampCommitteePage(String userID, String email, String name, Faculty faculty, String myCampName) {
        super(userID, email, name, faculty);
        this.myCampName = myCampName;
    }

    /**
     * the UI for Camp Committee
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
                    case ("2") -> viewMySuggestions();
                    case ("3") -> changeSuggestion();
                    case ("4") -> submitSuggestion();
                    case ("5") -> viewEnquiries();
                    case ("6") -> answerEnquiry();
                    case ("7") -> generateReport();
                    case ("8") -> throw new PageTerminatedException();
                    default -> System.out.println("Invalid input.");
                }
            } catch (InvalidUserInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * print the header for users to know which page they are using now
     */
    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Committee Menu]");
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
        System.out.println("\t2. View my suggestions");
        System.out.println("\t3. Edit/Delete suggestion");
        System.out.println("\t4. Submit suggestion");
        System.out.println("\t5. View enquiries");
        System.out.println("\t6. Answer to enquiry");// view/answer to all enquiries of my camp
        System.out.println("\t7. Generate report");
        System.out.println("\t8. Return to previous page");
    }
    
    /**
     * show unprocessed suggestions from user
     */
    private void viewMySuggestions() {
        System.out.println("*** Only unprocessed suggestions are shown. ***");
        System.out.printf("%-10s | %s\n",
            "Index", "Suggestion"
        );

        SuggestionListManager suggestions = getVisibleCampList()
            .getCamp(myCampName)
            .getSuggestionManager();
        suggestions = suggestions.getListManagerOfOwner(getUserID());

        for (int index = 0; index < suggestions.size(); index++) {
            System.out.printf("%-10s | %s\n",
                index, suggestions.getContent(index)
            );
        }
    }
    
    /**
     * edit the suggestions
     * @throws InvalidUserInputException
     */
    private void changeSuggestion() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose action [edit, delete]: ");
        String action = sc.nextLine();
        if (!action.equals("edit") && !action.equals("delete")) {
            throw new InvalidUserInputException("Invalid action choice.");
        }

        CampManager myCamp = getVisibleCampList().getCamp(myCampName);
        SuggestionListManager suggestions = myCamp.getSuggestionManager();

        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        int realIndex;
        for (realIndex = 0; realIndex < suggestions.size(); realIndex++) {
            if (suggestions.getOwner(realIndex).equals(getUserID())) {
                if (index-- == 0) {
                    break;
                }
            }
        }
        if (realIndex >= suggestions.size())
            throw new InvalidUserInputException("Invalid index.");

        if (action.equals("edit")) {
            System.out.print("Enter new suggestion: ");
            suggestions.editSuggestion(realIndex, sc.nextLine());
        } else {
            suggestions.removeSuggestion(realIndex);
            // decrease committee point
            CampSlotsManager slotsManager = myCamp.getSlotsManager();
            slotsManager.decrementCommitteePoint(getUserID());
        }

        suggestions.updateToFile(myCampName);
        myCamp.getSlotsManager().updateToFile(myCampName);
        if (action.equals("edit"))
            System.out.println("Suggestion updated.");
        else
            System.out.println("Suggestion deleted");
    }
    
    /**
     * create suggestions to camp
     */
    private void submitSuggestion() {
        CampManager myCamp = getVisibleCampList().getCamp(myCampName);
        SuggestionListManager suggestions = myCamp.getSuggestionManager();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new suggestion: ");
        suggestions.addSuggestion(getUserID(), sc.nextLine());

        // add committee point
        CampSlotsManager slotsManager = myCamp.getSlotsManager();
        slotsManager.incrementCommitteePoint(getUserID());

        suggestions.updateToFile(myCampName);
        myCamp.getSlotsManager().updateToFile(myCampName);
        System.out.println("Suggestion submitted.");
    }
    
    /**
     * show enquiries of the camp
     */
    @Override
    public void viewEnquiries() {
        System.out.printf("%-10s | %-30s | %s\n",
            "Index", "Enquiry", "Response"
        );

        EnquiryListManager enquiries = getVisibleCampList()
            .getCamp(myCampName)
            .getEnquiryManager();
        for (int index = 0; index < enquiries.size(); index++) {
            String response = (enquiries.isProcessed(index)) ?
                enquiries.getAnswerContent(index) : "-";
            System.out.printf("%-10s | %-30s | %s\n",
                index, enquiries.getQuestionContent(index), response
            );
        }
    }
    
    /**
     * answer enquiries of the camp
     * @throws InvalidUserInputException
     */
    @Override
    public void answerEnquiry() throws InvalidUserInputException {
        CampManager myCamp = getVisibleCampList().getCamp(myCampName);
        EnquiryListManager enquiries = myCamp.getEnquiryManager();

        Scanner sc = new Scanner(System.in);
        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        if (index < 0 || index >= enquiries.size())
            throw new InvalidUserInputException("Invalid index.");

        if (enquiries.isProcessed(index))
            throw new InvalidUserInputException("This enquiry is already processed.");

        System.out.print("Enter response for the chosen enquiry: ");
        String answerContent = sc.nextLine();
        enquiries.setAnswer(index, getUserID(), answerContent);

        // add committee point
        CampSlotsManager slotsManager = myCamp.getSlotsManager();
        slotsManager.incrementCommitteePoint(getUserID());

        enquiries.updateToFile(myCampName);
        slotsManager.updateToFile(myCampName);
        System.out.println("Response submitted.");
    }
    
    /**
     * generate Report of camp
     * @throws InvalidUserInputException
     */
    @Override
    public void generateReport() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** This feature always overwrites if the report file already exists. ***");
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
        CampManager myCamp = getVisibleCampList().getCamp(myCampName);

        // write camp details
        List<String> lines = new ArrayList<>();
        lines.add("| Camp Report | Filter: " + filter);
        lines.add("Name: " + myCamp.getName());
        lines.add("Description: " + myCamp.getMetaDataManager().getDescription());
        lines.add("Dates: " +
            CampDatesFormatter.getDateAsString(myCamp.getDatesManager().getStartDate()) +
            " - " +
            CampDatesFormatter.getDateAsString(myCamp.getDatesManager().getEndDate())
        );
        lines.add("Registration deadline: " +
            CampDatesFormatter.getDateAsString(myCamp.getDatesManager().getRegistrationDeadline())
        );
        lines.add("Faculty: " + myCamp.getMetaDataManager().getFaculty());
        lines.add("Location: " + myCamp.getMetaDataManager().getLocation());
        lines.add("Total slot: " + myCamp.getSlotsManager().getTotalSlotAsString());
        lines.add("Committee slot: " + myCamp.getSlotsManager().getCommitteeSlotAsString());

        // write committees & attendees
        if (showCommittee) {
            lines.add("Committees: ");
            for (String committee : myCamp.getSlotsManager().reportCommitteeList()) {
                lines.add("\t" + committee);
            }
        }
        if (showAttendee) {
            lines.add("Attendees: ");
            for (String attendee : myCamp.getSlotsManager().reportAttendeeList()) {
                lines.add("\t" + attendee);
            }
        }
        // write to file
        String reportFileName = "report_by_committee/" + myCampName + "_" + filter + ".txt";
        AppUtil.overwriteTXT(reportFileName, lines);
        System.out.println("Report generated as " + reportFileName);
    }
    
    /**
     * get List of Visible Camp
     * @return {@link CampList}
     */
    @Override
    public CampList getVisibleCampList() {
        CampManager camp = AppUtil.readCamps().getCamp(myCampName);
        CampList visibleCampList = new CampList();
        visibleCampList.putCamp(camp.getName(), camp);
        return visibleCampList;
    }
}
