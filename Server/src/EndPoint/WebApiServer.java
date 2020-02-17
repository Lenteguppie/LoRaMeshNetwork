package EndPoint;

import Extentions.ConfigManager;
import Extentions.Logger.Log;
import Extentions.Webserver.Webserver;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;

public class WebApiServer extends Webserver {

    private static String TAG = WebApiServer.class.getSimpleName();

    public WebApiServer(int port) {
        super(port);
    }

    public void init(){

    }

    public static WebApiServer initFromJSON(JSONObject WebServerAPIConfig) throws ConfigManager.ConfigManagerException {
        Log.d(TAG,"LOADING...", true);
        if(WebServerAPIConfig != null) {
            WebApiServer webApiServer =  new WebApiServer(Math.toIntExact(WebServerAPIConfig.getInt("Port")));
            Log.e("DONE");
            return webApiServer;
        }else {
            Log.e("FAILED");
        }
        throw new ConfigManager.ConfigManagerException("config file is null");
    }

}
