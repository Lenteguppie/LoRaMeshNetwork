package LoRaNetwork.HardwareManagers;

import Extentions.DatabaseManager;
import Extentions.Logger.Log;
import LoRaNetwork.DataSets.Node;

import java.sql.SQLException;
import java.util.ArrayList;

public class NodeManager{

    private static String TAG = NodeManager.class.getSimpleName();
    private final DatabaseManager databaseManager;
    private ArrayList<String> nodeCache = new ArrayList<>();

    public NodeManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    public void processPacket(Node.Packet packet, boolean debug) throws Node.Packet.PacketException {
        if(nodeCache.contains(packet.getNode_uid())){
            savePacket(packet);
        }else{
            if(isRegistered(packet.getNode_uid())){
                savePacket(packet);
                nodeCache.add(packet.getNode_uid());
            }else{
                if(debug) {
                    Log.d(TAG, "Ignored packet with UID: " + packet.getNode_uid());
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

    private void savePacket(Node.Packet packet) throws Node.Packet.PacketException {
        databaseManager.setQueryFormat("INSERT INTO `loraserver`.`node_data` (`node_id`, `message`) VALUES ('%s', '%s');");
        try {
            databaseManager.sendArgs(new String[]{packet.getNode_uid(), packet.toString()});
            Log.d(TAG, "Saved packet: " + packet.toString());
        } catch (SQLException e) {
            if(e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails")){
                nodeCache.remove(packet.getNode_uid());
                Log.d(TAG, "Node removed form DB with uid: " + packet.getNode_uid());
            }else {
                throw new Node.Packet.PacketException("Can't save data packet to db ERROR: " + e.getMessage());
            }
        }
    }

}
