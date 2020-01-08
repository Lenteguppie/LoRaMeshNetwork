package Users;

import Extentions.Logger.Log;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;


public class UserSessionManager extends UserManager{
    // Initializing the list of loaded users
    static private HashMap<UUID, User> UserSessions;

    public UserSessionManager(JSONObject config){
        super(config);
        this.UserSessions = new HashMap<UUID, User>();
    }

    UUID newSession(UUID userID){
        User tempUser = super.getUser(userID);
        if (!sessionexist(tempUser)) {
            UUID sessionKey = generateSessionKey();
            UserSessions.put( sessionKey, tempUser );
            Log.d(TAG, String.format("User session created for user: %s with sessionKey: %s", tempUser.getUserData().getUserName(), sessionKey.toString()));
            return sessionKey;
        }else{
            Log.d(TAG, "User session already exists");
            return null;
        }
    }

    boolean sessionexist(UUID sessionKey){
        return (UserSessions.containsKey(sessionKey));
    }

    boolean sessionexist(User user){
        return (UserSessions.containsValue(user));
    }

    public UUID generateSessionKey() {
        UUID sessionKey;
        try {
            sessionKey = java.util.UUID.randomUUID();
            return sessionKey;
        } catch (Exception e) {
            Log.d("UUID error", e);
            return null;
        }
    }

    void removeSession(UUID sessionKey){ UserSessions.remove(sessionKey); }
    void clearSessions(){ UserSessions.clear(); }




}
