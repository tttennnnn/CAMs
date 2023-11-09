package util;

import app.CAMsApp;
import camp.*;
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
import java.time.LocalDate;
import java.util.*;

public class AppUtil {

    public static String sha256(String s) { return DigestUtils.sha256Hex(s); }
    public static FileReader getFileReader(String fileName) throws FileNotFoundException {
        return new FileReader(fileName);
    }
    public static FileWriter getFileWriter(String fileName) throws IOException {
        return new FileWriter(fileName);
    }
    public static CSVReader getCSVReader(String fileName) throws IOException {
        return new CSVReaderBuilder(getFileReader(fileName)).build();
    }
    public static CSVReader getCSVReader(String fileName, int n) throws IOException {
        return new CSVReaderBuilder(getFileReader(fileName))
            .withSkipLines(n).build();
    }
    public static void overwriteCSV(String fileName, List<String[]> newData) throws IOException {
        CSVWriter csvWriter = new CSVWriter(getFileWriter(fileName),
                                            CSVWriter.DEFAULT_SEPARATOR,
                                            CSVWriter.NO_QUOTE_CHARACTER,
                                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                            CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeAll(newData);
        csvWriter.flush();
        csvWriter.close();
    }
    public static void printSectionLine() {
        String sectionLine = String.join("", Collections.nCopies(30, "="));
        System.out.println(sectionLine);
    }

    public static UserList readUsers() throws IOException, CsvException {
        CSVReader csvReader = AppUtil.getCSVReader(CAMsApp.getUserFile(), 1);

        List<String[]> lines = csvReader.readAll();
        csvReader.close();

        UserList userList = new UserList();
        for (String[] line : lines) {
            String id = line[0].split("@")[0];
            if (line[3].equals("1"))
                userList.putStudent(id, new User(id, line[0], line[1], Faculty.valueOf(line[2])));
            else if (line[3].equals("2"))
                userList.putStaff(id, new User(id, line[0], line[1], Faculty.valueOf(line[2])));
        }

        return userList;
    }
    public static CampList readCamps() throws IOException, CsvException {
        CSVReader campInfoReader = AppUtil.getCSVReader(CAMsApp.getCampInfoFile(), 1);
        CSVReader campSlotReader = AppUtil.getCSVReader(CAMsApp.getCampSlotFile(), 1);
        CSVReader campDateReader = AppUtil.getCSVReader(CAMsApp.getCampDateFile(), 1);

        List<String[]> infoLines = campInfoReader.readAll();
        List<String[]> slotLines = campSlotReader.readAll();
        List<String[]> dateLines = campDateReader.readAll();

        campInfoReader.close(); campSlotReader.close(); campDateReader.close();

        // read from camps.csv
        CampList campList = new CampList();
        for (String[] infoLine : infoLines) {
            String campName = infoLine[0];
            String staffID = infoLine[1];
            String description = infoLine[2];
            boolean visibility = infoLine[3].equals("T");
            Faculty faculty = Faculty.valueOf(infoLine[4]);
            Location location = Location.valueOf(infoLine[5]);

            campList.putCamp(
                campName,
                new Camp(campName, staffID, description, null, null, visibility, faculty, location)
            );
        }

        // read from slots.csv
        for (String[] slotLine : slotLines) {
            CampSlot campSlot = new CampSlot(
                Integer.parseInt(slotLine[1]),
                CampSlot.getAttendeeListAsArrayList(slotLine[2]),
                CampSlot.getAttendeeListAsArrayList(slotLine[3]),
                CampSlot.getAttendeeListAsArrayList(slotLine[4])
            );
            campList.getCamp(slotLine[0]).setCampSlot(campSlot);
        }

        // read from dates.csv
        for (String[] dateLine : dateLines) {
            CampDates campDates = new CampDates(
                CampDates.getDateAsLocalDate(dateLine[1]),
                CampDates.getDateAsLocalDate(dateLine[2]),
                CampDates.getDateAsLocalDate(dateLine[3])
            );
            campList.getCamp(dateLine[0]).setDates(campDates);
        }

        return campList;
    }
}
