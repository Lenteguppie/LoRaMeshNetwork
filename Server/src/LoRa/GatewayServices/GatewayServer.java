package LoRa.GatewayServices;

import Extentions.Logger.Log;
import LoRa.DataProcessor;
import LoRa.DataSets.Gateway;
import LoRa.DataSets.Node;
import org.json.JSONObject;

import java.util.ArrayList;

import static Extentions.ConfigManager.ConfigManagerException;

public class GatewayServer {

    private static String TAG = GatewayServer.class.getSimpleName();
    private final PacketHandler packetHandler;
    private UDPServer udpServer;

    private GatewayServer(UDPServer udpServer) {
        this.udpServer = udpServer;
        PacketHandler packetHandler = new PacketHandler();
        packetHandler.addOnPayloadListener(packet -> {
            DataProcessor dp = new DataProcessor();
            dp.setData(packet.toString());
            ArrayList<JSONObject> res = dp.retrieveNodeRecord();
            try {
                Node.Packet packet1 = Node.processPacket(res.get(0));
                Log.d(TAG, packet1.toString());
                Log.d(TAG, "PACKET DATA DECODED: " + packet1.getData().decode());
            } catch (Node.Packet.PacketException e) {
                e.printStackTrace();
            }

        });

        packetHandler.addOnStatusListener(packet -> {
            Log.d(TAG, "STATUS DATA: " + packet.toString());
            Gateway.Status status = Gateway.getStatusObject(packet);
            Log.d(TAG, status.toString());
        });
        this.packetHandler = packetHandler;
        udpServer.setPacketHandler(packetHandler);
        Log.d(TAG, "Gateway server initialized!");
        new Thread(udpServer).start();
        Log.d(TAG, "Listening for gateway traffic");
    }

    public static GatewayServer init(JSONObject config) throws ConfigManagerException {
        UDPServer udpServer = UDPServer.initFromJSON(config);
        return new GatewayServer(udpServer);
    }


    public UDPServer getUdpServer() {
        return udpServer;
    }

    public PacketHandler getHandler(){
        return packetHandler;
    }
}
