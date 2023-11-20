package userpage.student;

import camp.Camp;
import camp.CampSlot;
import camp.Faculty;
import camp.convo.Enquiry;
import camp.convo.Suggestion;
import com.opencsv.exceptions.CsvException;
import userpage.ApplicationPage;
import userpage.User;
import util.AppUtil;
import util.exceptions.InvalidEnquiryException;
import util.exceptions.InvalidSuggestionException;
import util.exceptions.PageTerminatedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CampCommitteePage extends User implements ApplicationPage {
    private final String myCampName;
    public CampCommitteePage(String userID, String email, String name, Faculty faculty, String myCampName) {
        super(userID, email, name, faculty);
        this.myCampName = myCampName;
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
                    viewMySuggestions();
                    break;
                case ("3"):
                    try {
                        changeSuggestion();
                    } catch (InvalidSuggestionException e) {
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
                    } catch (InvalidEnquiryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ("7"):
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
        System.out.println("\t6. Answer enquiry");// view/answer to all enquiries of my camp
        System.out.println("\t7. Generate report");
        System.out.println("\t8. Return to previous page");
    }
    private void viewMySuggestions() throws IOException, CsvException {
        System.out.println("[Only unprocessed suggestions are shown]");
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
    private void changeSuggestion() throws InvalidSuggestionException, IOException, CsvException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose action [edit, delete]: ");
        String action = sc.nextLine();
        if (!action.equals("edit") && !action.equals("delete")) {
            throw new InvalidSuggestionException("Invalid action choice.");
        }

        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Suggestion> suggestions = myCamp.getSuggestions();

        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidSuggestionException();
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
        Suggestion suggestion;
        try {
            if (action.equals("edit")) {
                suggestion = suggestions.get(realIndex);
                System.out.print("Enter new suggestion: ");
                suggestion.setContent(sc.nextLine());
            } else {
                suggestions.remove(realIndex);
                // decrease committee point
                int point = myCamp.getCommitteePoint(getUserID()) - 1;
                myCamp.setCommitteePoint(getUserID(), point);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidSuggestionException();
        }

        Suggestion.updateSuggestionsToFile(myCampName, suggestions);
        CampSlot.updateCampSlotToFile(myCampName, myCamp.getCampSlot());
        if (action.equals("edit"))
            System.out.println("Suggestion updated.");
        else
            System.out.println("Suggestion deleted");
    }
    private void submitSuggestion() throws IOException, CsvException {
        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Suggestion> suggestions = myCamp.getSuggestions();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new suggestion: ");
        suggestions.add(new Suggestion(getUserID(), sc.nextLine()));

        // add committee point
        int point = 1 + myCamp.getCommitteePoint(getUserID());
        myCamp.setCommitteePoint(getUserID(), point);

        Suggestion.updateSuggestionsToFile(myCampName, suggestions);
        CampSlot.updateCampSlotToFile(myCampName, myCamp.getCampSlot());
        System.out.println("Suggestion submitted.");
    }
    private void viewEnquiries() throws IOException, CsvException {
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
    private void answerEnquiry() throws InvalidEnquiryException, IOException, CsvException {
        Camp myCamp = AppUtil.readCamps().getCamp(myCampName);
        ArrayList<Enquiry> enquiries = myCamp.getEnquiries();

        Scanner sc = new Scanner(System.in);
        System.out.print("Choose index: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidEnquiryException();
        }

        Enquiry enquiry;
        try {
            enquiry = enquiries.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidEnquiryException();
        }

        if (enquiry.isProcessed())
            throw new InvalidEnquiryException("This enquiry is already processed.");

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
}
