package camp.slots;

import app.CAMsApp;
import camp.CampManager;
import camp.NormalTypeManager;
import util.AppUtil;
import camp.CampList;

import java.util.*;

public class CampSlotsManager implements NormalTypeManager {
    private CampSlots campSlots;
    private CampSlotsManager(CampSlots campSlots) {
        this.campSlots = campSlots;
    }
    public int getTotalSlot() { return campSlots.getTotalSlot(); }
    public int getCommitteeSlot() { return campSlots.getCommitteeSlot(); }
    public int getTotalVacancy() { return campSlots.getTotalSlot() - campSlots.getAttendees().size() - campSlots.getCommittees().size(); }
    public int getCommitteeVacancy() {
        return campSlots.getCommitteeSlot() - campSlots.getCommittees().size();
    }
    public String getTotalSlotAsString() { return (campSlots.getAttendees().size() + campSlots.getCommittees().size()) + "/" + campSlots.getTotalSlot(); }
    public String getCommitteeSlotAsString() { return campSlots.getCommittees().size() + "/" + campSlots.getCommitteeSlot(); }
    public ArrayList<String> reportAttendeeList() {
        ArrayList<String> list = new ArrayList<>(campSlots.getAttendees());
        Collections.sort(list);
        return list;
    }
    public ArrayList<String> reportCommitteeList() {
        ArrayList<String> list = new ArrayList<>(campSlots.getCommittees().keySet());
        Collections.sort(list);
        return list;
    }

    public void setTotalSlot(int totalSlot) { campSlots.setTotalSlot(totalSlot); }
    public void setCommitteeSlot(int committeeSlot) { campSlots.setCommitteeSlot(committeeSlot); }
    public void addAttendee(String userID) { campSlots.getAttendees().add(userID); }
    public void addCommittee(String userID, int point) { campSlots.getCommittees().put(userID, point); }
    public void addWithdrawn(String userID) { campSlots.getWithdrawns().add(userID); }
    public void removeAttendee(String userID) { campSlots.getAttendees().remove(userID); }
    public boolean hasAttendee(String userID) { return campSlots.getAttendees().contains(userID); }
    public boolean hasCommittee(String userID) { return campSlots.getCommittees().containsKey(userID); }
    public boolean hasWithdrawn(String userID) { return campSlots.getWithdrawns().contains(userID); }
    public String getUserRole(String userID) {
        if (campSlots.getAttendees().contains(userID))
            return "Attendee";
        if (campSlots.getCommittees().containsKey(userID))
            return "Committee";
        if (campSlots.getWithdrawns().contains(userID))
            return "Withdrawn";
        return "-";
    }
    public int getCommitteePoint(String userID) { return campSlots.getCommittees().get(userID); }
    public void incrementCommitteePoint(String userID) {
        int point = getCommitteePoint(userID);
        campSlots.getCommittees().put(userID, point+1);
    }
    public void decrementCommitteePoint(String userID) {
        int point = getCommitteePoint(userID);
        campSlots.getCommittees().put(userID, point-1);
    }
    public boolean isRegistered() { return (campSlots.getAttendees().size() + campSlots.getCommittees().size()) > 0; }
    @Override
    public CampSlots unbindManager() { return campSlots; }
    @Override
    public void updateToFile(String campName) {
        List<String[]> slotLines = AppUtil.getDataFromCSV(CAMsApp.getCampSlotFile());
        String[] newSlotLine = new String[]{
            campName,
            Integer.toString(campSlots.getTotalSlot()),
            Integer.toString(campSlots.getCommitteeSlot()),
            CampSlotsFormatter.getAttendeeListAsString(campSlots.getAttendees()),
            CampSlotsFormatter.getCommitteeListAsString(campSlots.getCommittees()),
            CampSlotsFormatter.getWithdrawnListAsString(campSlots.getWithdrawns())
        };
        int row;
        for (row = 1; row < slotLines.size(); row++) {
            String[] slotLine = slotLines.get(row);
            if (slotLine[0].equals(campName)) {
                slotLines.set(row, newSlotLine);
                break;
            }
        }
        if (row >= slotLines.size()) {
            slotLines.add(newSlotLine);
        }
        AppUtil.overwriteCSV(CAMsApp.getCampSlotFile(), slotLines);
    }

    // static methods
    public static CampSlotsManager createInstance(CampSlots campSlots) { return new CampSlotsManager(campSlots); }
    public static CampSlotsManager createInstance(int totalSlot, int committeeSlot) {
        CampSlots campSlots = new CampSlots(totalSlot, committeeSlot, new HashSet<>(), new HashMap<>(), new HashSet<>());
        return new CampSlotsManager(campSlots);
    }
    public static CampSlotsManager createInstance(int maxTotal, int committeeSlot, HashSet<String> attendees, HashMap<String, Integer> committees, HashSet<String> withdrawns) {
        CampSlots campSlots = new CampSlots(maxTotal, committeeSlot, attendees, committees, withdrawns);
        return new CampSlotsManager(campSlots);
    }
    public static String getUserCommitteeStatus(String userID) {
        CampList campList = AppUtil.readCamps();
        for (CampManager camp : campList.getSortedCampSet()) {
            CampSlotsManager slotsManager = camp.getSlotsManager();
            if (slotsManager.getUserRole(userID).equals("Committee")) {
                return camp.getName();
            }
        }
        return "-";
    }
    public static int getUserCommitteePoint(String userID) {
        String committeeStatus = getUserCommitteeStatus(userID);
        CampList campList = AppUtil.readCamps();
        return campList.getCamp(committeeStatus).getSlotsManager().getCommitteePoint(userID);
    }
    public static void isValidSlots(int totalSlot, int committeeSlot) throws IllegalArgumentException {
        if (committeeSlot < 1)
            throw new IllegalArgumentException("Camp committee slots must be at least 1");
        if (committeeSlot > CampSlots.getMaxCommitteeSlot())
            throw new IllegalArgumentException("Camp committee slots must not be greater than " + CampSlots.getMaxCommitteeSlot());
        if (totalSlot <= committeeSlot) {
            throw new IllegalArgumentException("Total slots must be greater than Committee slots.");
        }
    }
}
