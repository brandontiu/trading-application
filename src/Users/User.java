package Users;

import java.io.Serializable;
import java.util.UUID;

/**
 * <h1>User</h1>
 * Represents the parent class of TradingUser, AdminUser, and DemoUser
 */
public abstract class User implements Serializable {
    private String username;
    private String password;
    private final UUID userId = UUID.randomUUID();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for username as a String
     * @return username as a String
     */
    public String getUsername() { return username; }

    /**
     * Setter for password as a String
     * @param newPass new password as a String
     */
    public void setPassword(String newPass) { this.password = newPass; }

    /**
     * Getter for password as a String
     * @return password as a String
     */
    public String getPassword() { return password; }

    /**
     * Getter for userId as a UUID
     * @return userId as a UUID
     */
    public UUID getUserId() { return userId; }
}

