package camp;

import app.CAMsApp;
import camp.chat.EnquiryListManager;
import camp.chat.SuggestionListManager;
import camp.dates.CampDatesManager;
import camp.slots.CampSlotsManager;
import util.AppUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampManager implements NormalTypeManager {
    private final Camp camp;
    private CampManager(Camp camp) {
        this.camp = camp;
    }
    public String getName() { return camp.getName(); }
    public String getStaffID() { return camp.getStaffID(); }
    public String getDescription() { return camp.getDescription(); }
    public boolean getVisibility() { return camp.getVisibility(); }
    public Faculty getFaculty() { return camp.getFaculty(); }
    public Location getLocation() { return camp.getLocation(); }
    public void setCampSlots(CampSlotsManager campSlots) { camp.setCampSlots(campSlots.unbindManager()); }
    public void setCampDates(CampDatesManager campDates) { camp.setCampDates(campDates.unbindManager()); }
    public void setEnquiries(EnquiryListManager enquiries) { camp.setEnquiries(enquiries.unbindManager()); }
    public void setSuggestions(SuggestionListManager suggestions) { camp.setSuggestions(suggestions.unbindManager()); }
    public CampSlotsManager getSlotsManager() { return CampSlotsManager.createInstance(camp.getCampSlots()); }
    public CampDatesManager getDatesManager() { return CampDatesManager.createInstance(camp.getCampDates()); }
    public EnquiryListManager getEnquiryManager() { return EnquiryListManager.createInstance(camp.getEnquiries()); }
    public SuggestionListManager getSuggestionManager() { return SuggestionListManager.createInstance(camp.getSuggestions()); }
    public void setLocation(Location location) { camp.setLocation(location); }
    public void setDescription(String description) { camp.setDescription(description); }
    public void toggleVisibility() { camp.toggleVisibility(); }
    @Override
    public Camp unbindManager() { return camp; }
    @Override
    public void updateToFile(String campName) {
        String[] newInfoLine = new String[]{
            campName,
            getStaffID(),
            getDescription(),
            (getVisibility()) ? "T" : "F",
            getFaculty().name(),
            getLocation().name()
        };
        List<String[]> infoLines = AppUtil.getDataFromCSV(CAMsApp.getCampInfoFile());
        int row;
        for (row = 1; row < infoLines.size(); row++) {
            String[] infoLine = infoLines.get(row);
            if (infoLine[0].equals(campName)) {
                infoLines.set(row, newInfoLine);
                break;
            }
        }
        if (row >= infoLines.size()) {
            infoLines.add(newInfoLine);
        }
        AppUtil.overwriteCSV(CAMsApp.getCampInfoFile(), infoLines);
    }

    // static methods
    private static CampManager createInstance(Camp camp) {
        return new CampManager(camp);
    }
    public static CampManager createInstance(String name, String staffID, String description,
                                     boolean visibility,
                                     Faculty faculty,
                                     Location location) {
        Camp camp = new Camp(
            name, staffID, description,
            visibility,
            faculty,
            location,
            null,
            null,
            new ArrayList<>(), new ArrayList<>()
        );
        return CampManager.createInstance(camp);
    }
    public static CampManager createInstance(String name, String staffID, String description,
                                  boolean visibility,
                                  Faculty faculty,
                                  Location location,
                                  int totalSlot, int committeeSlot,
                                  LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline) {
        Camp camp = new Camp(
            name, staffID, description,
            visibility,
            faculty,
            location,
            CampSlotsManager.createInstance(totalSlot, committeeSlot).unbindManager(),
            CampDatesManager.createInstance(startDate, endDate, registrationDeadline).unbindManager(),
            new ArrayList<>(), new ArrayList<>()
        );
        return CampManager.createInstance(camp);
    }
}
