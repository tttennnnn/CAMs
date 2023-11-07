package camp;

import java.util.Date;

public class Camp {
    private String name, description, staffID;
    private String[] students, committees;

    private Date startDate, endDate, registrationClosingDate;

    private int totalSlots, committeeSlots;

    private boolean visibility;
    private Faculty faculty;
    private Location location;
}