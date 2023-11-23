package camp;

import camp.chat.Enquiry;
import camp.chat.Suggestion;
import camp.dates.CampDates;
import camp.slots.CampSlots;

import java.util.ArrayList;

class Camp implements CampData {
    private String name, staffID, description;
    private boolean visibility;
    private Faculty faculty;
    private Location location;
    private CampSlots campSlots;
    private CampDates campDates;
    private ArrayList<Enquiry> enquiries;
    private ArrayList<Suggestion> suggestions;
    Camp(String name, String staffID, String description,
                boolean visibility,
                Faculty faculty,
                Location location,
                CampSlots campSlots,
                CampDates campDates,
                ArrayList<Enquiry> enquiries, ArrayList<Suggestion> suggestions) {
        this.name = name; this.staffID = staffID; this.description = description;
        this.visibility = visibility;
        this.faculty = faculty;
        this.location = location;
        this.campSlots = campSlots;
        this.campDates = campDates;
        this.enquiries = enquiries; this.suggestions = suggestions;
    }
    String getName() { return name; }
    String getStaffID() { return staffID; }
    String getDescription() { return description; }
    CampSlots getCampSlots() { return campSlots; }
    CampDates getCampDates() { return campDates; }
    boolean getVisibility() { return visibility; }
    Faculty getFaculty() { return faculty; }
    Location getLocation() { return location; }
    ArrayList<Suggestion> getSuggestions() { return suggestions; }
    ArrayList<Enquiry> getEnquiries() { return enquiries; }
    void setCampSlots(CampSlots campSlot) {
        this.campSlots = campSlot;
    }
    void setCampDates(CampDates campDates) {
        this.campDates = campDates;
    }
    void setEnquiries(ArrayList<Enquiry> enquiries) { this.enquiries = enquiries; }
    void setSuggestions(ArrayList<Suggestion> suggestions) { this.suggestions = suggestions; }
    void toggleVisibility() { visibility = !visibility; }
    void setLocation(Location location) { this.location = location; }
    void setDescription(String description) { this.description = description; }


}