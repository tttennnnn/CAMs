package userpage;

import util.exceptions.PageTerminatedException;

public interface ApplicationPage {
    void runPage() throws PageTerminatedException;
    void printHeader();
    void showUsage();
}
