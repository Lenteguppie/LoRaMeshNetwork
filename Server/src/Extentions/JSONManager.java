package Extentions;


import Extentions.Logger.Log;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;


public class JSONManager {
    static final public String TAG = JSONManager.class.getSimpleName();

    static public JSONObject parseFileToJSONObject(String filePath) throws FileNotFoundException {
        //Parsing the contents of the JSON file

        //JSON parser object to parse read file
        JSONObject jsonObj = null;

        try (FileReader reader = new FileReader (filePath)) {
            //Read JSON file
            JSONObject obj = new JSONObject(reader);
            jsonObj = obj;
            Log.d (TAG, jsonObj);
            return jsonObj;
        } catch (IOException e) {
            Log.d (TAG, e);
            e.printStackTrace ( );
            return null;
        }

    }

    static public void parseObjectToFile(Object o) {
        ObjectMapper mapper = new ObjectMapper ( );
        String objectName = o.getClass ( ).getSimpleName ( );
        String outPath = "/json/" + objectName + ".json";

        try {
            mapper.writeValue (new File (outPath), o);
            Log.d (TAG, "new JSON file stored at: " + outPath);
        } catch (JsonGenerationException e) {
            e.printStackTrace ( );
        } catch (JsonMappingException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    static public String parseObjectToString(Object o) {
        // Creating Object of ObjectMapper define in Jakson Api
        ObjectMapper Obj = new ObjectMapper ( );

        try {
            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString (o);

            // Displaying JSON String
            Log.d (TAG, jsonStr);
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace ( );
            return null;
        }
    }

    static public JSONObject parseObjectToJSONObject(Object o) throws ParseException {
        // Creating Object of ObjectMapper defined in the Jakson Api
        ObjectMapper Obj = new ObjectMapper ( );
        JSONObject jsonObj;
        String jsonStr;
        try {
            // get Oraganisation object as a json string
            jsonStr = Obj.writeValueAsString (o);

            // Displaying JSON String
            Log.d (TAG, jsonStr);
            return (parseStringToJSONObject (jsonStr));
        } catch (IOException e) {
            e.printStackTrace ( );
            return null;
        }
    }

    static public JSONObject parseStringToJSONObject(String s) {
       return new JSONObject(s);
    }

}

