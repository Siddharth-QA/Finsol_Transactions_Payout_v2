package Pages.API_api;

import Utils.PropFileHandler;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.Random;


public class Payoutv2 {
    static PropFileHandler propFileHandler = new PropFileHandler();
    static String token;
    static String txnId;
    static String transferId;
    static String refId;
    String apiBaseURI = propFileHandler.readProperty("apiBaseURI");

    public void getToken() {
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        request.header("Content-Type", "application/json");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");
        requestParams.put("salt", "B9TDPIcBfviCTrH0qLvLPVz7kKaEzQE9");

        request.body(requestParams.toString());
        Response response = request.post("/get-token");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);


        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String message = jsonPathEvaluator.get("data.message");
        Assert.assertEquals(message, "Token Created Successfully");
        token = jsonPathEvaluator.get("data.token");

    }

    public void getBalance() {
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        JSONObject requestParams = new JSONObject();
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");

        request.body(requestParams.toString());
        Response response = request.post("/get-balance");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String message = jsonPathEvaluator.get("data.message");
        Assert.assertEquals(message, "Balance fetched successfully.");
        String currencyCode = jsonPathEvaluator.get("data.amount[0].currencyCode");
        Assert.assertEquals(currencyCode, "AUD");
        String currencyCode2 = jsonPathEvaluator.get("data.amount[1].currencyCode");
        Assert.assertEquals(currencyCode2, "INR");


    }

    public void verifyVPArequired(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "UP");
        requestParams.put("sub_pay_mode", "UP");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "vpa: vpa required when pay_mode is UP.");
    }

    public void verifyBankAccIfscRequired(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
//        beneficiary.put("bank_acc_no", "42628723822");
//        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "bank_acc_no: Bank Account Number and IFSC required when Payment Mode is IM");
    }

    public void verifyImpsPayoutRequiredFields(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
//        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "pay_mode:This field is required.");

        requestParams.clear();

        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
//        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
         response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

         jsonPathEvaluator = response.jsonPath();
         status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
         dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
         status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
         remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "sub_pay_mode:This field is required.");

        requestParams.clear();

        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
//        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        jsonPathEvaluator = response.jsonPath();
        status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "currency: This field is required.");

        requestParams.clear();

        requestParams.clear();
beneficiary.clear();
        requestParams.put("amount", "20");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        jsonPathEvaluator = response.jsonPath();
        status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "name: This field is required.");

    }
    public void verifyDecimalAmt(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "amount:Please enter a valid decimal value.");

//        txnId = jsonPathEvaluator.get("data.txn_id");


    }
    public void sendImpsPayoutRequest(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Accepted");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "0000");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Request Accepted");

        txnId = jsonPathEvaluator.get("data.txn_id");


    }

    public void sendUpiPayoutRequest(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10.01");
        requestParams.put("order_id", "ORDER_87654" +random.nextInt(10000));
        requestParams.put("pay_mode", "UP");
        requestParams.put("sub_pay_mode", "UP");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("vpa", "siddharth@ybl");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Accepted");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "0000");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Request Accepted");

        txnId = jsonPathEvaluator.get("data.txn_id");
    }

    public void makeTransactionSuccesFail(){
        RestAssured.baseURI = "https://spayin.finsol.group/acq/lt5minute";
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.get();
    }

    public void txnNotFountGetStatus(){

        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");


        JSONObject requestParams = new JSONObject();
        requestParams.put("txn_id","3245678976543");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");

        request.body(requestParams.toString());
        Response response = request.post("/get-status");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Error");
//        String status_code = jsonPathEvaluator.get("data.status_code");
//        Assert.assertEquals(status_code, "3333");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Transaction Not Found");


    }
    public void verifyInvalidMid(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");


        JSONObject requestParams = new JSONObject();
//        requestParams.put("txn_id","3245678976543");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");

        request.body(requestParams.toString());
        Response response = request.post("/get-status");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Error");
//        String status_code = jsonPathEvaluator.get("data.status_code");
//        Assert.assertEquals(status_code, "00");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Something went wrong.");

        requestParams.clear();

        requestParams.put("txn_id","3245678976543");
//        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");

        request.body(requestParams.toString());
         response = request.post("/get-status");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

         jsonPathEvaluator = response.jsonPath();
         status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
         dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Error");
//         status_code = jsonPathEvaluator.get("data.status_code");
//        Assert.assertEquals(status_code, "00");
         remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Something went wrong.");
    }

    public void verifyTransactionStatus(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");


        JSONObject requestParams = new JSONObject();
        requestParams.put("txn_id",txnId);
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");

        request.body(requestParams.toString());
        Response response = request.post("/get-status");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");

        if(dataStatus.equals("Failed")){

            String status_code = jsonPathEvaluator.get("data.status_code");
            Assert.assertEquals(status_code, "4444");
            String remarks = jsonPathEvaluator.get("data.remarks");
            Assert.assertEquals(remarks, "Failed at Bank End");
            String rrn = jsonPathEvaluator.get("data.rrn");
            Assert.assertTrue(rrn.isEmpty());
        }

        else if(dataStatus.equals("Success")){
            String status_code = jsonPathEvaluator.get("data.status_code");
//            Assert.assertEquals(status_code, "4444");
            String remarks = jsonPathEvaluator.get("data.remarks");
//            Assert.assertEquals(remarks, "Failed at Bank End");
            System.out.println(status_code);
            System.out.println(remarks);
            String rrn = jsonPathEvaluator.get("data.rrn");
            Assert.assertFalse(rrn.isEmpty());
        }
    }
    public void verifyOrderIdAlreadyExists(){
        RestAssured.baseURI = apiBaseURI;

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Token "+token);
        request.header("Secret-Key", "bxKmsQU7HZN9XdxJttTPq01CzbibTgKCSATOmXNKeYhPYMFXarIymLiQGT181dO4Dt3t1a8Ne774ekynVVtC7wsEJV9CanlALII7GBqkxRe3pLDHPVYOwZis2vwrVW3s");

        Random random = new Random();
        int randNum = random.nextInt(10000);
        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10");
        requestParams.put("order_id", "ORDER_87654" + randNum);
        requestParams.put("pay_mode", "IM");
        requestParams.put("sub_pay_mode", "IM");
        requestParams.put("currency", "INR");
        requestParams.put("merchant_id", "UM1PGA1CZ25YW7E");


        // Construct the beneficiary JSON object
        JSONObject beneficiary = new JSONObject();
        beneficiary.put("bank_acc_no", "42628723822");
        beneficiary.put("ifsc", "IDFC0NOIDAA");
        beneficiary.put("name", "Test User");
        beneficiary.put("email", "success@test.com");
        beneficiary.put("mobile", "1231231231");
        beneficiary.put("country", "AU");
        beneficiary.put("bsb", "123123");

        requestParams.put("beneficiary", beneficiary);

        System.out.println("req body "+ requestParams);
        request.body(requestParams.toString());
        Response response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        String dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Accepted");
        String status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "0000");
        String remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Request Accepted");

        txnId = jsonPathEvaluator.get("data.txn_id");
        request.body(requestParams.toString());
        response = request.post("/pay-request");
        System.out.println("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        jsonPathEvaluator = response.jsonPath();
        status = jsonPathEvaluator.get("status");
        Assert.assertEquals(status, "Success");
        dataStatus = jsonPathEvaluator.get("data.status");
        Assert.assertEquals(dataStatus, "Rejected");
        status_code = jsonPathEvaluator.get("data.status_code");
        Assert.assertEquals(status_code, "3333");
        remarks = jsonPathEvaluator.get("data.remarks");
        Assert.assertEquals(remarks, "Order Id Already Exists");

    }



}
