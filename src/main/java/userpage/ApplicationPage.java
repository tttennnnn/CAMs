package userpage;

import com.opencsv.exceptions.CsvException;
import util.exceptions.PageTerminatedException;

import java.io.IOException;

public interface ApplicationPage {
    void runPage() throws PageTerminatedException, IOException, CsvException;
    void printHeader();
    void showUsage();
}
