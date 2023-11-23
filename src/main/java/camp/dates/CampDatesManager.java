package camp.dates;

import app.CAMsApp;
import camp.NormalTypeManager;
import util.AppUtil;

import java.time.LocalDate;
import java.util.List;

public class CampDatesManager implements NormalTypeManager {
    private CampDates campDates;
    private CampDatesManager(CampDates campDates) {
        this.campDates = campDates;
    }
    public LocalDate getStartDate() { return campDates.getStartDate(); }
    public LocalDate getEndDate() {
        return campDates.getEndDate();
    }
    public LocalDate getRegistrationDeadline() {
        return campDates.getRegistrationDeadline();
    }
    public boolean containsDate(LocalDate date) {
        LocalDate thisStarts = campDates.getStartDate();
        LocalDate thisEnds = campDates.getEndDate();
        if (date.isBefore(thisStarts))
            return false;
        return !date.isAfter(thisEnds);
    }
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
    @Override
    public CampDates unbindManager() { return campDates; }
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

    // static methods
    public static CampDatesManager createInstance(CampDates campDates) {
        return new CampDatesManager(campDates);
    }
    public static CampDatesManager createInstance(LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        CampDates campDates = new CampDates(startDate, endDate, registrationDeadline);
        return new CampDatesManager(campDates);
    }
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
