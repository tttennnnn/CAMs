package util;

import app.CAMsApp;
import camp.*;
import camp.convo.Enquiry;
import camp.convo.Suggestion;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.codec.digest.DigestUtils;
import userpage.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AppUtil {

    public static String sha256(String s) { return DigestUtils.sha256Hex(s); }
    private static FileWriter getFileWriter(String fileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println("Error: \"" + fileName + "\". (AppUtil.getFileWriter)");
            System.exit(1);
        }
        return fileWriter;
    }
    public static void overwriteTXT(String fileName, List<String> lines) {
        FileWriter fileWriter = AppUtil.getFileWriter(fileName);
        try {
            for (String line : lines) {
                fileWriter.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void overwriteCSV(String fileName, List<String[]> data) {
        CSVWriter csvWriter = new CSVWriter(AppUtil.getFileWriter(fileName));
        csvWriter.writeAll(data);
        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static List<String[]> getDataFromCSV(String fileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: \"" + fileName + "\" not found. (AppUtil.getFileReader)");
            System.exit(1);
        }

        CSVReader csvReader = new CSVReaderBuilder(fileReader).build();
        List<String[]> lines = null;
        try {
            lines = csvReader.readAll();
            csvReader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return lines;
    }
    public static void printSectionLine() {
        String sectionLine = String.join("", Collections.nCopies(30, "="));
        System.out.println(sectionLine);
    }

    public static UserList readUsers() {
        List<String[]> lines = AppUtil.getDataFromCSV(CAMsApp.getUserFile());
        UserList userList = new UserList();
        for (int row = 1; row < lines.size(); row++) {
            String[] line = lines.get(row);
            String id = line[0].split("@")[0];
            if (line[3].equals("1"))
                userList.putStudent(id, new User(id, line[0], line[1], Faculty.valueOf(line[2])));
            else if (line[3].equals("2"))
                userList.putStaff(id, new User(id, line[0], line[1], Faculty.valueOf(line[2])));
        }
        return userList;
    }
    public static CampList readCamps() {
        List<String[]> infoLines = AppUtil.getDataFromCSV(CAMsApp.getCampInfoFile());
        List<String[]> slotLines = AppUtil.getDataFromCSV(CAMsApp.getCampSlotFile());
        List<String[]> dateLines = AppUtil.getDataFromCSV(CAMsApp.getCampDateFile());
        List<String[]> enquiryLines = AppUtil.getDataFromCSV(CAMsApp.getCampEnquiryFile());
        List<String[]> suggestionLines = AppUtil.getDataFromCSV(CAMsApp.getCampSuggestionFile());

        // read from camps.csv
        CampList campList = new CampList();
        for (int row = 1; row < infoLines.size(); row++) {
            String[] infoLine = infoLines.get(row);
            String campName = infoLine[0];
            String staffID = infoLine[1];
            String description = infoLine[2];
            boolean visibility = infoLine[3].equals("T");
            Faculty faculty = Faculty.valueOf(infoLine[4]);
            Location location = Location.valueOf(infoLine[5]);

            campList.putCamp(
                campName,
                new Camp(campName, staffID, description, visibility, faculty, location, null, null, null, null)
            );
        }

        // read from slots.csv
        for (int row = 1; row < slotLines.size(); row++) {
            String[] slotLine = slotLines.get(row);
            CampSlot campSlot = new CampSlot(
                Integer.parseInt(slotLine[1]),
                CampSlot.getAttendeeListAsSet(slotLine[2]),
                CampSlot.getCommitteeListAsMap(slotLine[3]),
                CampSlot.getWithdrawnListAsSet(slotLine[4])
            );
            campList.getCamp(slotLine[0]).setCampSlot(campSlot);
        }

        // read from dates.csv
        for (int row = 1; row < dateLines.size(); row++) {
            String[] dateLine = dateLines.get(row);
            CampDates campDates = new CampDates(
                CampDates.getDateAsLocalDate(dateLine[1]),
                CampDates.getDateAsLocalDate(dateLine[2]),
                CampDates.getDateAsLocalDate(dateLine[3])
            );
            campList.getCamp(dateLine[0]).setDates(campDates);
        }

        // read from enquiries.csv
        for (int row = 1; row < enquiryLines.size(); row++) {
            String[] enquiryLine = enquiryLines.get(row);
            ArrayList<Enquiry> enquiries = Enquiry.getEnquiryListAsArrayList(
                Arrays.copyOfRange(enquiryLine, 1, enquiryLine.length)
            );
            campList.getCamp(enquiryLine[0]).setEnquiries(enquiries);
        }

        // read from suggestions.csv
        for (int row = 1; row < suggestionLines.size(); row++) {
            String[] suggestionLine = suggestionLines.get(row);
            ArrayList<Suggestion> suggestions = Suggestion.getSuggestionListAsArrayList(
                Arrays.copyOfRange(suggestionLine, 1, suggestionLine.length)
            );
            campList.getCamp(suggestionLine[0]).setSuggestions(suggestions);
        }

        return campList;
    }
}
