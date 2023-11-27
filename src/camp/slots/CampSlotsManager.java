package camp.slots;

import app.CAMsApp;
import camp.CampManager;
import camp.NormalTypeManager;
import util.AppUtil;
import camp.CampList;

import java.util.*;

/**
 * class that represents Camp Slots Manager
 * @author Anqi
 * @date 2023/11/22
 */
public class CampSlotsManager implements NormalTypeManager {
    /**
     * campSlots
     */
    private CampSlots campSlots;

    /**
     * constructor of camp slot manager
     * @param campSlots
     */
    private CampSlotsManager(CampSlots campSlots) {
        this.campSlots = campSlots;
    }

    /**
     * get Total Slot
     * @return Total Slot
     */
    public int getTotalSlot() { return campSlots.getTotalSlot(); }

    /**
     * get Committee Slot
     * @return committee Slot
     */
    public int getCommitteeSlot() { return campSlots.getCommitteeSlot(); }

    /**
     * get the total vacancy of the camp
     * @return total vacancy of the camp
     */
    public int getTotalVacancy() { return campSlots.getTotalSlot() - campSlots.getAttendees().size() - campSlots.getCommittees().size(); }

    /**
     * get the committee vacancy of the camp
     * @return committee vacancy of the camp
     */
    public int getCommitteeVacancy() {
        return campSlots.getCommitteeSlot() - campSlots.getCommittees().size();
    }

    /**
     * get Total Slot of camp As a form of String
     * @return Total Slot of camp As a form of String
     */
    public String getTotalSlotAsString() { return (campSlots.getAttendees().size() + campSlots.getCommittees().size()) + "/" + campSlots.getTotalSlot(); }

    /**
     * get Committee Slot of camp As a form of String
     * @return Committee Slot of camp As a form of String
     */
    public String getCommitteeSlotAsString() { return campSlots.getCommittees().size() + "/" + campSlots.getCommitteeSlot(); }

    /**
     * get the list of attendee
     * @return list of attendee
     */
    public ArrayList<String> reportAttendeeList() {
        ArrayList<String> list = new ArrayList<>(campSlots.getAttendees());
        Collections.sort(list);
        return list;
    }

    /**
     * get the list of Committee
     * @return list of Committee
     */
    public ArrayList<String> reportCommitteeList() {
        ArrayList<String> list = new ArrayList<>(campSlots.getCommittees().keySet());
        Collections.sort(list);
        return list;
    }

    /**
     * set Total Slot
     * @param totalSlot
     */
    public void setTotalSlot(int totalSlot) { campSlots.setTotalSlot(totalSlot); }

    /**
     * set Committee Slot
     * @param committeeSlot
     */
    public void setCommitteeSlot(int committeeSlot) { campSlots.setCommitteeSlot(committeeSlot); }

    /**
     * get Attendees in the slots
     * @param userID
     */
    public void addAttendee(String userID) { campSlots.getAttendees().add(userID); }

    /**
     * add committee to camp
     * @param userID
     * @param point
     */
    public void addCommittee(String userID, int point) { campSlots.getCommittees().put(userID, point); }

    /**
     * add Withdrawn
     * @param userID
     */
    public void addWithdrawn(String userID) { campSlots.getWithdrawns().add(userID); }

    /**
     * remove Attendee from the camp
     * @param userID
     */
    public void removeAttendee(String userID) { campSlots.getAttendees().remove(userID); }

    /**
     * see if it has Attendee
     * @param userID
     * @return boolean
     */
    public boolean hasAttendee(String userID) { return campSlots.getAttendees().contains(userID); }

    /**
     * see if it has Committee
     * @param userID
     * @return boolean
     */
    public boolean hasCommittee(String userID) { return campSlots.getCommittees().containsKey(userID); }

    /**
     * see if it has Withdrawn
     * @param userID
     * @return boolean
     */
    public boolean hasWithdrawn(String userID) { return campSlots.getWithdrawns().contains(userID); }

    /**
     * get the role of user
     * @param userID
     * @return {@link String}
     */
    public String getUserRole(String userID) {
        if (campSlots.getAttendees().contains(userID))
            return "Attendee";
        if (campSlots.getCommittees().containsKey(userID))
            return "Committee";
        if (campSlots.getWithdrawns().contains(userID))
            return "Withdrawn";
        return "-";
    }

    /**
     * get Point of Committee
     * @param userID
     * @return int
     */
    public int getCommitteePoint(String userID) { return campSlots.getCommittees().get(userID); }

    /**
     * increment Committee Point
     * @param userID
     */
    public void incrementCommitteePoint(String userID) {
        int point = getCommitteePoint(userID);
        campSlots.getCommittees().put(userID, point+1);
    }

    /**
     * decrement Committee Point
     * @param userID
     */
    public void decrementCommitteePoint(String userID) {
        int point = getCommitteePoint(userID);
        campSlots.getCommittees().put(userID, point-1);
    }

    /**
     * see if it is registered
     * @return boolean
     */
    public boolean isRegistered() { return (campSlots.getAttendees().size() + campSlots.getCommittees().size()) > 0; }

    /**
     * show the unbind Manager of camp slots
     * @return campSlots
     */
    @Override
    public CampSlots unbindManager() { return campSlots; }

    /**
     * update the information to data file
     * @param campName
     */
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

    /**
     * create Instance of camp Slots
     * @param campSlots
     * @return {@link CampSlotsManager}
     */// static methods
    public static CampSlotsManager createInstance(CampSlots campSlots) { return new CampSlotsManager(campSlots); }

    /**
     * create Instance of camp Slots( totalSlot, committeeSlot)
     * @param totalSlot
     * @param committeeSlot
     * @return {@link CampSlotsManager}
     */
    public static CampSlotsManager createInstance(int totalSlot, int committeeSlot) {
        CampSlots campSlots = new CampSlots(totalSlot, committeeSlot, new HashSet<>(), new HashMap<>(), new HashSet<>());
        return new CampSlotsManager(campSlots);
    }

    /**
     * create Instance of camp Slots( maxTotal, committeeSlot, attendees, committees, withdrawns)
     * @param maxTotal
     * @param committeeSlot
     * @param attendees
     * @param committees
     * @param withdrawns
     * @return {@link CampSlotsManager}
     */
    public static CampSlotsManager createInstance(int maxTotal, int committeeSlot, HashSet<String> attendees, HashMap<String, Integer> committees, HashSet<String> withdrawns) {
        CampSlots campSlots = new CampSlots(maxTotal, committeeSlot, attendees, committees, withdrawns);
        return new CampSlotsManager(campSlots);
    }

    /**
     * get User Committee Status
     * @param userID
     * @return Committee Status
     */
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

    /**
     * get Committee Point of User
     * @param userID
     * @return Committee Point of User
     */
    public static int getUserCommitteePoint(String userID) {
        String committeeStatus = getUserCommitteeStatus(userID);
        CampList campList = AppUtil.readCamps();
        return campList.getCamp(committeeStatus).getSlotsManager().getCommitteePoint(userID);
    }

    /**
     * see if the slot is still valid
     * @param totalSlot
     * @param committeeSlot
     * @throws IllegalArgumentException
     */
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
