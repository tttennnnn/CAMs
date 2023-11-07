package util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
}
