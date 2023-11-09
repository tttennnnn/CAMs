package camp;

import util.CampList;

import java.time.LocalDate;

public class Camp {
    private String name, staffID, description;
    private CampSlot campSlot;
    private CampDates dates;
    private boolean visibility;
    private Faculty faculty;
    private Location location;

    public Camp(String name, String staffID, String description,
                CampSlot campSlot,
                CampDates dates,
                boolean visibility,
                Faculty faculty,
                Location location) {
        this.name = name;
        this.staffID = staffID;
        this.description = description;
        this.campSlot = campSlot;
        this.dates = dates;
        this.visibility = visibility;
        this.faculty = faculty;
        this.location = location;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CampSlot getCampSlot() { return campSlot; }
    public CampDates getCampDates() { return dates; }
    public boolean getVisibility() { return visibility; }
    public Faculty getFaculty() { return faculty; }
    public Location getLocation() { return location; }

    public int getTotalVacancy() {
        return campSlot.getMaxTotal() - campSlot.getStudents().size() - campSlot.getCommittees().size();
    }
    public int getCommitteeVacancy() {
        return campSlot.getMaxCommittee() - campSlot.getCommittees().size();
    }
    public String getTotalSlotAsString() {
        return (campSlot.getStudents().size() + campSlot.getCommittees().size()) + "/" + campSlot.getMaxTotal();
    }
    public String getCommitteeSlotAsString() {
        return campSlot.getCommittees().size() + "/" + campSlot.getMaxCommittee();
    }
    public String getStatus(String key) {
        if (campSlot.getStudents().contains(key))
            return "Attendee";
        if (campSlot.getCommittees().contains(key))
            return "Committee";
        return "-";
    }
    public boolean hasTimeClash(Camp anotherCamp) {
        LocalDate thisStarts = dates.getStartDate();
        LocalDate thisEnds = dates.getEndDate();
        LocalDate anotherStarts = anotherCamp.getCampDates().getStartDate();
        LocalDate anotherEnds = anotherCamp.getCampDates().getEndDate();
        if (thisStarts.isBefore(anotherStarts) && !thisEnds.isBefore(anotherStarts))
            return true;
        if (thisStarts.isEqual(anotherStarts))
            return true;
        return !thisStarts.isAfter(anotherEnds);
    }
    public boolean hasWithdrawn(String ID) { return campSlot.getWithdrawns().contains(ID); }
    public void addStudent(String student) {
        campSlot.getStudents().add(student);
    }
    public void addCommittee(String committee) { campSlot.getCommittees().add(committee); }

    public void setCampSlot(CampSlot campSlot) {
        this.campSlot = campSlot;
    }
    public void setDates(CampDates dates) {
        this.dates = dates;
    }
    public void toggleVisibility() { visibility = !visibility; }

    public static Camp createCamp(String name, String staffID, String description,
                                  int totalNumSlot,
                                  LocalDate startDate, LocalDate endDate, LocalDate registrationDeadline,
                                  boolean visibility,
                                  Faculty faculty,
                                  Location location){
        return new Camp(name, staffID, description,
                        new CampSlot(totalNumSlot),
                        new CampDates(startDate, endDate, registrationDeadline),
                        visibility,
                        faculty,
                        location);
    }
}