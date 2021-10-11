package helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Arrays;

public class Utilities {
    public static JSONObject convertStringtoJSONObject (String jObject){
        try {
            JSONObject requestparams = new JSONObject();

            JSONParser jsonParser = new JSONParser();
            requestparams = (JSONObject) jsonParser.parse(jObject);

            return requestparams;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object getValue(JSONObject jsonObject, String[] keys) throws Exception {
        String currentKey = keys[0];
        Object result = new Object();

        if (keys.length == 1 && jsonObject.containsKey(currentKey)) {
            return jsonObject.get(currentKey);
        } else if (keys.length == 1 &&!jsonObject.containsKey(currentKey)) {
            //throw new Exception(currentKey + "is not a valid key.");
            return null;
        }
        //JSONArray jsonArray = (JSONArray) jsonObject.get(currentKey);
        Object nextObj = jsonObject.get(currentKey);
        int nextKeyIdx = 1;
        String[] remainingKeys = Arrays.copyOfRange(keys, nextKeyIdx, keys.length);
        if(nextObj instanceof JSONArray){
            for (int len = 0;len < ((JSONArray) nextObj).size();len++){
                JSONObject nextJsonObj = (JSONObject) ((JSONArray) nextObj).get(len);
                result =   getValue(nextJsonObj, remainingKeys);
                if(result == null){
                    len=  ((JSONArray) nextObj).size();
                }
            }
        }
        else {
            //JSONObject nestedJsonObjectVal = (JSONObject) jsonObject.get(currentKey);
            result = getValue((JSONObject) nextObj, remainingKeys);
            //jsonObject.put(currentKey, updatedNestedValue);

        }
        return result;
    }
}
