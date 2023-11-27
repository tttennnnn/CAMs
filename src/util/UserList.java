package util;

import userpage.User;

import java.util.HashMap;

/**
 * class that represents the user list
 * @author Anqi
 * @date 2023/11/22
 */
public class UserList {
    /**
     * hash map of student and staff
     */
    private final HashMap<String, User> students, staffs;

    /**
     * constructor of UserList
     */
    public UserList() {
        students = new HashMap<>();
        staffs = new HashMap<>();
    }

    /**
     * get student as user object
     * @param key
     * @return student
     */
    public User getStudent(String key) { return students.get(key); }

    /**
     * get staff as user object
     * @param key
     * @return Staff
     */
    public User getStaff(String key) { return staffs.get(key); }

    /**
     * put student into user list
     * @param key
     * @param value
     */
    public void putStudent(String key, User value) { students.put(key, value); }

    /**
     * put staff into user list
     * @param key
     * @param value
     */
    public void putStaff(String key, User value) { staffs.put(key, value); }

    /**
     * see if list contains student
     * @param key
     * @return boolean
     */
    public boolean hasStudent(String key) { return students.containsKey(key); }

    /**
     * see if list contains staff
     * @param key
     * @return boolean
     */
    public boolean hasStaff(String key) { return staffs.containsKey(key); }
}
