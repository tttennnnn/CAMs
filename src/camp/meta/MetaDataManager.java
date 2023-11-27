package camp.meta;

import app.CAMsApp;
import camp.NormalTypeManager;
import util.AppUtil;

import java.util.List;

/**
 * class that represent the manager of camp information data
 * @author Anqi
 * @date 2023/11/22
 */
public class MetaDataManager implements NormalTypeManager {
    /**
     * camp information data
     */
    private MetaData metaData;

    /**
     * manager of camp information data
     * @param metaData
     */
    private MetaDataManager(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * get the ID of staff
     * @return ID of staff
     */
    public String getStaffID() { return metaData.getStaffID(); }

    /**
     * get the description of camp
     * @return description of camp
     */
    public String getDescription() { return metaData.getDescription(); }

    /**
     * see if camp is visible to user
     * @return boolean
     */
    public boolean isVisible() { return metaData.isVisible(); }

    /**
     * get the faculty of camp
     * @return faculty of camp
     */
    public Faculty getFaculty() { return metaData.getFaculty(); }

    /**
     * get the location of camp
     * @return location of camp
     */
    public Location getLocation() { return metaData.getLocation(); }

    /**
     * set the description of camp
     * @param description
     */
    public void setDescription(String description) { metaData.setDescription(description); }

    /**
     * set the location of camp
     * @param location
     */
    public void setLocation(Location location) { metaData.setLocation(location); }

    /**
     * set the visibility of camp
     */
    public void toggleVisibility() { metaData.setVisibility(!metaData.isVisible()); }

    /**
     * manager of unbind metadata
     * @return metaData
     */
    @Override
    public MetaData unbindManager() {
        return metaData;
    }

    /**
     * update the data of camp to the data file
     * @param campName
     */
    @Override
    public void updateToFile(String campName) {
        String[] newInfoLine = new String[]{
            campName,
            getStaffID(),
            getDescription(),
            (isVisible()) ? "T" : "F",
            getFaculty().name(),
            getLocation().name()
        };
        List<String[]> infoLines = AppUtil.getDataFromCSV(CAMsApp.getCampInfoFile());
        int row;
        for (row = 1; row < infoLines.size(); row++) {
            String[] infoLine = infoLines.get(row);
            if (infoLine[0].equals(campName)) {
                infoLines.set(row, newInfoLine);
                break;
            }
        }
        if (row >= infoLines.size()) {
            infoLines.add(newInfoLine);
        }
        AppUtil.overwriteCSV(CAMsApp.getCampInfoFile(), infoLines);
    }

    // static methods
    public static MetaDataManager createInstance(MetaData metaData) {
        return new MetaDataManager(metaData);
    }
    public static MetaDataManager createInstance(String staffID, String description, boolean visibility, Faculty faculty, Location location) {
        MetaData metaData = new MetaData(staffID, description, visibility, faculty, location);
        return MetaDataManager.createInstance(metaData);
    }
}
