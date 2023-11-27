package camp.dates;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * class that represents the formatter of camp dates
 * @author Anqi
 * @date 2023/11/22
 */
public class CampDatesFormatter {
    /**
     * get dates information of camp As String
     * @param date
     * @return {@link String}
     */
    public static String getDateAsString(LocalDate date) {
        return date.getYear() + "/" + date.getMonth().getValue() + "/" + date.getDayOfMonth();
    }

    /**
     * get Date As Local Date
     * @param date
     * @return Date As Local Date
     * @throws IllegalArgumentException
     */
    public static LocalDate getDateAsLocalDate(String date) throws IllegalArgumentException {
        String[] dateArr = date.split("/");
        LocalDate res;
        try {
            res = LocalDate.of(Integer.parseInt(dateArr[0]),
                Integer.parseInt(dateArr[1]),
                Integer.parseInt(dateArr[2])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date.");
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return res;
    }
}
