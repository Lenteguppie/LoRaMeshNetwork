package LoRaNetwork.DataSets;

import Extentions.DatabaseManager;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;

public class Node{
    private String name;
    private String brand;
    private int frequency;
    private String location;
    private String owner;
    private String uid;

    //region general Setters and Getters
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    private void setBrand(String brand) {
        this.brand = brand;
    }

    public int getFrequency() {
        return frequency;
    }

    private void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getLocation() {
        return location;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    private void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUid() {
        return uid;
    }

    private void setUid(String uid) {
        this.uid = uid;
    }
    //endregion

    public static Node getNode(String uid, DatabaseManager databaseManager) throws Exception {
        databaseManager.setQueryFormat("SELECT * FROM nodes WHERE uid = '%s';");
        try {
            return fromResultSet(databaseManager.sendArgs(new String[]{uid}));
        } catch (SQLException e) {
            throw new Exception("Node not found in resultSet");
        }
    }

    private static Node fromResultSet(ResultSet resultSet) throws SQLException {
        resultSet.absolute(1);
        Node node = new Node();
        node.setName(resultSet.getString("name"));
        node.setBrand(resultSet.getString("brand"));
        node.setFrequency(resultSet.getInt("frequency"));
        node.setLocation(resultSet.getString("location"));
        node.setOwner(resultSet.getString("owner"));
        node.setUid(resultSet.getString("uid"));
        return node;
    }

    public static Packet processPacket(JSONObject packetObject) throws Packet.PacketException {
        return new Packet(packetObject);
    }

    public static class Packet{
        private String node_uid;
        private String modulation;
        private int status;
        private int RSSI;
        private DataPacket data;
        private int frequency;
        private int timeStamp;
        private int RFChain;
        private String CRIdentifier;
        private int size;
        private String DRIdentifier;
        private int SNR_ration;
        private String time;
        private int IFChannel;

        private JSONObject jsonPacket;

        Packet(JSONObject packetObject) throws PacketException {
            if(packetObject.has("rxpk")){
                throw new PacketException("More packets found in node packet can only be 1 node data packet");
            }
            this.jsonPacket = packetObject;
            try {
                setModulation(packetObject.getString("modu"));
                setStatus(packetObject.getInt("stat"));
                setRSSI(packetObject.getInt("rssi"));
                setData(new DataPacket(packetObject.getString("data")));
                //setNode_uid(data.getUID());//TODO LETOP VOOR DEBUG ROBIN
                setFrequency(packetObject.getInt("freq"));
                setTimeStamp(packetObject.getInt("tmst"));
                setRFChain(packetObject.getInt("rfch"));
                setCRIdentifier(packetObject.getString("codr"));
                setSize(packetObject.getInt("size"));
                setDRIdentifier(packetObject.getString("datr"));
                setSNR_ration(packetObject.getInt("lsnr"));
                setTime(packetObject.getString("time"));
                setIFChannel(packetObject.getInt("chan"));
            }catch (Exception e){
                throw new PacketException(e.getMessage());
            }
        }

        //region  modu | string | Modulation identifier "LORA" or "FSK"
        public String getModulation() {
            return modulation;
        }

        private void setModulation(String modulation) {
            this.modulation = modulation;
        }
        //endregion

        //region stat | number | CRC status: 1 = OK, -1 = fail, 0 = no CRC
        public int getStatus() {
            return status;
        }

        private void setStatus(int status) {
            this.status = status;
        }
        //endregion

        //region  rssi | number | RSSI in dBm (signed integer, 1 dB precision)
        public int getRSSI() {
            return RSSI;
        }

        private void setRSSI(int RSSI) {
            this.RSSI = RSSI;
        }
        //endregion

        //region data | string | Base64 encoded RF packet payload, padded
        public DataPacket getData() {
            return data;
        }

        private void setData(DataPacket data) {
            this.data = data;
        }
        //endregion

        //region freq | number | RX central frequency in MHz (unsigned float, Hz precision)
        public int getFrequency() {
            return frequency;
        }

        private void setFrequency(int frequency) {
            this.frequency = frequency;
        }
        //endregion

        //region tmst | number | Internal timestamp of "RX finished" event (32b unsigned)
        public int getTimeStamp() {
            return timeStamp;
        }

        private void setTimeStamp(int timeStamp) {
            this.timeStamp = timeStamp;
        }
        //endregion

        //region rfch | number | Concentrator "RF chain" used for RX (unsigned integer)
        public int getRFChain() {
            return RFChain;
        }

        private void setRFChain(int RFChain) {
            this.RFChain = RFChain;
        }
        //endregion

        //region codr | string | LoRa ECC coding rate identifier
        public String getCRIdentifier() {
            return CRIdentifier;
        }

        private void setCRIdentifier(String CRIdentifier) {
            this.CRIdentifier = CRIdentifier;
        }
        //endregion

        //region size | number | RF packet payload size in bytes (unsigned integer)
        public int getSize() {
            return size;
        }

        private void setSize(int size) {
            this.size = size;
        }
        //endregion

        //region datr | string | LoRa datarate identifier (eg. SF12BW500)
        public String getDRIdentifier() {
            return DRIdentifier;
        }

        private void setDRIdentifier(String DRIdentifier) {
            this.DRIdentifier = DRIdentifier;
        }
        //endregion

        //region lsnr | number | Lora SNR ratio in dB (signed float, 0.1 dB precision)
        public int getSNR_ration() {
            return SNR_ration;
        }

        private void setSNR_ration(int SNR_ration) {
            this.SNR_ration = SNR_ration;
        }
        //endregion

        //region chan | number | Concentrator "IF" channel used for RX (unsigned integer)
        public int getIFChannel() {
            return IFChannel;
        }

        private void setIFChannel(int channel) {
            this.IFChannel = channel;
        }
        //endregion

        //region time | string | UTC time of pkt RX, us precision, ISO 8601 'compact' format
        public String getTime() {
            return time;
        }

        private void setTime(String time) {
            this.time = time;
        }
        //endregion

        public String getNode_uid() {
            return node_uid;
        }

        private void setNode_uid(String node_uid) {
            this.node_uid = node_uid;
        }

        //region DataPacket class for transferring encode data en decoding data
        public static class DataPacket{
            private String dataEncoded;
            private String dataDecoded;
            private HashMap<String, String> dataDecodedMap;

            DataPacket(String dataEncoded){
                this.dataEncoded = dataEncoded;
            }

            HashMap<String, String> decode(){
                if(dataDecoded == null){
                    dataDecoded = decryptNodeMessage(dataEncoded);
                }
                dataDecodedMap = createDecodedMap(dataDecoded);
                return dataDecodedMap;
            }

            /*Decrypt the encrypted data which is encrypted with the Base64 encryption*/
            private String decryptNodeMessage(String encodedString) {
                byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
                return new String(decodedBytes);
            }

            String getUID() throws PacketException {
                if(dataDecodedMap == null || dataDecodedMap.isEmpty()){
                    decode();
                }
                if(!dataDecodedMap.isEmpty() && dataDecodedMap.containsKey("uid")){
                    return dataDecodedMap.get("uid");
                }else{
                    throw new Node.Packet.PacketException("Missing device identifier");
                }
            }

            private static HashMap<String, String> createDecodedMap(String URI) {
                HashMap<String, String> result = new HashMap<>();
                for (String param : URI.split("&")) {
                    String[] entry = param.split("=");
                    if (entry.length > 1) {
                        result.put(entry[0], entry[1]);
                    }
                }
                return result;
            }

            @Override
            public String toString() {
                if(dataDecoded == null){
                    decode();
                }
                return dataDecoded;
            }
        }
        //endregion

        private JSONObject getJsonPacket() {
            return jsonPacket;
        }

        public static class PacketException extends Exception{
            public PacketException(String message){
                super(message);
            }
        }

        @Override
        public String toString() {
            return getJsonPacket().toString();
        }
    }
}