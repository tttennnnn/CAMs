package camp;

import camp.chat.Enquiry;
import camp.chat.Suggestion;
import camp.dates.CampDates;
import camp.meta.MetaData;
import camp.slots.CampSlots;

import java.util.ArrayList;

/**
 * class that represent the information of camp
 * @author Anqi
 * @date 2023/11/22
 */
class Camp {
    /**
     * name of camp
     */
    private final String campName;
    /**
     * the information of camp
     */
    private MetaData metaData;
    /**
     * slots of camp
     */
    private CampSlots campSlots;
    /**
     * dates information of camp
     */
    private CampDates campDates;
    /**
     * list of enquiries
     */
    private ArrayList<Enquiry> enquiries;
    /**
     * list of suggestions
     */
    private ArrayList<Suggestion> suggestions;

    /**
     * constructor of camp
     * @param campName
     * @param metaData
     * @param campSlots
     * @param campDates
     * @param enquiries
     * @param suggestions
     */
    Camp(String campName, MetaData metaData, CampSlots campSlots, CampDates campDates, ArrayList<Enquiry> enquiries, ArrayList<Suggestion> suggestions) {
        this.campName = campName;
        this.metaData = metaData;
        this.campSlots = campSlots;
        this.campDates = campDates;
        this.enquiries = enquiries; this.suggestions = suggestions;
    }

    /**
     * get name of camp
     * @return name of camp
     */// getters
    String getName() { return campName; }

    /**
     * get the camp information
     * @return camp information
     */
    MetaData getMetaData() { return metaData; }

    /**
     * get the slot of camp
     * @return slot of camp
     */
    CampSlots getCampSlots() { return campSlots; }

    /**
     * get date information of camp
     * @return date information of camp
     */
    CampDates getCampDates() { return campDates; }

    /**
     * get list of enquiries
     * @return list of enquiries
     */
    ArrayList<Enquiry> getEnquiries() { return enquiries; }

    /**
     * get list of suggestions
     * @return list of suggestions
     */
    ArrayList<Suggestion> getSuggestions() { return suggestions; }

    /**
     * set the data information of camp
     * @param metaData
     */// setters
    void setMetaData(MetaData metaData) { this.metaData = metaData; }

    /**
     * set slot of camps
     * @param campSlot
     */
    void setCampSlots(CampSlots campSlot) {
        this.campSlots = campSlot;
    }

    /**
     * set date information of camps
     * @param campDates
     */
    void setCampDates(CampDates campDates) {
        this.campDates = campDates;
    }

    /**
     * set enquiries of camp
     * @param enquiries
     */
    void setEnquiries(ArrayList<Enquiry> enquiries) { this.enquiries = enquiries; }

    /**
     * set suggestions of camp
     * @param suggestions
     */
    void setSuggestions(ArrayList<Suggestion> suggestions) { this.suggestions = suggestions; }
}