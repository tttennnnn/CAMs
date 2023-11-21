package util;

import camp.Camp;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class CampList {
    private final HashMap<String, Camp> camps;

    public CampList() {
        camps = new HashMap<>();
    }

    public Collection<Camp> getSortedCampSet() {
        Collection<Camp> camps = this.camps.values();
        TreeMap<String, Camp> sortedCamps = new TreeMap<>();
        for (Camp camp : camps) {
            sortedCamps.put(camp.getName(), camp);
        }
        return sortedCamps.values();
    }
    public Camp getCamp(String key) {
        return camps.get(key);
    }
    public void putCamp(String key, Camp value) {
        camps.put(key, value);
    }
    public boolean hasCamp(String key) { return camps.containsKey(key); }
}
