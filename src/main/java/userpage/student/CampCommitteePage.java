package userpage.student;

import camp.Camp;
import camp.CampDates;
import camp.CampSlot;
import camp.Faculty;
import camp.convo.Enquiry;
import camp.convo.Suggestion;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;
import util.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CampCommitteePage extends User implements ApplicationPage {
    private final String myCampName;
    public CampCommitteePage(String userID, String email, String name, Faculty faculty, String myCampName) {
        super(userID, email, name, faculty);
        this.myCampName = myCampName;
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
                    viewMySuggestions();
                    break;
                case ("3"):
                    try {
                        changeSuggestion();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("4"):
                    submitSuggestion();
                    break;
                case ("5"):
                    viewEnquiries();
                    break;
                case ("6"):
                    try {
                        answerEnquiry();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("7"):
                    try {
                        generateReport();
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("8"):
                    throw new PageTerminatedException();
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    @Override
    public void printHeader() {
        AppUtil.printSectionLine();
        System.out.println("[Camp Committee Menu]");
        System.out.println("My Camp: " + myCampName);
        showUsage();
        AppUtil.printSectionLine();
    }

    @Override
    public void showUsage() {
        System.out.println("Usage:");
        System.out.println("\t1. View usage");
        System.out.println("\t2. View my suggestions");
        System.out.println("\t3. Edit/Delete a suggestion");
        System.out.println("\t4. Submit a suggestion");
        System.out.println("\t5. View enquiries");
        System.out.println("\t6. Answer to an enquiry");// view/answer to all enquiries of my camp
        System.out.println("\t7. Generate report");
        System.out.println("\t8. Return to previous page");
    }
    private void viewMySuggestions() {
        System.out.println("*** Only unprocessed suggestions are shown. ***");
        System.out.printf("%-10s | %s\n",
            "Index", "Suggestion"
        );

        ArrayList<Suggestion> suggestions = AppUtil.readCamps()
            .getCamp(myCampName)
            .getSuggestions();
        int index = 0;
        for (Suggestion suggestion : suggestions) {
            if (suggestion.getOwner().equals(getUserID())) {
                System.out.printf("%-10s | %s\n",
                    index, suggestion.getContent()
                );
                index++;
            }
        }
    }
    private void changeSuggestion() throws InvalidUserInputException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose action [edit, delete]: ");
        String action = sc.nextLine();
        if (!action.equals("edit") && !action.equals("delete")) {
            throw new InvalidUserInputException("Invalid action choice.");
        }

        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Suggestion> suggestions = myCamp.getSuggestions();

        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        int realIndex;
        for (realIndex = 0; realIndex < suggestions.size(); realIndex++) {
            Suggestion suggestion = suggestions.get(realIndex);
            if (suggestion.getOwner().equals(getUserID())) {
                if (index == 0)
                    break;
                index--;
            }
        }

        try {
            if (action.equals("edit")) {
                Suggestion suggestion = suggestions.get(realIndex);
                System.out.print("Enter new suggestion: ");
                suggestion.editSuggestion(sc.nextLine());
            } else {
                suggestions.remove(realIndex);
                // decrease committee point
                int point = myCamp.getCommitteePoint(getUserID()) - 1;
                myCamp.setCommitteePoint(getUserID(), point);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        Suggestion.updateSuggestionsToFile(myCampName, suggestions);
        CampSlot.updateCampSlotToFile(myCampName, myCamp.getCampSlot());
        if (action.equals("edit"))
            System.out.println("Suggestion updated.");
        else
            System.out.println("Suggestion deleted");
    }
    private void submitSuggestion() {
        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Suggestion> suggestions = myCamp.getSuggestions();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new suggestion: ");
        suggestions.add(Suggestion.createSuggestion(getUserID(), sc.nextLine()));

        // add committee point
        int point = 1 + myCamp.getCommitteePoint(getUserID());
        myCamp.setCommitteePoint(getUserID(), point);

        Suggestion.updateSuggestionsToFile(myCampName, suggestions);
        CampSlot.updateCampSlotToFile(myCampName, myCamp.getCampSlot());
        System.out.println("Suggestion submitted.");
    }
    private void viewEnquiries() {
        System.out.printf("%-10s | %-30s | %s\n",
            "Index", "Enquiry", "Response"
        );

        ArrayList<Enquiry> enquiries = AppUtil.readCamps()
            .getCamp(myCampName)
            .getEnquiries();
        for (int index = 0; index < enquiries.size(); index++) {
            Enquiry enquiry = enquiries.get(index);
            String response = (enquiry.isProcessed()) ?
                enquiry.getAnswer().getContent() : "-";
            System.out.printf("%-10s | %-30s | %s\n",
                index, enquiry.getQuestion().getContent(), response
            );
        }
    }
    private void answerEnquiry() throws InvalidUserInputException {
        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Enquiry> enquiries = myCamp.getEnquiries();

        Scanner sc = new Scanner(System.in);
        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        Enquiry enquiry;
        try {
            enquiry = enquiries.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidUserInputException("Invalid index.");
        }

        if (enquiry.isProcessed())
            throw new InvalidUserInputException("This enquiry is already processed.");

        System.out.print("Enter response for the chosen enquiry: ");
        String answerContent = sc.nextLine();
        enquiry.setAnswer(getUserID(), answerContent);

        // add committee point
        int point = 1 + myCamp.getCommitteePoint(getUserID());
        myCamp.setCommitteePoint(getUserID(), point);

        Enquiry.updateEnquiriesToFile(myCampName, enquiries);
        CampSlot.updateCampSlotToFile(myCampName, myCamp.getCampSlot());
        System.out.println("Response submitted.");
    }
    private void generateReport() throws InvalidUserInputException {
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
        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);

        // write camp details
        List<String> lines = new ArrayList<>();
        lines.add("| Camp Report | Filter: " + filter);
        lines.add("Name: " + myCamp.getName());
        lines.add("Description: " + myCamp.getDescription());
        lines.add("Dates: " +
            CampDates.getDateAsString(myCamp.getCampDates().getStartDate()) +
            " - " +
            CampDates.getDateAsString(myCamp.getCampDates().getEndDate())
        );
        lines.add("Registration deadline: " +
            CampDates.getDateAsString(myCamp.getCampDates().getRegistrationDeadline())
        );
        lines.add("Faculty: " + myCamp.getFaculty());
        lines.add("Location: " + myCamp.getLocation());
        lines.add("Total slot: " + myCamp.getTotalSlotAsString());
        lines.add("Committee slot: " + myCamp.getCommitteeSlotAsString());

        // write committees & attendees
        if (showCommittee) {
            lines.add("Committees: ");
            for (String committee : myCamp.getCampSlot().getCommittees().keySet()) {
                lines.add("\t" + committee);
            }
        }
        if (showAttendee) {
            lines.add("Attendees: ");
            for (String attendee : myCamp.getCampSlot().getAttendees()) {
                lines.add("\t" + attendee);
            }
        }

        // write to file
        String reportFileName = "report/" + myCampName + "_" + filter + ".txt";
        AppUtil.overwriteTXT(reportFileName, lines);
        System.out.println("Report generated as " + reportFileName);
    }
}
