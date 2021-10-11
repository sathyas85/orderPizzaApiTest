package helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;


import java.util.Map;

public class RequestAndResponse {


    public static Response getResponse(String method, String url,  String body, Map headerMap,boolean loggingFlag) {

        Response responseObj = null;
        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        if (headerMap != null) {
            request.headers(headerMap);
        }
        request.body(body);
        if(loggingFlag) {
            if (method.equalsIgnoreCase("POST")) {
                responseObj = request.post(url).then().log().all().extract().response();
            } else if (method.equalsIgnoreCase("PUT")) {
                responseObj = request.put(url).then().log().all().extract().response();
            } else if (method.equalsIgnoreCase("GET")) {
                responseObj = request.get(url).then().log().all().extract().response();
            } else if (method.equalsIgnoreCase("DELETE")) {
                responseObj = request.delete(url).then().log().all().extract().response();
            }
        }else{
            if (method.equalsIgnoreCase("POST")) {
                responseObj = request.post(url).then().extract().response();
            } else if (method.equalsIgnoreCase("PUT")) {
                responseObj = request.put(url).then().extract().response();
            } else if (method.equalsIgnoreCase("GET")) {
                responseObj = request.get(url).then().extract().response();
            } else if (method.equalsIgnoreCase("DELETE")) {
                responseObj = request.delete(url).then().extract().response();
            }
        }


        return responseObj;
    }


}