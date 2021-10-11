package helpers;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthHelper {
    public static String generateToken() {
        //LoadPropertiesFileUtil.loadPropertiesFile();
        String baseURI = "https://order-pizza-api.herokuapp.com/api/auth";

        JSONObject requestparams = new JSONObject();
        String jObject = "{\n" +
                "  \"password\": \"test\",\n" +
                "  \"username\": \"test\"\n" +
                "}";
        requestparams = Utilities.convertStringtoJSONObject(jObject);

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        Response response = RequestAndResponse.getResponse("Post", baseURI, jObject, headerMap,true);
        response.then().statusCode(HttpStatus.SC_OK);
        String token = response.then().extract().path("access_token").toString();
        return token;
    }
}
