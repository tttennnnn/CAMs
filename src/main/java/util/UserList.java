package util;

import java.util.HashMap;

public class UserList {
    private final HashMap<String, String[]> students, staffs;
    public UserList() {
        students = new HashMap<>();
        staffs = new HashMap<>();
    }

    public String[] getStudent(String key) { return students.get(key); }
    public String[] getStaff(String key) { return staffs.get(key); }
    public void putStudent(String key, String[] value) { students.put(key, value); }
    public void putStaff(String key, String[] value) { staffs.put(key, value); }

    public boolean hasStudent(String key) { return students.containsKey(key); }
    public boolean hasStaff(String key) { return staffs.containsKey(key); }
}
