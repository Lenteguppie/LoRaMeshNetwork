package LoRaNetwork.DataSets;

import Extentions.DatabaseManager;
import Extentions.Logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Gateway {

    private String brand;
    private String model;
    private String description;
    private int frequency;
    private String location; //stored as json in string
    private String owner;
    private String uid;
    private String mac;

    // region getters and setters
    public String getBrand() {
        return brand;
    }

    private void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    private void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    //endregion

    public Gateway getGateway(String mac, DatabaseManager databaseManager) throws Exception {
        databaseManager.setQueryFormat("SELECT * FROM gateways WHERE mac = '%s';");
        try {
            return fromResultSet(databaseManager.sendArgs(new String[]{mac}));
        } catch (Exception e) {
            throw new Exception("gateway not found in resultSet");
        }
    }

    private static Gateway fromResultSet(ResultSet resultSet) throws Exception {
        resultSet.absolute(1);
        Gateway gateway = new Gateway();
        gateway.setBrand(resultSet.getString("brand"));
        gateway.setModel(resultSet.getString("model"));
        gateway.setDescription(resultSet.getString("description"));
        gateway.setFrequency(resultSet.getInt("frequency"));
        gateway.setLocation(resultSet.getString("location"));
        gateway.setOwner(resultSet.getString("owner"));
        gateway.setUid(resultSet.getString("uid"));
        gateway.setMac(resultSet.getString("mac"));
        return gateway;
    }



    public static Status getStatusObject(JSONObject status){
        return new Status(status);
    }

    public static ArrayList<JSONObject> processPackets(JSONObject totalPacket){
        ArrayList<JSONObject> results = DataProcessor.retrieveNodeRecord(totalPacket);
        if(totalPacket.has("origin")){
            for (JSONObject packet: results) {
                packet.append("origin", totalPacket.get("origin"));
            }
        }
        return results;
    }

    private static class DataProcessor {

        /*+-------------------------------------------------------------------------------------------------------------------------------------------------------------+
         * |USAGE:                                                                                                                                                       |
         * +-------------------------------------------------------------------------------------------------------------------------------------------------------------+
         * | 1. Get the inputJSON which is passed through the gateway                                                                                                    |
         * | 2. Use the retrieveNodeRecord method to receive an ArrayList that stores the most recent record from the passed node messages                               |
         * | 3. If you directly want to get the decrypted message from the node data you can use the static method retrieveNodeMessage with a JSONObject from the record |
         * | 4. If you want to decrypt the message afterwards, you can use the static method decryptNodeMessage with a String parameter                                  |
         * +-------------------------------------------------------------------------------------------------------------------------------------------------------------+
         *  */

        private static final String TAG = DataProcessor.class.getSimpleName();

        static ArrayList<JSONObject> retrieveNodeRecord(boolean debug, JSONObject data) {
            JSONArray array = data.getJSONArray("rxpk");
            if (debug) {
                Log.JSONArray(TAG, array);
            }
            return getJsonObj(array);
        }

        static ArrayList<JSONObject> retrieveNodeRecord(JSONObject data) {
            return retrieveNodeRecord(false, data);
        }
        //Method to generate the ArrayList where the JSONObjects are stored in.
        private static ArrayList<JSONObject> getJsonObj(JSONArray jArr) {
            ArrayList<JSONObject> dataArr = new ArrayList<>();
            try {
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject innerObj = jArr.getJSONObject(i);
                    dataArr.add(innerObj);
                }
            } catch (
                    JSONException e) {
                Log.d(TAG, e);
            }
            return dataArr;
        }
    }

    public static class Status{

        private static String TAG = Gateway.class.getSimpleName() + ":" + Status.class.getSimpleName();

        private String time;
        private double latitude;
        private double longitude;
        private int altitude;
        private int rxfw;
        private int ackr;
        private String mail;
        private String pfrm;
        private int dwnb;
        private int txbn;
        private int rxnb;
        private int rxok;
        private String desc;
        private JSONObject statusObject;
        private String origin;

        Status(JSONObject statusObject){
            if(statusObject.has("stat")) {
                statusObject = statusObject.getJSONObject("stat");
            }
            if(statusObject.has("origin")){
                setOrigin(statusObject.getString("origin"));
            }
            this.statusObject = statusObject;
            try{
                setTime(statusObject.getString("time"));
                setLatitude(statusObject.getDouble("lati"));
                setLongitude(statusObject.getDouble("long"));
                setAltitude(statusObject.getInt("alti"));
                setRxfw(statusObject.getInt("rxfw"));
                setAckr(statusObject.getInt("ackr"));
                setMail(statusObject.getString("mail"));
                setPfrm(statusObject.getString("pfrm"));
                setDwnb(statusObject.getInt("dwnb"));
                setTxbn(statusObject.getInt("txnb"));
                setRxnb(statusObject.getInt("rxnb"));
                setRxok(statusObject.getInt("rxok"));
                setDesc(statusObject.getString("desc"));
            }catch (Exception e){
                Log.E(TAG,e);
            }
        }

        //region setters and getters
        //region time | string | UTC 'system' time of the gateway, ISO 8601 'expanded' format
        public String getTime() {
            return time;
        }

        void setTime(String time) {
            this.time = time;
        }
        //endregion

        //region lati | number | GPS latitude of the gateway in degree (float, N is +)
        public double getLatitude() {
            return latitude;
        }

        void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        //endregion

        //region long | number | GPS latitude of the gateway in degree (float, E is +)
        public double getLongitude() {
            return longitude;
        }

        void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        //endregion

        //region alti | number | GPS altitude of the gateway in meter RX (integer)
        public int getAltitude() {
            return altitude;
        }

        void setAltitude(int altitude) {
            this.altitude = altitude;
        }
        //endregion

        //region rxfw | number | Number of radio packets forwarded (unsigned integer)
        public int getRxfw() {
            return rxfw;
        }

        void setRxfw(int rxfw) {
            this.rxfw = rxfw;
        }
        //endregion

        //region ackr | number | Percentage of upstream datagrams that were acknowledged
        public int getAckr() {
            return ackr;
        }

        void setAckr(int ackr) {
            this.ackr = ackr;
        }
        //endregion

        //region mail | string | email of owner gateway
        public String getMail() {
            return mail;
        }

        void setMail(String mail) {
            this.mail = mail;
        }
        //endregion

        //region pfrm | string | get platform / model name
        public String getPfrm() {
            return pfrm;
        }

        void setPfrm(String pfrm) {
            this.pfrm = pfrm;
        }
        //endregion

        //region dwnb | number | Number of downlink datagrams received (unsigned integer)
        public int getDwnb() {
            return dwnb;
        }

        void setDwnb(int dwnb) {
            this.dwnb = dwnb;
        }
        //endregion

        //region txnb | number | Number of packets emitted (unsigned integer)
        public int getTxbn() {
            return txbn;
        }

        void setTxbn(int txbn) {
            this.txbn = txbn;
        }
        //endregion

        //region rxnb | number | Number of radio packets received (unsigned integer)
        public int getRxnb() {
            return rxnb;
        }

        void setRxnb(int rxnb) {
            this.rxnb = rxnb;
        }
        //endregion

        //region rxok | number | Number of radio packets received with a valid PHY CRC
        public int getRxok() {
            return rxok;
        }

        void setRxok(int rxok) {
            this.rxok = rxok;
        }
        //endregion

        //region desc | string | unknown function ??
        public String getDesc() {
            return desc;
        }

        void setDesc(String desc) {
            this.desc = desc;
        }
        //endregion

        public JSONObject getStatusObject() {
            return statusObject;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        //endregion

        @Override
        public String toString() {
            return "Gateway Status: " + getStatusObject().toString();
        }




    }

}



