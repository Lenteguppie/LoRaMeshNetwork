package LoRaNetwork.HardwareManagers;

import Extentions.DatabaseManager;
import Extentions.Logger.Log;
import LoRaNetwork.DataSets.Gateway;
import LoRaNetwork.DataSets.Node;

import java.sql.SQLException;
import java.util.ArrayList;

public class GatewayManager {
    private static String TAG = NodeManager.class.getSimpleName();
    private final DatabaseManager databaseManager;
    private ArrayList<String> gatewayCache = new ArrayList<>();

    public GatewayManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    public void processPacket(Gateway.Status packet, boolean debug) throws Node.Packet.PacketException {
        if(gatewayCache.contains(packet.getOrigin())){
            savePacket(packet);
        }else{
            if(isRegistered(packet.getOrigin())){
                savePacket(packet);
                gatewayCache.add(packet.getOrigin());
            }else{
                if(debug) {
                    Log.d(TAG, "Ignored packet with UID: " + packet.getOrigin());
                }
            }
        }
    }

    private boolean isRegistered(String uid){
        try {
            Node node = Node.getNode(uid, databaseManager);
            return node.getUid().equals(uid);
        } catch (Exception e) {
            return false;
        }
    }

    private void savePacket(Gateway.Status packet) throws Node.Packet.PacketException {
        databaseManager.setQueryFormat("INSERT INTO `loraserver`.`node_data` (`node_id`, `message`) VALUES ('%s', '%s');");
        try {
            databaseManager.sendArgs(new String[]{packet.getOrigin(), packet.toString()});
            Log.d(TAG, "Saved packet: " + packet.toString());
        } catch (SQLException e) {
            if(e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails")){
                gatewayCache.remove(packet.getOrigin());
                Log.d(TAG, "Node removed form DB with uid: " + packet.getOrigin());
            }else {
                throw new Node.Packet.PacketException("Can't save data packet to db ERROR: " + e.getMessage());
            }
        }
    }
}
