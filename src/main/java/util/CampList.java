package util;

import camp.Camp;

import java.util.Collection;
import java.util.HashMap;

public class CampList {
    private final HashMap<String, Camp> camps;

    public CampList() {
        camps = new HashMap<>();
    }

    public Collection<Camp> getCampSet() { return camps.values(); }
    public Camp getCamp(String key) {
        return camps.get(key);
    }
    public void putCamp(String key, Camp value) {
        camps.put(key, value);
    }
    public boolean hasCamp(String key) { return camps.containsKey(key); }
}
