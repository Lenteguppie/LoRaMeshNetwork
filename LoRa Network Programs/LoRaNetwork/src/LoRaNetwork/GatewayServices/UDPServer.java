package LoRaNetwork.GatewayServices;

import Extentions.ConfigManager;
import Extentions.Logger.Log;
import LoRaNetwork.DataSets.Gateway;
import LoRaNetwork.DataSets.Node;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPServer implements Runnable{

    private static String TAG = UDPServer.class.getSimpleName();
    private byte[] receiveData = new byte[1024];
    private DatagramSocket serverSocket = null;
    private PacketHandler packetHandler;

    public UDPServer(InetAddress address, int port) {
        try {
            serverSocket = new DatagramSocket(port, address);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void setPacketHandler(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    @Override
    public void run() {
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
            String sentence = new String(receivePacket.getData());
            try {
                sentence = sentence.substring(sentence.indexOf('{'));
            }catch (StringIndexOutOfBoundsException e){
                Log.d(TAG, e);
            }
            try {
                JSONObject obj = new JSONObject(sentence);
                obj.append("origin", getMacAddressByUseArp(receivePacket.getAddress().getHostAddress()));
                if(obj.has("stat")) {
                    packetHandler.createOnStatusEvent(obj);
                }else{
                    ArrayList<JSONObject> packets =  Gateway.processPackets(obj);
                    Node.Packet packet1 = Node.processPacket(packets.get(0));
                    Log.d(TAG,packet1.toString());
                    packetHandler.createOnPacketEvent(obj);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.E(TAG, sentence);
            }
        }
    }

    static UDPServer initFromJSON(JSONObject udpServerConfig) throws ConfigManager.ConfigManagerException {
        Log.d(TAG, "LOADING...", true);
        if(udpServerConfig != null) {
            try {
                InetAddress address = InetAddress.getByName(udpServerConfig.getString("Address"));
                return new UDPServer(address, Math.toIntExact(udpServerConfig.getInt("Port")));
            } catch (UnknownHostException e) {
                throw new ConfigManager.ConfigManagerException("invalid configuration object");
            }
        }
        throw new ConfigManager.ConfigManagerException("config file is null");
    }

    private static String getMacAddressByUseArp(String ip) throws IOException {
        String cmd = "arp -a " + ip;
        Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream());
        String str = null;
        Pattern pattern = Pattern.compile("(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4})");
        try {
            while (s.hasNext()) {
                str = s.next();
                Matcher matcher = pattern.matcher(str);
                if (matcher.matches()){
                    break;
                }
                else{
                    str = null;
                }
            }
        }
        finally {
            s.close();
        }
        if(str != null){
            return str.replaceAll("-","");
        }
        return null;
    }

}