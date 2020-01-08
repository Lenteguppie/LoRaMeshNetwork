package LoRa.DataSets;

import Extentions.DatabaseManager;
import Users.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private UserData.Role role;
    private String username;
    private String password;
    private String uid;

    //region setters and getters
    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserData.Role getRole() {
        return role;
    }

    private void setRole(UserData.Role role){
        this.role = role;
    }

    private void setRole(String role) {
        role = role.toLowerCase();
        switch (role){
            case "user":{
                setRole(UserData.Role.USER);
            }
            case "developer":{
                setRole(UserData.Role.DEVELOPER);
            }
            case "moderator":{
                setRole(UserData.Role.MODERATOR);
            }
            case "paiduser":{
                setRole(UserData.Role.PAIDUSER);
            }
            default:{
                setRole(UserData.Role.USER);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    private void setUid(String uid) {
        this.uid = uid;
    }
    //endregion

    public static User getUser(String UID, DatabaseManager databaseManager) throws Exception {
        databaseManager.setQueryFormat("SELECT * FROM users WHERE uid = '%s';");
        try {
            return fromResultSet(databaseManager.sendArgs(new String[]{UID}));
        } catch (SQLException e) {
            throw new Exception("Invalid uid, User not found");
        }
    }

    private static User fromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        resultSet.absolute(1);
        user.setEmail(resultSet.getString("email"));
        user.setFirstName(resultSet.getString("firstname"));
        user.setLastName(resultSet.getString("lastname"));
        user.setRole(resultSet.getString("role"));
        user.setPassword(resultSet.getString("password"));
        user.setUid(resultSet.getString("uid"));
        return user;
    }
}
