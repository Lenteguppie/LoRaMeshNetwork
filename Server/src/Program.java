import Extentions.*;
import Extentions.Logger.Log;
import Extentions.Logger.LoggerHelper;
import LoRa.DataSets.Node;
import LoRa.GatewayServices.PacketHandler;
import LoRa.LoRaServices;
import Users.User;
import Users.UserData;
import Users.UserManager;
import org.json.JSONObject;

import java.io.IOException;

import static Extentions.ConfigManager.*;

public class Program {
    private static String TAG = Program.class.getSimpleName();

    public static void main(String[] args) {

        LoggerHelper loggerHelper = new LoggerHelper();
        try {
            loggerHelper.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        ConfigManager configManager = new ConfigManager("config.json");
        LoRaServices services = new LoRaServices();
        services.Init(configManager);
        Log.d( TAG,"Listening for gateway payloads");
    }
}

