package order.pizza.tests;


import helpers.AuthHelper;
import helpers.RequestAndResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.*;

import java.util.HashMap;
import java.util.Map;

public class OrderPizzaApiTest {
    String baseURI = "https://order-pizza-api.herokuapp.com/api/";
    String token;
    Map<String, Object> headerMap = new HashMap<String, Object>();
    int size;
    int order_id;
    boolean loggingFlag = true;


    @BeforeTest
    public void setUp() {
        token = AuthHelper.generateToken();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization","Bearer "+token);
    }

    public int getOrderSize(){
        Response response = RequestAndResponse.getResponse("Get", baseURI+"orders", "", headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_OK);
        int newSize = response.jsonPath().getList("Order_ID").size();

        return newSize;
    }

    @Test(priority = 1)
    public void testGetOrderSize(){
        size = getOrderSize();
        Assert.assertTrue(size>0);
    }

    @Test(priority = 2)
    public void testPlaceOrder(){
        String placeOrderBody = "{\n" +
                "  \"Crust\": \"thin\",\n" +
                "  \"Flavor\": \"veggies\",\n" +
                "  \"Size\": \"S\",\n" +
                "  \"Table_No\": 11\n" +
                "}";
        Response response = RequestAndResponse.getResponse("Post", baseURI+"orders", placeOrderBody, headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_CREATED);
        order_id = response.then().extract().path("Order_ID");
        int newSize = getOrderSize();
        Assert.assertEquals(size+1,newSize);
    }

    @Test(priority = 3)
    public void testPlaceConflictOrder(){
        String placeOrderBody = "{\n" +
                "  \"Crust\": \"thin\",\n" +
                "  \"Flavor\": \"veggies\",\n" +
                "  \"Size\": \"S\",\n" +
                "  \"Table_No\": 11\n" +
                "}";
        Response response = RequestAndResponse.getResponse("Post", baseURI+"orders", placeOrderBody, headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_CONFLICT);
        String detail = response.then().extract().path("detail");
        int newSize = getOrderSize();
        Assert.assertEquals(size+1,newSize);
        Assert.assertTrue(detail.equalsIgnoreCase("Order for Table No. 11 exists already "));
    }

    @Test(priority = 4)
    public void testDeleteOrder(){
        Response response = RequestAndResponse.getResponse("Delete", baseURI+"orders/"+order_id, "", headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_OK);
        String message = response.then().extract().path("message");
        Assert.assertTrue(message.equalsIgnoreCase("Order deleted"));
        int newSize = getOrderSize();
        Assert.assertEquals(size,newSize);
    }

    @Test(priority = 5)
    public void testDeleteOrderNotPresent(){
        Response response = RequestAndResponse.getResponse("Delete", baseURI+"orders/"+order_id, "", headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
        String message = response.then().extract().path("detail");
        Assert.assertTrue(message.equalsIgnoreCase("Order not found for ID: "+order_id));
        int newSize = getOrderSize();
        Assert.assertEquals(size,newSize);
    }

    @Test(priority = 6)
    public void testUnAuthorizedAccessOnPost(){
        Map<String, Object> headerMapNew = new HashMap<String, Object>();
        headerMapNew.put("Content-Type", "application/json");
        headerMapNew.put("Accept", "application/json");
        headerMapNew.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzM5MzA3MzUsIm5iZiI6MTYzMzkzMDczNSwianRpIjoiMWM2OWQzZWYtZmZmZi00MDZhLTgxNTItZjk3ODljZjIy" +
                "MGYyIiwiZXhwIjoxNjMzOTMxNjM1LCJpZGVudGl0eSI6InRlc3QiLCJmcmVzaCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MifQ.YEfokMwLq4UWNLL0RHPFdGKVsO8NzwXo_8Xnz_mT9kE");
        String placeOrderBody = "{\n" +
                "  \"Crust\": \"thin\",\n" +
                "  \"Flavor\": \"veggies\",\n" +
                "  \"Size\": \"S\",\n" +
                "  \"Table_No\": 11\n" +
                "}";
        Response response = RequestAndResponse.getResponse("Post", baseURI+"orders", placeOrderBody, headerMapNew,loggingFlag);
        Assert.assertEquals(response.getStatusCode(),401);
    }

    @Test(priority = 7)
    public void testUnAuthorizedAccessOnGet(){
        Map<String, Object> headerMapNew = new HashMap<String, Object>();
        headerMapNew.put("Content-Type", "application/json");
        headerMapNew.put("Accept", "application/json");
        headerMapNew.put("Authorization","Bearer token");

        Response response = RequestAndResponse.getResponse("Get", baseURI+"orders", "", headerMapNew,loggingFlag);
        Assert.assertEquals(response.getStatusCode(),401);
    }

    @Test(priority = 8)
    public void testUnAuthorizedAccessOnDelete(){
        Map<String, Object> headerMapNew = new HashMap<String, Object>();
        headerMapNew.put("Content-Type", "application/json");
        headerMapNew.put("Accept", "application/json");
        headerMapNew.put("Authorization","Bearer token");
        String placeOrderBody = "{\n" +
                "  \"Crust\": \"thin\",\n" +
                "  \"Flavor\": \"veggies\",\n" +
                "  \"Size\": \"S\",\n" +
                "  \"Table_No\": 11\n" +
                "}";
        Response response = RequestAndResponse.getResponse("Post", baseURI+"orders", placeOrderBody, headerMap,loggingFlag);
        response.then().statusCode(HttpStatus.SC_CREATED);
        order_id = response.then().extract().path("Order_ID");

        response = RequestAndResponse.getResponse("Delete", baseURI+"orders/"+order_id, "", headerMapNew,loggingFlag);
        Assert.assertEquals(response.getStatusCode(),401);
    }
}
