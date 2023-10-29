package app;

import java.util.HashMap;

class UserList {
    private HashMap<String, String[]> students, staffs;
    UserList() {
        students = new HashMap<>();
        staffs = new HashMap<>();
    }
    String[] getStudent(String key) { return students.get(key); }
    String[] getStaff(String key) { return staffs.get(key); }
    void putStudent(String key, String[] value) { students.put(key, value); }
    void putStaff(String key, String[] value) { staffs.put(key, value); }

    boolean hasStudent(String key) { return students.containsKey(key); }
    boolean hasStaff(String key) { return staffs.containsKey(key); }

}
