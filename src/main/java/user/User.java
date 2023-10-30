package user;

public abstract class User {
    private final String userID, email, name, faculty;
    public User(String userID, String email, String name, String faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
    }
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getFaculty() { return faculty; }

//    boolean resetPW(char[] pw) {
//
//    }

    public abstract void showMenu();
    public abstract void UserApp();
}
