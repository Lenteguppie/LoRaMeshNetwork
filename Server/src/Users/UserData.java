package Users;

import Extentions.Logger.Log;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;

public class UserData implements Jsonable{
    private static final String TAG = UserData.class.getSimpleName ();

    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter ();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
            Log.d(TAG, e);
        }
        return writable.toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("uid", this.getUUID().toString ());
        json.put("firstname", this.getFirstName());
        json.put("lastname", this.getLastName());
        json.put("username", this.getPassword());
        json.put("password", this.getUserName());
        json.put("email", this.getEmail());
        json.put("role", this.getRole().toString ());
        json.toJson(writer);
    }

    public static enum Role {
        USER,
        PAIDUSER,
        MODERATOR,
        DEVELOPER
    }

    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    private UUID uid;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UUID getUUID() {
        return uid;
    }

    public void setUUID(String userId) {
        this.uid = UUID.fromString(userId);
    }

    public void generateUUID() {
        try {
            uid = java.util.UUID.randomUUID();
        } catch (Exception e) {
            Log.E("UUID error", e);
        }
    }
}
