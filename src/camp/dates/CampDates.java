package camp.dates;

import camp.CampData;

import java.time.LocalDate;

/**
 * class that represent the camp information of dates
 * @author Anqi
 * @date 2023/11/22
 */
public class CampDates implements CampData {
    private LocalDate startDate, endDate, registrationDeadline;

    /**
     * the constructor of the camp information of dates
     * @param startDate
     * @param endDate
     * @param registrationDeadline
     */
    CampDates(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDeadline = registrationDeadline;
    }

    /**
     * get Start Date of the camp
     * @return Start Date of the camp
     */
    LocalDate getStartDate() {
        return startDate;
    }

    /**
     * get end Date of the camp
     * @return end Date of the camp
     */
    LocalDate getEndDate() {
        return endDate;
    }

    /**
     * get Registration Deadline of the camp
     * @return Registration Deadline of the camp
     */
    LocalDate getRegistrationDeadline() {
        return registrationDeadline;
    }
}
