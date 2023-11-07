package userpage;

public class User {
    private final String userID, email, name, faculty;
    User(String userID, String email, String name, String faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
    }
    protected String getUserID() { return userID; }
    protected String getEmail() { return email; }
    protected String getName() { return name; }
    protected String getFaculty() { return faculty; }
}
