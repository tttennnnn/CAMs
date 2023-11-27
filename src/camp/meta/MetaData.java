package camp.meta;

import camp.CampData;

/**
 * class that represent the data information of camp
 * @author Anqi
 * @date 2023/11/22
 */
public class MetaData implements CampData {
    /**
     * ID of staff
     */
    private final String staffID;
    /**
     * the description of camp
     */
    private String description;
    /**
     * visibility of camp
     */
    private boolean visibility;
    /**
     * faculty of camp
     */
    private final Faculty faculty;
    /**
     * location of camp
     */
    private Location location;

    /**
     * constructor of metadata
     * @param staffID
     * @param description
     * @param visibility
     * @param faculty
     * @param location
     */
    MetaData(String staffID, String description, boolean visibility, Faculty faculty, Location location) {
        this.staffID = staffID;
        this.description = description;
        this.visibility = visibility;
        this.faculty = faculty;
        this.location = location;
    }

    /**
     * get the ID of staff
     * @return ID of staff
     */
    String getStaffID() { return staffID; }

    /**
     * get the description of camp
     * @return description of camp
     */
    String getDescription() { return description; }

    /**
     * see if camp is visible to user
     * @return boolean
     */
    boolean isVisible() { return visibility; }

    /**
     * get the faculty of camp
     * @return faculty of camp
     */
    Faculty getFaculty() { return faculty; }

    /**
     * get the location of camp
     * @return location of camp
     */
    Location getLocation() { return location; }

    /**
     * set the location of camp
     * @param location
     */
    void setLocation(Location location) { this.location = location; }

    /**
     * set the description of camp
     * @param description
     */
    void setDescription(String description) { this.description = description; }

    /**
     * set the visibility of camp
     * @param visibility
     */
    void setVisibility(boolean visibility) { this.visibility = visibility; }
}
