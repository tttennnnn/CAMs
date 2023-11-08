package util;

import userpage.User;

import java.util.HashMap;

public class UserList {
    private final HashMap<String, User> students, staffs;
    public UserList() {
        students = new HashMap<>();
        staffs = new HashMap<>();
    }

    public User getStudent(String key) { return students.get(key); }
    public User getStaff(String key) { return staffs.get(key); }
    public void putStudent(String key, User value) { students.put(key, value); }
    public void putStaff(String key, User value) { staffs.put(key, value); }

    public boolean hasStudent(String key) { return students.containsKey(key); }
    public boolean hasStaff(String key) { return staffs.containsKey(key); }
}
