package Users;

import Extentions.DatabaseManager;
import java.util.UUID;
import java.time.*;

public class UserSession {
    private String ipadress;
    private User user;
    private LocalDateTime dateTime;
    private UUID sessionKey;
    private DatabaseManager databaseManager;

    //region Constructors
    public UserSession(String ipadress, User user, LocalDateTime dateTime, UUID sessionKey) {
        this.ipadress = ipadress;
        this.user = user;
        this.dateTime = dateTime;
        this.sessionKey = sessionKey;
    }

    public UserSession() {
    }
    //endregion

    //region Getters and setters
    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public UUID getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(UUID sessionKey) {
        this.sessionKey = sessionKey;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    //endregion
}
