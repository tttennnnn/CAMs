package camp;

import app.CAMsApp;
import util.AppUtil;

import java.util.*;

public class CampList implements DataManager {
    private final HashMap<String, CampManager> camps;

    public CampList() {
        camps = new HashMap<>();
    }

    public Collection<CampManager> getSortedCampSet() {
        Collection<CampManager> camps = this.camps.values();
        TreeMap<String, CampManager> sortedCamps = new TreeMap<>();
        for (CampManager camp : camps) {
            sortedCamps.put(camp.getName(), camp);
        }
        return sortedCamps.values();
    }
    public CampManager getCamp(String key) {
        return camps.get(key);
    }
    public boolean hasCamp(String key) { return camps.containsKey(key); }
    public void putCamp(String key, CampManager value) { camps.put(key, value); }
    @Override
    public void updateToFile(String campToDelete) {
        List<List<String[]>> files = new ArrayList<>();
        files.add(AppUtil.getDataFromCSV(CAMsApp.getCampInfoFile()));
        files.add(AppUtil.getDataFromCSV(CAMsApp.getCampSlotFile()));
        files.add(AppUtil.getDataFromCSV(CAMsApp.getCampDateFile()));
        files.add(AppUtil.getDataFromCSV(CAMsApp.getCampEnquiryFile()));
        files.add(AppUtil.getDataFromCSV(CAMsApp.getCampSuggestionFile()));
        for (List<String[]> fileLines : files) {
            for (int row = 1; row < fileLines.size(); row++) {
                String[] fileLine = fileLines.get(row);
                if (fileLine[0].equals(campToDelete)) {
                    fileLines.remove(row);
                    break;
                }
            }
        }
    }
}
