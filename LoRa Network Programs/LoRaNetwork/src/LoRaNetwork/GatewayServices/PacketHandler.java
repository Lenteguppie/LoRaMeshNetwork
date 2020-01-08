package LoRaNetwork.GatewayServices;

import org.json.JSONObject;

import java.util.ArrayList;

public class PacketHandler {

    private ArrayList<OnPacketListener> onPacketListeners = new ArrayList<>();
    private ArrayList<OnStatusListener> onStatusListeners = new ArrayList<>();

    public void addOnPayloadListener(OnPacketListener listener){
        onPacketListeners.add(listener);
    }

    public void addOnStatusListener(OnStatusListener listener){onStatusListeners.add(listener);}

    public void createOnPacketEvent(JSONObject packet){
        for (OnPacketListener listener: onPacketListeners) {
            listener.onPacket(packet);
        }
    }

    public void createOnStatusEvent(JSONObject packet){
        for (OnStatusListener listener: onStatusListeners) {
            listener.onStatus(packet);
        }
    }
    public interface OnPacketListener{
        void onPacket(JSONObject packet);
    }

    public interface OnStatusListener{
        void onStatus(JSONObject packet);
    }

}
