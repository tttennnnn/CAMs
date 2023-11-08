package camp;

import java.util.ArrayList;
import java.util.Arrays;

public class CampSlot {
    private ArrayList<String> students, committees;
    private int maxTotal, maxCommittee = 10;

    public CampSlot(int maxTotal) {
        students = new ArrayList<>();
        committees = new ArrayList<>();
        this.maxTotal = maxTotal;
    }

    public CampSlot(int maxTotal, ArrayList<String> students, ArrayList<String> committees) {
        this.students = students;
        this.committees = committees;
        this.maxTotal = maxTotal;
    }

    public int getOccupiedTotal() { return students.size() + committees.size(); }
    public int getOccupiedCommittee() { return committees.size(); }
    public int getMaxTotal() { return maxTotal; }
    public int getMaxCommittee() { return maxCommittee; }
    public boolean hasStudent(String key) { return students.contains(key); }
    public boolean hasCommittee(String key) { return committees.contains(key); }

    public static String getAttendeeListAsString(ArrayList<String> attendeeList) {
        return String.join(";", attendeeList);
    }
    public static ArrayList<String> getAttendeeListAsArrayList(String attendeeList) {
        if (attendeeList.isEmpty())
            return new ArrayList<>();

        String[] arr = attendeeList.split(";");
        return new ArrayList<>(Arrays.asList(arr));
    }
}