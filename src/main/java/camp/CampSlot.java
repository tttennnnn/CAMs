package camp;

import app.CAMsApp;
import util.AppUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CampSlot {
    private HashSet<String> attendees, withdrawns;
    private HashMap<String, Integer> committees;
    private int maxTotal, maxCommittee = 10;

    public CampSlot(int maxTotal) {
        attendees = new HashSet<>();
        committees = new HashMap<>();
        withdrawns = new HashSet<>();

        this.maxTotal = maxTotal;
    }

    public CampSlot(int maxTotal, HashSet<String> attendees, HashMap<String, Integer> committees, HashSet<String> withdrawns) {
        this.attendees = attendees;
        this.committees = committees;
        this.maxTotal = maxTotal;
        this.withdrawns = withdrawns;
    }
    public HashSet<String> getAttendees() { return attendees; }
    public HashMap<String, Integer> getCommittees() { return committees; }
    public HashSet<String> getWithdrawns() { return withdrawns; }
    public int getMaxTotal() { return maxTotal; }
    public int getMaxCommittee() { return maxCommittee; }

    public static String getAttendeeListAsString(HashSet<String> attendees) {
        return String.join(";", attendees);
    }
    public static String getCommitteeListAsString(HashMap<String, Integer> committees) {
        String[] arr = new String[committees.size()];
        int i = 0;
        for (String committee : committees.keySet()) {
            arr[i++] = committee + "=" + committees.get(committee);
        }
        return String.join(";", arr);
    }
    public static String getWithdrawnListAsString(HashSet<String> withdrawns) {
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

    public static void updateCampSlotToFile(String campName, CampSlot campSlot) {
        List<String[]> slotLines = AppUtil.getDataFromCSV(CAMsApp.getCampSlotFile());
        for (int row = 1; row < slotLines.size(); row++) {
            String[] slotLine = slotLines.get(row);
            if (slotLine[0].equals(campName)) {
                slotLine[1] = Integer.toString(campSlot.getMaxTotal());
                slotLine[2] = CampSlot.getAttendeeListAsString(campSlot.getAttendees());
                slotLine[3] = CampSlot.getCommitteeListAsString(campSlot.getCommittees());
                slotLine[4] = CampSlot.getWithdrawnListAsString(campSlot.getWithdrawns());
                break;
            }
        }
        AppUtil.overwriteCSV(CAMsApp.getCampSlotFile(), slotLines);
    }
}