package camp;

import app.CAMsApp;
import util.AppUtil;

import java.util.*;

/**
 * class that represent the list of camp
 * @author Anqi
 * @date 2023/11/22
 */
public class CampList implements DataManager {
    /**
     * HashMap of camps
     */
    private final HashMap<String, CampManager> camps;

    /**
     * use of hash map for the list of camp
     */
    public CampList() {
        camps = new HashMap<>();
    }

    /**
     * get Sorted Set of Camp
     * @return sorted Camps
     */
    public Collection<CampManager> getSortedCampSet() {
        Collection<CampManager> camps = this.camps.values();
        TreeMap<String, CampManager> sortedCamps = new TreeMap<>();
        for (CampManager camp : camps) {
            sortedCamps.put(camp.getName(), camp);
        }
        return sortedCamps.values();
    }

    /**
     * get the camp with certain key
     * @param key
     * @return camp
     */
    public CampManager getCamp(String key) {
        return camps.get(key);
    }

    /**
     * see if the camp exist
     * @param key
     * @return boolean
     */
    public boolean hasCamp(String key) { return camps.containsKey(key); }

    /**
     * add camp
     * @param key
     * @param value
     */
    public void putCamp(String key, CampManager value) { camps.put(key, value); }

    /**
     * update the camp information To data file
     * @param campToDelete
     */
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
