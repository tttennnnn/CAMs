package camp;

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
    public CampDates getCampDates() { return dates; }
    public boolean getVisibility() { return visibility; }
    public Faculty getFaculty() { return faculty; }
    public Location getLocation() { return location; }

    public int getTotalVacancy() {
        return campSlot.getMaxTotal() - campSlot.getOccupiedTotal();
    }
    public int getCommitteeVacancy() {
        return campSlot.getMaxCommittee() - campSlot.getOccupiedCommittee();
    }
    public String getTotalSlotAsString() {
        return campSlot.getOccupiedTotal() + "/" + campSlot.getMaxTotal();
    }
    public String getCommitteeSlotAsString() {
        return campSlot.getOccupiedCommittee() + "/" + campSlot.getMaxCommittee();
    }
    public String getStatus(String key) {
        if (campSlot.hasStudent(key))
            return "Attendee";
        if (campSlot.hasCommittee(key))
            return "Committee";
        return "-";
    }

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