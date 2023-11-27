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

/**
 * class that represent the camp manager
 * @author Anqi
 * @date 2023/11/22
 */
public class CampManager {
    /**
     * camp
     */
    private final Camp camp;

    /**
     * constructor of camp
     * @param camp
     */
    private CampManager(Camp camp) {
        this.camp = camp;
    }

    /**
     * get name of camp
     * @return name of camp
     */
    public String getName() { return camp.getName(); }

    /**
     * get the camp information
     * @return camp information
     */
    public MetaDataManager getMetaDataManager() { return MetaDataManager.createInstance(camp.getMetaData()); }

    /**
     * get the slot of camp
     * @return slot of camp
     */
    public CampSlotsManager getSlotsManager() { return CampSlotsManager.createInstance(camp.getCampSlots()); }

    /**
     * get date information of camp
     * @return date information of camp
     */
    public CampDatesManager getDatesManager() { return CampDatesManager.createInstance(camp.getCampDates()); }

    /**
     * get list of enquiries
     * @return list of enquiries
     */
    public EnquiryListManager getEnquiryManager() { return EnquiryListManager.createInstance(camp.getEnquiries()); }

    /**
     * get list of suggestions
     * @return list of suggestions
     */
    public SuggestionListManager getSuggestionManager() { return SuggestionListManager.createInstance(camp.getSuggestions()); }

    /**
     * set the data information of camp
     * @param metaData
     */
    public void setMetadataManager(MetaDataManager metaData) { camp.setMetaData(metaData.unbindManager());}

    /**
     * set slot of camps
     * @param campSlots
     */
    public void setSlotsManager(CampSlotsManager campSlots) { camp.setCampSlots(campSlots.unbindManager()); }

    /**
     * set date information of camps
     * @param campDates
     */
    public void setDatesManager(CampDatesManager campDates) { camp.setCampDates(campDates.unbindManager()); }

    /**
     * set enquiries of camp
     * @param enquiries
     */
    public void setEnquiryManager(EnquiryListManager enquiries) { camp.setEnquiries(enquiries.unbindManager()); }

    /**
     * set suggestions of camp
     * @param suggestions
     */
    public void setSuggestionManager(SuggestionListManager suggestions) { camp.setSuggestions(suggestions.unbindManager()); }

    /**
     * create instance of camp
     * @param camp
     * @return new camp information
     */// static methods
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

    /**
     * create instance of camp(name, staffID, description, visibility, faculty, location, totalSlot, int committeeSlot,nstartDate, endDate, registrationDeadline)      * @param name
     * @param staffID
     * @param description
     * @param visibility
     * @param faculty
     * @param location
     * @param totalSlot
     * @param committeeSlot
     * @param startDate
     * @param endDate
     * @param registrationDeadline
     * @return new camp information
     */
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
