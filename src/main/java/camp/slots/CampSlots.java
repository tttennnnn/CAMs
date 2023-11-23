package camp.slots;

import camp.CampData;

import java.util.HashMap;
import java.util.HashSet;

public class CampSlots implements CampData {
    private HashSet<String> attendees, withdrawns;
    private HashMap<String, Integer> committees;
    private int totalSlot, committeeSlot;
    private static final int MAX_COMMITTEE_SLOT = 10;
    CampSlots(int totalSlot, int committeeSlot, HashSet<String> attendees, HashMap<String, Integer> committees, HashSet<String> withdrawns) {
        this.totalSlot = totalSlot;
        this.committeeSlot = committeeSlot;
        this.attendees = attendees;
        this.committees = committees;
        this.withdrawns = withdrawns;
    }
    static int getMaxCommitteeSlot() { return MAX_COMMITTEE_SLOT; }
    int getTotalSlot() { return totalSlot; }
    int getCommitteeSlot() { return committeeSlot; }
    public void setTotalSlot(int totalSlot) { this.totalSlot = totalSlot; }
    public void setCommitteeSlot(int committeeSlot) { this.committeeSlot = committeeSlot; }
    HashSet<String> getAttendees() { return attendees; }
    HashMap<String, Integer> getCommittees() { return committees; }
    HashSet<String> getWithdrawns() { return withdrawns; }
}
