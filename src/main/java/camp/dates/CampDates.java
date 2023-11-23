package camp.dates;

import camp.CampData;

import java.time.LocalDate;

public class CampDates implements CampData {
    private LocalDate startDate, endDate, registrationDeadline;
    CampDates(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDeadline = registrationDeadline;
    }
    LocalDate getStartDate() {
        return startDate;
    }
    LocalDate getEndDate() {
        return endDate;
    }
    LocalDate getRegistrationDeadline() {
        return registrationDeadline;
    }
}
