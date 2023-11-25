package userpage;

import camp.meta.Faculty;

public class User {
    private final String userID, email, name;
    private final Faculty faculty;
    public User(String userID, String email, String name, Faculty faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
    }
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }
}
