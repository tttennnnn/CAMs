package camp.dates;

import app.CAMsApp;
import camp.NormalTypeManager;
import util.AppUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * class that represents the camp dates manager
 * @author Anqi
 * @date 2023/11/22
 */
public class CampDatesManager implements NormalTypeManager {
    /**
     * camp dates
     */
    private CampDates campDates;

    /**
     * constructor of camp dates
     * @param campDates
     */
    private CampDatesManager(CampDates campDates) {
        this.campDates = campDates;
    }

    /**
     * get start date of the camp
     * @return start date of the camp
     */
    public LocalDate getStartDate() { return campDates.getStartDate(); }

    /**
     * get end date of the camp
     * @return end date of the camp
     */
    public LocalDate getEndDate() {
        return campDates.getEndDate();
    }

    /**
     * get RegistrationDeadline of the camp
     * @return RegistrationDeadline of the camp
     */
    public LocalDate getRegistrationDeadline() {
        return campDates.getRegistrationDeadline();
    }

    /**
     * whether the camp is still available to be registered
     * @param date
     * @return boolean
     */
    public boolean containsDate(LocalDate date) {
        LocalDate thisStarts = campDates.getStartDate();
        LocalDate thisEnds = campDates.getEndDate();
        if (date.isBefore(thisStarts))
            return false;
        return !date.isAfter(thisEnds);
    }

    /**
     * whether the camp has Time Clash with other registered camp
     * @param anotherDates
     * @return boolean
     */
    public boolean hasTimeClash(CampDatesManager anotherDates) {
        LocalDate thisStarts = campDates.getStartDate();
        LocalDate thisEnds = campDates.getEndDate();
        LocalDate anotherStarts = anotherDates.campDates.getStartDate();
        LocalDate anotherEnds = anotherDates.campDates.getEndDate();
        if (thisStarts.isBefore(anotherStarts))
            return !thisEnds.isBefore(anotherStarts);
        if (thisStarts.isEqual(anotherStarts))
            return true;
        return !thisStarts.isAfter(anotherEnds);
    }

    /**
     * unbind Manager for camp dates
     * @return campDates
     */
    @Override
    public CampDates unbindManager() { return campDates; }

    /**
     * update the camp dates to the data file
     * @param campName
     */
    @Override
    public void updateToFile(String campName) {
        List<String[]> dateLines = AppUtil.getDataFromCSV(CAMsApp.getCampDateFile());
        String[] newDateLine = new String[]{
            campName,
            CampDatesFormatter.getDateAsString(getStartDate()),
            CampDatesFormatter.getDateAsString(getEndDate()),
            CampDatesFormatter.getDateAsString(getRegistrationDeadline())
        };
        int row;
        for (row = 1; row < dateLines.size(); row++) {
            String[] dateLine = dateLines.get(row);
            if (dateLine[0].equals(campName)) {
                dateLines.set(row, newDateLine);
                break;
            }
        }
        // if campName does not exist in dateFile
        if (row >= dateLines.size()) {
            dateLines.add(newDateLine);
        }

        AppUtil.overwriteCSV(CAMsApp.getCampDateFile(), dateLines);
    }

    /**
     * create instance of camp dates
     * @param campDates
     * @return new campDates
     */// static methods
    public static CampDatesManager createInstance(CampDates campDates) {
        return new CampDatesManager(campDates);
    }

    /**
     * create instance(startDate, endDate, registrationDeadline) of camp dates
     * @param startDate
     * @param endDate
     * @param registrationDeadline
     * @return new campDates
     */
    public static CampDatesManager createInstance(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        CampDates campDates = new CampDates(startDate, endDate, registrationDeadline);
        return new CampDatesManager(campDates);
    }

    /**
     * see if the dates is valid
     * @param startDate
     * @param endDate
     * @param registrationDeadline
     * @throws IllegalArgumentException
     */
    public static void isValidDates(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) throws IllegalArgumentException {
        LocalDate now = LocalDate.now();
        if (!registrationDeadline.isAfter(now))
            throw new IllegalArgumentException("Registration deadline must be after today.");
        if (!startDate.isAfter(registrationDeadline))
            throw new IllegalArgumentException("Start date must be after registration deadline.");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must not be before start date.");
        }
    }
}
