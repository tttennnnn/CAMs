package camp;

import java.util.ArrayList;
import java.util.Arrays;

public class CampSlot {
    private ArrayList<String> students, committees, withdrawns;
    private int maxTotal, maxCommittee = 10;

    public CampSlot(int maxTotal) {
        students = new ArrayList<>();
        committees = new ArrayList<>();
        this.maxTotal = maxTotal;
    }

    public CampSlot(int maxTotal, ArrayList<String> students, ArrayList<String> committees, ArrayList<String> withdrawns) {
        this.students = students;
        this.committees = committees;
        this.maxTotal = maxTotal;
        this.withdrawns = withdrawns;
    }
    public ArrayList<String> getStudents() { return students; }
    public ArrayList<String> getCommittees() { return committees; }
    public ArrayList<String> getWithdrawns() { return withdrawns; }
    public int getMaxTotal() { return maxTotal; }
    public int getMaxCommittee() { return maxCommittee; }

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