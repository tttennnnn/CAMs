package camp;

import camp.chat.Enquiry;
import camp.chat.Suggestion;
import camp.dates.CampDates;
import camp.meta.MetaData;
import camp.slots.CampSlots;

import java.util.ArrayList;

class Camp {
    private final String campName;
    private MetaData metaData;
    private CampSlots campSlots;
    private CampDates campDates;
    private ArrayList<Enquiry> enquiries;
    private ArrayList<Suggestion> suggestions;
    Camp(String campName, MetaData metaData, CampSlots campSlots, CampDates campDates, ArrayList<Enquiry> enquiries, ArrayList<Suggestion> suggestions) {
        this.campName = campName;
        this.metaData = metaData;
        this.campSlots = campSlots;
        this.campDates = campDates;
        this.enquiries = enquiries; this.suggestions = suggestions;
    }
    // getters
    String getName() { return campName; }
    MetaData getMetaData() { return metaData; }
    CampSlots getCampSlots() { return campSlots; }
    CampDates getCampDates() { return campDates; }
    ArrayList<Enquiry> getEnquiries() { return enquiries; }
    ArrayList<Suggestion> getSuggestions() { return suggestions; }
    // setters
    void setMetaData(MetaData metaData) { this.metaData = metaData; }
    void setCampSlots(CampSlots campSlot) {
        this.campSlots = campSlot;
    }
    void setCampDates(CampDates campDates) {
        this.campDates = campDates;
    }
    void setEnquiries(ArrayList<Enquiry> enquiries) { this.enquiries = enquiries; }
    void setSuggestions(ArrayList<Suggestion> suggestions) { this.suggestions = suggestions; }
}