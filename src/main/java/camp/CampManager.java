package camp;

import camp.chat.EnquiryListManager;
import camp.chat.SuggestionListManager;
import camp.dates.CampDatesManager;
import camp.meta.Faculty;
import camp.meta.Location;
import camp.meta.MetaDataManager;
import camp.slots.CampSlotsManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class CampManager {
    private final Camp camp;
    private CampManager(Camp camp) {
        this.camp = camp;
    }
    public String getName() { return camp.getName(); }
    public MetaDataManager getMetaDataManager() { return MetaDataManager.createInstance(camp.getMetaData()); }
    public CampSlotsManager getSlotsManager() { return CampSlotsManager.createInstance(camp.getCampSlots()); }
    public CampDatesManager getDatesManager() { return CampDatesManager.createInstance(camp.getCampDates()); }
    public EnquiryListManager getEnquiryManager() { return EnquiryListManager.createInstance(camp.getEnquiries()); }
    public SuggestionListManager getSuggestionManager() { return SuggestionListManager.createInstance(camp.getSuggestions()); }
    public void setMetadataManager(MetaDataManager metaData) { camp.setMetaData(metaData.unbindManager());}
    public void setSlotsManager(CampSlotsManager campSlots) { camp.setCampSlots(campSlots.unbindManager()); }
    public void setDatesManager(CampDatesManager campDates) { camp.setCampDates(campDates.unbindManager()); }
    public void setEnquiryManager(EnquiryListManager enquiries) { camp.setEnquiries(enquiries.unbindManager()); }
    public void setSuggestionManager(SuggestionListManager suggestions) { camp.setSuggestions(suggestions.unbindManager()); }

    // static methods
    private static CampManager createInstance(Camp camp) {
        return new CampManager(camp);
    }
    public static CampManager createInstance(String name) {
        Camp camp = new Camp(
            name,
            null,
            null,
            null,
            new ArrayList<>(),
            new ArrayList<>()
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
            name,
            MetaDataManager.createInstance(staffID, description, visibility, faculty, location).unbindManager(),
            CampSlotsManager.createInstance(totalSlot, committeeSlot).unbindManager(),
            CampDatesManager.createInstance(startDate, endDate, registrationDeadline).unbindManager(),
            new ArrayList<>(),
            new ArrayList<>()
        );
        return CampManager.createInstance(camp);
    }
}
