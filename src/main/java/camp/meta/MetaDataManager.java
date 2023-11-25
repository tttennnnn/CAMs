package camp.meta;

import app.CAMsApp;
import camp.NormalTypeManager;
import util.AppUtil;

import java.util.List;

public class MetaDataManager implements NormalTypeManager {
    private MetaData metaData;
    private MetaDataManager(MetaData metaData) {
        this.metaData = metaData;
    }
    public String getStaffID() { return metaData.getStaffID(); }
    public String getDescription() { return metaData.getDescription(); }
    public boolean isVisible() { return metaData.isVisible(); }
    public Faculty getFaculty() { return metaData.getFaculty(); }
    public Location getLocation() { return metaData.getLocation(); }
    public void setDescription(String description) { metaData.setDescription(description); }
    public void setLocation(Location location) { metaData.setLocation(location); }
    public void toggleVisibility() { metaData.setVisibility(!metaData.isVisible()); }

    @Override
    public MetaData unbindManager() {
        return metaData;
    }

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
