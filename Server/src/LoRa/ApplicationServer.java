package LoRa;

import Extentions.Logger.Log;
import Extentions.Logger.LoggerHelper;
import Extentions.Webserver.PageHandler;
import Extentions.Webserver.Webserver;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;

import static Extentions.ConfigManager.ConfigManagerException;
import static Extentions.Webserver.PageHandler.*;

public class ApplicationServer extends Webserver {

    private static String TAG = ApplicationServer.class.getSimpleName();
    private LoggerHelper Logger;

    private ApplicationServer(int port) {
        super(port);
    }

    public static ApplicationServer initFromJSON(JSONObject configObject) throws ConfigManagerException {
        Log.d(TAG,"LOADING...", true);
        if(configObject != null) {
            try {
                ApplicationServer applicationServer =  new ApplicationServer(Math.toIntExact(configObject.getInt("Port")));
                Log.e("DONE");
                return applicationServer;
            }catch (NullPointerException e){
                Log.e("FAILED");
                throw new ConfigManagerException("invalid config file fields");
            }
        }
        Log.e("FAILED");
        throw new ConfigManagerException("config file is null");
    }

    public void addDefaults(){
        addPageListener("/", page -> {
            page.handle("handled ;)");
        });
        addPageListener("/testPage", page -> {
            if(page.hasArgs()){
                page.handle(page.getArguments().toString());
            }else{
                page.handle("");
            }
        });
    }

    public void init(){
        new Thread(this).start();
    }
}
