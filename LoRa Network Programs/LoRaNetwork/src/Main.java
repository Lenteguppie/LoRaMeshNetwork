import Extentions.DatabaseManager;
import Extentions.Logger.LoggerHelper;
import LoRaNetwork.GatewayServices.GatewayServer;
import LoRaNetwork.GatewayServices.PacketHandler;
import LoRaNetwork.GatewayServices.UDPServer;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        LoggerHelper loggerHelper = new LoggerHelper();
        try {
            loggerHelper.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }

        try {
            GatewayServer gatewayServer = new GatewayServer(new UDPServer(InetAddress.getByName("192.168.1.18"),1700));
            DatabaseManager databaseManager = new DatabaseManager("localhost", "loraserver");
            databaseManager.setLogin("root","");
            databaseManager.connect();
            gatewayServer.addPacketLogger(databaseManager);
        } catch (UnknownHostException | DatabaseManager.DatabaseException.IsOffline e) {
            e.printStackTrace();
        }
    }

}
