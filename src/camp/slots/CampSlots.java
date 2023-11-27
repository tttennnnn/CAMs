package camp.slots;

import camp.CampData;

import java.util.HashMap;
import java.util.HashSet;

/**
 * class that represents slots of camp
 * @author Anqi
 * @date 2023/11/22
 */
public class CampSlots implements CampData {
    /**
     *  hash set of attendees and also those withdrawn
     */
    private HashSet<String> attendees, withdrawns;
    /**
     * hash map of committees
     */
    private HashMap<String, Integer> committees;
    /**
     * total slots and committee slots
     */
    private int totalSlot, committeeSlot;
    /**
     * the max amount of committee of a camp is 10
     */
    private static final int MAX_COMMITTEE_SLOT = 10;

    /**
     * constructor of CampSlots(totalSlot, committeeSlot, attendees, committees, withdrawns)
     * @param totalSlot
     * @param committeeSlot
     * @param attendees
     * @param committees
     * @param withdrawns
     */
    CampSlots(int totalSlot, int committeeSlot, HashSet<String> attendees, HashMap<String, Integer> committees, HashSet<String> withdrawns) {
        this.totalSlot = totalSlot;
        this.committeeSlot = committeeSlot;
        this.attendees = attendees;
        this.committees = committees;
        this.withdrawns = withdrawns;
    }

    /**
     * get Max Committee Slot
     * @return int
     */
    static int getMaxCommitteeSlot() { return MAX_COMMITTEE_SLOT; }

    /**
     * get Total Slot
     * @return int
     */
    int getTotalSlot() { return totalSlot; }

    /**
     * get Committee Slot
     * @return int
     */
    int getCommitteeSlot() { return committeeSlot; }

    /**
     * set total Slot
     * @param totalSlot
     */
    public void setTotalSlot(int totalSlot) { this.totalSlot = totalSlot; }

    /**
     * set Committee Slot
     * @param committeeSlot
     */
    public void setCommitteeSlot(int committeeSlot) { this.committeeSlot = committeeSlot; }

    /**
     * get Attendees in the slots
     * @return {@link HashSet}<{@link String}>
     */
    HashSet<String> getAttendees() { return attendees; }

    /**
     * get committees in the slots
     * @return {@link HashMap}<{@link String}, {@link Integer}>
     */
    HashMap<String, Integer> getCommittees() { return committees; }

    /**
     * get withdrawns in the slots
     * @return {@link HashSet}<{@link String}>
     */
    HashSet<String> getWithdrawns() { return withdrawns; }
}
