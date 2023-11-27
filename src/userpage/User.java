package userpage;

import camp.meta.Faculty;

/**
 * class that represent the user information
 * @author Anqi
 * @date 2023/11/22
 */
public class User {
    /**
     * userID, email, name
     */
    private final String userID, email, name;
    /**
     * faculty
     */
    private final Faculty faculty;

    /**
     * constructor of user
     * @param userID
     * @param email
     * @param name
     * @param faculty
     */
    public User(String userID, String email, String name, Faculty faculty) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.faculty = faculty;
    }

    /**
     * get User ID
     * @return {@link String}
     */
    public String getUserID() { return userID; }

    /**
     * get Email of user
     * @return {@link String}
     */
    public String getEmail() { return email; }

    /**
     * get the name of user
     * @return {@link String}
     */
    public String getName() { return name; }

    /**
     * get the faculty of user
     * @return {@link Faculty}
     */
    public Faculty getFaculty() { return faculty; }
}
