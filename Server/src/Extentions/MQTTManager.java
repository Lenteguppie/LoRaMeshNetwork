package Extentions;


import Extentions.Logger.Log;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class MQTTManager {

    private static final String TAG = MQTTManager.class.getSimpleName();
    private MqttConnectOptions options;
    private MqttClient client;
    private String broker = "";
    private String clientId = "LoRaMeshServerWorker";

    public MQTTManager(String url, String username, String password) {

        this.options = new MqttConnectOptions(); // Initiate the connection options for the MQTT connection

        this.broker = "tcp://" + url; // Construct the ip to connect to the MQTT broker

        try {
            this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        } catch (MqttException me) {
            exceptionToString(me);
        }
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        connect();
    }

    public String getBrokerAddress(){
        return broker;
    }

    private void connect() {
        try {
            client.connect(options);
            Log.d("MQTT connection", "Established!");
        } catch (MqttException e) {
            exceptionToString(e);
            Log.d("MQTT connection", "Revoked!");
        }
    }

    public void publish(String topic, String content, int qos) {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
            client.publish(topic, message);
            Log.d(TAG, "Publish:\t" + message.toString());
        } catch (MqttException e) {
            exceptionToString(e);
        }
    }

    public void publish(String topic, String content) {
        MqttMessage message = new MqttMessage(content.getBytes());
        try {
            client.publish(topic, message);
            Log.d(TAG, "Publish:\t" + message.toString());
        } catch (MqttException e) {
            exceptionToString(e);
        }
    }

    public void subscribe() {

    }

    private static void exceptionToString(MqttException e) {
        Log.E("reason ", e.getReasonCode());
        Log.E("msg ", e.getMessage());
        Log.E("loc ", e.getLocalizedMessage());
        Log.E("cause ", (Exception) e.getCause());
        Log.E("excep ", e);
        e.printStackTrace();

    }

    public static MQTTManager initFromJSON(JSONObject mqttConfig) throws ConfigManager.ConfigManagerException {
        Log.d(TAG, "LOADING...", true);
        if(mqttConfig != null) {
            try {
                MQTTManager mqttHelper;
                String brokerIp = (mqttConfig.get("Address") + ":" + mqttConfig.get("Port"));
                mqttHelper = new MQTTManager(brokerIp, (String) mqttConfig.get("Username"), (String) mqttConfig.get("Password"));
                Log.e("DONE");
                return mqttHelper;
            }catch (NullPointerException e){
                Log.e("FAILED");
                e.printStackTrace();
            }
        }
        throw new ConfigManager.ConfigManagerException("config file is null");
    }
}


