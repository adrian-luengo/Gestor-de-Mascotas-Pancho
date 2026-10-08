package models;

// This class represents a user in the system
abstract public class User {
    String username;

    String usernameCode;

    final int id;

    public User(String username, String usernameCode, int id) {
        this.username = username;
        this.usernameCode = usernameCode;
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUsernameCode() {
        return this.usernameCode;
    }

    public int getId() {
        return this.id;
    }

    public abstract boolean isOwner();
}
