package LoRa;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import Extentions.JSONManager;
import Extentions.Logger.Log;
import org.json.*;

/*+-------------------------------------------------------------------------------------------------------------------------------------------------------------+
 * |USAGE:                                                                                                                                                       |
 * +-------------------------------------------------------------------------------------------------------------------------------------------------------------+
 * | 1. Get the inputJSON which is passed through the gateway                                                                                                    |
 * | 2. Use the retrieveNodeRecord method to receive an ArrayList that stores the most recent record from the passed node messages                               |
 * | 3. If you directly want to get the decrypted message from the node data you can use the static method retrieveNodeMessage with a JSONObject from the record |
 * | 4. If you want to decrypt the message afterwards, you can use the static method decryptNodeMessage with a String parameter                                  |
 * +-------------------------------------------------------------------------------------------------------------------------------------------------------------+
 *  */


public class DataProcessor {
    static final public String TAG = DataProcessor.class.getSimpleName();
    private String inputJSON;

    /* Getters and Setters */
    public String getInputJSON() {
        return inputJSON;
    }

    public void setData(String inputJSON) {
        this.inputJSON = inputJSON;
    }

    /*Methods to get the required data from the inputJSON*/

    // Method to parse the input JSON String to a JSONObject
    private JSONObject dataToJSON() {
        JSONObject jo = JSONManager.parseStringToJSONObject(inputJSON);
        return jo;
    }

    //Method to retrieve the decrypted message from the incomming node message
    public static String retrieveNodeMessage(JSONObject jsonObject) {
        String data = decryptNodeMessage((String) jsonObject.get("data"));
        Log.d(TAG, data);
        return data;

    }

    // retrieve the record that was passed through from the Gateway in an ArrayList
    public ArrayList<JSONObject> retrieveNodeRecord() {
        return retrieveNodeRecord(false);
    }

    public ArrayList<JSONObject> retrieveNodeRecord(boolean debug) {
        JSONObject jsonObject = dataToJSON();
        JSONArray array = jsonObject.getJSONArray("rxpk");
        if(debug) {
            Log.JSONArray(TAG, array);
        }
        return getJsonObj(array);
    }


    //Method to generate the ArrayList where the JSONObjects are stored in.
    private ArrayList<JSONObject> getJsonObj(JSONArray jArr) {
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

    /*Decrypt the encrypted data which is encrypted with the Base64 encryption*/
    private static String decryptNodeMessage(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}