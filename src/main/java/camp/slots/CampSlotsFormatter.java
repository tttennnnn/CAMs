package camp.slots;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class CampSlotsFormatter {
    static String getAttendeeListAsString(HashSet<String> attendees) {
        return String.join(";", attendees);
    }
    static String getCommitteeListAsString(HashMap<String, Integer> committees) {
        String[] arr = new String[committees.size()];
        int i = 0;
        for (String committee : committees.keySet()) {
            arr[i++] = committee + "=" + committees.get(committee);
        }
        return String.join(";", arr);
    }
    static String getWithdrawnListAsString(HashSet<String> withdrawns) {
        return String.join(";", withdrawns);
    }
    public static HashSet<String> getAttendeeListAsSet(String attendees) {
        if (attendees.isEmpty())
            return new HashSet<>();

        String[] arr = attendees.split(";");
        return new HashSet<>(Arrays.asList(arr));
    }
    public static HashMap<String, Integer> getCommitteeListAsMap(String committees) {
        if (committees.isEmpty())
            return new HashMap<>();

        HashMap<String, Integer> res = new HashMap<>();
        String[] arr = committees.split(";");
        for (String pair : arr) {
            String committee = pair.split("=")[0];
            Integer point = Integer.parseInt(pair.split("=")[1]);
            res.put(committee, point);
        }
        return res;
    }
    public static HashSet<String> getWithdrawnListAsSet(String withdrawns) {
        if (withdrawns.isEmpty())
            return new HashSet<>();

        String[] arr = withdrawns.split(";");
        return new HashSet<>(Arrays.asList(arr));
    }
}
