package camp;

import camp.convo.Enquiry;
import camp.convo.Suggestion;

import java.time.LocalDate;
import java.util.ArrayList;

public class Camp {
    private String name, staffID, description;
    private boolean visibility;
    private Faculty faculty;
    private Location location;
    private CampSlot campSlot;
    private CampDates dates;
    private ArrayList<Enquiry> enquiries;
    private ArrayList<Suggestion> suggestions;
    public Camp(String name, String staffID, String description,
                boolean visibility,
                Faculty faculty,
                Location location,
                CampSlot campSlot,
                CampDates dates,
                ArrayList<Enquiry> enquiries, ArrayList<Suggestion> suggestions) {
        this.name = name; this.staffID = staffID; this.description = description;
        this.visibility = visibility;
        this.faculty = faculty;
        this.location = location;
        this.campSlot = campSlot;
        this.dates = dates;
        this.enquiries = enquiries; this.suggestions = suggestions;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CampSlot getCampSlot() { return campSlot; }
    public CampDates getCampDates() { return dates; }
    public boolean getVisibility() { return visibility; }
    public Faculty getFaculty() { return faculty; }
    public Location getLocation() { return location; }

    public int getTotalVacancy() {
        return campSlot.getMaxTotal() - campSlot.getAttendees().size() - campSlot.getCommittees().size();
    }
    public int getCommitteeVacancy() {
        return campSlot.getMaxCommittee() - campSlot.getCommittees().size();
    }
    public String getTotalSlotAsString() {
        return (campSlot.getAttendees().size() + campSlot.getCommittees().size()) + "/" + campSlot.getMaxTotal();
    }
    public String getCommitteeSlotAsString() {
        return campSlot.getCommittees().size() + "/" + campSlot.getMaxCommittee();
    }
    public String getCampStatus(String key) {
        if (campSlot.getAttendees().contains(key))
            return "Attendee";
        if (campSlot.getCommittees().containsKey(key))
            return "Committee";
        if (campSlot.getWithdrawns().contains(key))
            return "Withdrawn";
        return "-";
    }
    public boolean hasTimeClash(Camp anotherCamp) {
        LocalDate thisStarts = dates.getStartDate();
        LocalDate thisEnds = dates.getEndDate();
        LocalDate anotherStarts = anotherCamp.getCampDates().getStartDate();
        LocalDate anotherEnds = anotherCamp.getCampDates().getEndDate();
        if (thisStarts.isBefore(anotherStarts))
            return !thisEnds.isBefore(anotherStarts);
        if (thisStarts.isEqual(anotherStarts))
            return true;
        return !thisStarts.isAfter(anotherEnds);
    }
    public boolean hasWithdrawn(String ID) { return campSlot.getWithdrawns().contains(ID); }
    public void addAttendee(String student) { campSlot.getAttendees().add(student); }
    public void addCommittee(String committee, int point) { campSlot.getCommittees().put(committee, point); }

    public void setCampSlot(CampSlot campSlot) {
        this.campSlot = campSlot;
    }
    public void setDates(CampDates dates) {
        this.dates = dates;
    }
    public void setEnquiries(ArrayList<Enquiry> enquiries) { this.enquiries = enquiries; }
    public void setSuggestions(ArrayList<Suggestion> suggestions) { this.suggestions = suggestions; }
}