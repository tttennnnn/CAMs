package userpage;

import util.exceptions.PageTerminatedException;

/**
 * interface of application page
 * @author Anqi
 * @date 2023/11/22
 */
public interface ApplicationPage {
    /**
     * the (user interface) UI
     * @throws PageTerminatedException
     */
    void runPage() throws PageTerminatedException;

    /**
     * print the header for users to know which page they are using now
     */
    void printHeader();

    /**
     * print down the operation options for user
     */
    void showUsage();
}
