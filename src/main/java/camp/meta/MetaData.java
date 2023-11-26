package camp.meta;

import camp.CampData;

public class MetaData implements CampData {
    private final String staffID;
    private String description;
    private boolean visibility;
    private final Faculty faculty;
    private Location location;
    MetaData(String staffID, String description, boolean visibility, Faculty faculty, Location location) {
        this.staffID = staffID;
        this.description = description;
        this.visibility = visibility;
        this.faculty = faculty;
        this.location = location;
    }
    String getStaffID() { return staffID; }
    String getDescription() { return description; }
    boolean isVisible() { return visibility; }
    Faculty getFaculty() { return faculty; }
    Location getLocation() { return location; }
    void setLocation(Location location) { this.location = location; }
    void setDescription(String description) { this.description = description; }
    void setVisibility(boolean visibility) { this.visibility = visibility; }
}
