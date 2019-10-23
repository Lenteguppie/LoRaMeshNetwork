package LoRa;

import EndPoint.WebApiServer;
import Extentions.*;
import Extentions.Logger.Log;
import Extentions.Logger.LoggerHelper;
import LoRa.GatewayServices.GatewayServer;

public class LoRaServices {

    private String TAG = this.getClass().getSimpleName();
    private ApplicationServer applicationServer;
    private GatewayServer gatewayServer;
    private MQTTManager mqttManager;
    private WebApiServer webApiServer;
    private LoggerHelper Logger;
    private DatabaseManager databaseManager;

    public void Init(ConfigManager configManager){
        try {
            this.databaseManager = DatabaseManager.initFromJSON(configManager.getConfigObject("Database"));
            this.gatewayServer = GatewayServer.init(configManager.getConfigObject("GatewayServer"));
            this.applicationServer = ApplicationServer.initFromJSON(configManager.getConfigObject("ApplicationServer"));
            this.mqttManager = MQTTManager.initFromJSON(configManager.getConfigObject("MQTT"));
            this.webApiServer = WebApiServer.initFromJSON(configManager.getConfigObject("WebServerAPI"));
        } catch (ConfigManager.ConfigManagerException | FileManager.FileManagerException.General e) {
            e.printStackTrace();
        }
        addDefaultContent();
        Log.d(TAG, "INIT...\tDONE");
    }

    public void addDefaultContent(){
        applicationServer.addDefaults();
        applicationServer.init();
    }

    public MQTTManager getMqttManager(){
        return mqttManager;
    }

    public GatewayServer getGatewayServer() {
        return gatewayServer;
    }

    public ApplicationServer getApplicationServer() {
        return applicationServer;
    }

    public WebApiServer getWebApiServer() {
        return webApiServer;
    }
}
