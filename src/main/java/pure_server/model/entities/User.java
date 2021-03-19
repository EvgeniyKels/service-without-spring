package pure_server.model.entities;

public class User {
    private String id;
    private String userName;
    private String passwordHash;

    public User() {
    }

    public User(String id, String userName, String passwordHash) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
