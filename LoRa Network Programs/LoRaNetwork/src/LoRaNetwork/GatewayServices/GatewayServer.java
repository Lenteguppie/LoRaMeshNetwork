package LoRaNetwork.GatewayServices;

import Extentions.DatabaseManager;
import Extentions.Logger.Log;

import LoRaNetwork.DataSets.Gateway;
import LoRaNetwork.DataSets.Node;
import LoRaNetwork.HardwareManagers.NodeManager;
import org.json.JSONObject;

import java.util.ArrayList;

import static Extentions.ConfigManager.ConfigManagerException;

public class GatewayServer {

    private static String TAG = GatewayServer.class.getSimpleName();
    private final PacketHandler packetHandler;
    private UDPServer udpServer;
    private NodeManager nodeManager;

    public GatewayServer(UDPServer udpServer) {
        this.udpServer = udpServer;
        PacketHandler packetHandler = new PacketHandler();
        packetHandler.addOnPayloadListener(packet -> {
           ArrayList<JSONObject> packets =  Gateway.processPackets(packet);
            try {
                Node.Packet packet1 = Node.processPacket(packets.get(0));
                Log.d(TAG,packet1.getData().toString());
                if(nodeManager != null){
                    nodeManager.processPacket(packet1,false);
                }
            } catch (Node.Packet.PacketException e) {
                e.printStackTrace();
            }
        });
        packetHandler.addOnStatusListener(packet -> {
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
    
    public void addPacketLogger(DatabaseManager databaseManager){
        this.nodeManager = new NodeManager(databaseManager);
    }

    public UDPServer getUdpServer() {
        return udpServer;
    }

    public PacketHandler getHandler(){
        return packetHandler;
    }
}
