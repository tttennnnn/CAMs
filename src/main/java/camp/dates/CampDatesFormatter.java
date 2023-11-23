package camp.dates;

import java.time.DateTimeException;
import java.time.LocalDate;

public class CampDatesFormatter {
    public static String getDateAsString(LocalDate date) {
        return date.getYear() + "/" + date.getMonth().getValue() + "/" + date.getDayOfMonth();
    }
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
