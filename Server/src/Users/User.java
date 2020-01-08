package Users;

import Extentions.JSONManager;
import Extentions.Logger.Log;
import org.json.JSONObject;

public class User{
    //TODO communicate with database to check if user excists
    static final public String TAG = User.class.getSimpleName( );
    private UserData userData;

    /* Constructor for new users */
    public User(String username, String password, String firstName, String lastName, String email, UserData.Role role){
        this.userData = new UserData();
        userData.setEmail(email);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setRole(role);
        userData.setUserName(username);
        userData.setPassword(password);
        userData.generateUUID();
    }

    /* Constructor for excisting users */
    public User(String username, String firstName, String lastName, String email, UserData.Role role){
        this.userData = new UserData();
        userData.setEmail(email);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setRole(role);
        userData.setUserName(username);
        userData.generateUUID();
    }

    public User(String uid, String username, String password, String firstName, String lastName, String email, UserData.Role role) {
        this.userData = new UserData();
        userData.setUUID(uid);
        userData.setEmail(email);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setRole(role);
        userData.setUserName(username);
        userData.setPassword(password);
        userData.setRole(role);
    }

    public void updateUserData(String username, String password, String firstName, String lastName, String email, UserData.Role role){
        //TODO create a script that updates users in the database and filter out the field that doesn't change!
        this.userData = new UserData();
        userData.setEmail(email);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setRole(role);
        userData.setUserName(username);
        userData.setPassword(password);
        userData.generateUUID();
    }

    public JSONObject getUserDataJSON(){

        try {
            return JSONManager.parseObjectToJSONObject (getUserData());
        } catch (Exception e) {
            Log.d (TAG, e);
            return null;
        }
    }

    public UserData getUserData(){ return this.userData;}
}
