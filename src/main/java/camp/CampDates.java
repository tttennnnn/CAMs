package camp;

import java.time.LocalDate;

public class CampDates {
    private LocalDate startDate, endDate, registrationDeadline;

    public CampDates(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDeadline = registrationDeadline;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public LocalDate getRegistrationDeadline() {
        return registrationDeadline;
    }

    public static String getDateAsString(LocalDate date) {
        return date.getYear() + "/" + date.getMonth().getValue() + "/" + date.getDayOfMonth();
    }
    public static LocalDate getDateAsLocalDate(String date) {
        String[] dateArr = date.split("/");
        return LocalDate.of(Integer.parseInt(dateArr[0]),
            Integer.parseInt(dateArr[1]),
            Integer.parseInt(dateArr[2]));
    }
}
