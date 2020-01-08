package Users;

import Extentions.ConfigManager;
import Extentions.DatabaseManager;
import Extentions.JSONManager;
import Extentions.Logger.Log;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    static final public String TAG = UserManager.class.getSimpleName();
    private HashMap<UUID, User> userBuffer = new HashMap<>();
    private DatabaseManager databaseManager;

    public UserManager(JSONObject config) {
        try {
            databaseManager = DatabaseManager.initFromJSON(config);
            databaseManager.connect();
        } catch (ConfigManager.ConfigManagerException | DatabaseManager.DatabaseException.IsOffline e) {
            Log.d( TAG, e );
        }

    }

    public User getUser(UUID uuid) {
        return getUserFromBuffer( uuid );
    }

    public User getUser(String username) {
        if (isInUserBuffer( username )) {
            return getUserFromBuffer( username );
        } else if (isInUserDatabase( username )) {
            return getUserFromDatabase( username );
        } else {
            return null;
        }
    }

    public void createUser(String username, String password, String firstName, String lastName, String email, UserData.Role role) {
        //TODO Create Query to check if user with that username and/or email exists
        boolean exist = userExist( username );

        User user = new User( username, password, firstName, lastName, email, role );
        if (!exist) {
            user.getUserData().generateUUID();
            addUserToBuffer( user );
            addUserToDatabase( user );
//            return true;
        } else {
            Log.d( TAG, "User already exist" );
            addUserToBuffer( user );
//            return false;
        }
        //TODO Create Query to insert the user in the database
    }

    public void createUser(String uuid, String username, String password, String firstName, String lastName, String email, UserData.Role role) {
        //TODO Create Query to check if user with that username and/or email exists
        boolean exist = userExist( username );

        User user = new User( username, password, firstName, lastName, email, role );
        if (!exist) {
            user.getUserData().setUUID( uuid );
            addUserToBuffer( user );
            //            return true;
        } else {
            Log.d( TAG, "User already exist" );
            addUserToBuffer( user );
        }
    }

    private boolean userExist(String username) {
        if (isInUserBuffer( username )) {
            Log.d( TAG, "User found in buffer with username: " + username );
            return true;
        } else if (isInUserDatabase( username )) {
            Log.d( TAG, "User found in database with username: " + username );
            return true;
        } else {
            Log.d( TAG, "No users found in the buffer or database with username: " + username );
            return false;
        }
    }

    public void addUserToDatabase(User user) {
        JSONObject userData = null;
        if (!(isInUserDatabase( user.getUserData().getUserName() ))) {
            userData = JSONManager.parseStringToJSONObject( user.getUserData().toJson() );
            Log.d( TAG + " JSON: ", userData );
            databaseManager.setQueryFormat( "INSERT INTO `loraserver`.`users` (`email`,`firstname`, `lastname`, `role`, `username`, `password`, `uid`) VALUES ('%s','%s','%s','%s','%s','%s','%s');" );
            try {
                databaseManager.sendArgs( new String[]{
                                (String) userData.get( "email" ),
                                (String) userData.get( "firstname" ),
                                (String) userData.get( "lastname" ),
                                (String) userData.get( "role" ),
                                (String) userData.get( "username" ),
                                (String) userData.get( "password" ),
                                (String) userData.get( "uid" )
                        }
                );
            } catch (SQLException e) {
                Log.d( TAG, e );
            }
        } else {
            Log.d( TAG, "User already exists so no push to dtb!" );
        }


        //TODO create query to inset the user in the database

    }

    public void addUserToBuffer(User user) {
        //TODO Create Query to check if user with that username and/or email exists
        if (!isInUserBuffer( user.getUserData().getUserName() )) {
            userBuffer.put( user.getUserData().getUUID(), user );
        }
    }

    void updateUser() {
        //TODO User needs to update
    }

    public boolean checkUserCredentials(String username, String password) {
        User tempUser = null;
        if (!isInUserBuffer( username )) {
            tempUser = getUserFromDatabase( username );
            return (tempUser.getUserData().getPassword().equals( password ));
        } else if (isInUserBuffer( username )) {
            tempUser = getUserFromBuffer( username );
            return (tempUser.getUserData().getPassword().equals( password ));
        } else {
            return false;
        }
    }

    boolean isInUserBuffer(String username) {
        for (Map.Entry<UUID, User> entry : userBuffer.entrySet()) {
            if (entry.getValue().getUserData().getUserName().equals( username )) {
                Log.d( TAG, "User found with username " + username );
                return true;
            }
        }
        return false;
    }

    boolean isInUserDatabase(String username) {
        String query = String.format( "SELECT EXISTS(SELECT * from users WHERE %s);", username );
        ResultSet r;
        databaseManager.setQueryFormat( "SELECT EXISTS(SELECT * from users WHERE %s" );
        try {
            r = databaseManager.sendArgs( new String[]{String.format( "username='%s') AS res;", username )} );
            r.absolute( 1 );
            Log.d( TAG, "DTB User: " + r.getBoolean( 1 ) );
            return (r.getBoolean( 1));
        } catch (SQLException e) {
            Log.d( TAG, e );
        }
        return true;
    }

    public User getUserFromDatabase(UUID uuid) {
        //TODO Get the user from the database with UUID
        User tempUser;
        ResultSet r;
        databaseManager.setQueryFormat( "SELECT uid, username, password, firstname, lastname, email, role FROM Users WHERE %s" );       // SELECT the first row from the result
        try {
            r = databaseManager.sendArgs( new String[]{String.format( "uid='%s'", uuid.toString() )} );
            r.absolute( 1 );
            tempUser = new User( r.getString( 0 ), r.getString( 2 ), r.getString( 3 ), r.getString( 4 ), r.getString( 5 ), UserData.Role.valueOf( r.getString( 6 ) ) );
            addUserToBuffer( tempUser );
        } catch (SQLException e) {
            Log.d( TAG, e );
            tempUser = null;
        }
        return tempUser;
    }

    public User getUserFromDatabase(String username) {
        //TODO Get the user from the database with UUID
        User tempUser;
        ResultSet r;
        databaseManager.setQueryFormat( "SELECT uid, username, password, firstname, lastname, email, role FROM Users WHERE %s" );       // SELECT the first row from the result
        try {
            r = databaseManager.sendArgs( new String[]{String.format( "username='%s'", username )} );
            r.absolute( 1 );
            tempUser = new User( r.getString( 1 ), r.getString( 2 ), r.getString( 3 ), r.getString( 4 ), r.getString( 5 ),r .getString( 6 ), UserData.Role.valueOf( r.getString( 7 ) ) );
            addUserToBuffer( tempUser );
        } catch (SQLException e) {
            Log.d( TAG + "UserFromDTB", e );
            tempUser = null;
        }
        return tempUser;
    }

    User getUserFromBuffer(UUID userId) {
        User u = userBuffer.get( userId );
        return u;
    }

    User getUserFromBuffer(String userName) {
        for (Map.Entry<UUID, User> entry : userBuffer.entrySet()) {
            if (entry.getValue().getUserData().getUserName().equals( userName )) {
                Log.d( TAG, "User found with username " + userName );
                return entry.getValue();
            }
        }
        return null;
    }
}
