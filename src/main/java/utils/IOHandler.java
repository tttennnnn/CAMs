package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IOHandler {
    public static String sha256(String s) { return DigestUtils.sha256Hex(s); }
    public static FileReader getFileReader(String fileName) throws FileNotFoundException {
        return new FileReader(fileName);
    }
    public static FileWriter getFileWriter(String fileName) throws IOException {
        return new FileWriter(fileName);
    }
    public static void overwriteCSV(String fileName, List<String[]> newData) throws IOException{
        CSVWriter csvWriter = new CSVWriter(getFileWriter(fileName),
                                            CSVWriter.DEFAULT_SEPARATOR,
                                            CSVWriter.NO_QUOTE_CHARACTER,
                                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                            CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeAll(newData);
        csvWriter.flush();
        csvWriter.close();
    }
}
