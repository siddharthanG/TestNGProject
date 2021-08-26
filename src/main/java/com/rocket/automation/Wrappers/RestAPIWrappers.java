package com.rocket.automation.Wrappers;



import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigBuilder;

import com.rocket.automation.Utils.DataLoader;
import com.rocket.automation.Utils.JsonFormater;
import com.rocket.automation.Utils.LoggerUtil;
import com.rocket.automation.Utils.PropertyLoader;
import io.restassured.path.json.JsonPath;
import com.jayway.jsonpath.Configuration;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.rocket.automation.Utils.Constant.REST_PAYLOAD;
import static org.hamcrest.Matchers.*;


// created by sid and thirveni
public class RestAPIWrappers {

    protected String QueryParam = null;

    protected String JsonResponse;
    protected JsonPath jp;
    public PropertyLoader propLoader;
    protected JsonFormater jsonFormater;
    protected Response response;
    protected RequestSpecification request;
    protected File file;
    protected ValidatableResponse responseTable;
    protected Map<String, String> parameters;
    DataLoader dataLoader;
    private int callExecution = 0;
    private boolean running = true;

    /*
    Intializing Configuration, authentication, BaseURI and BasePath
     */
    public RestAPIWrappers() {
    }

    public void initializeRestAPI(String serviceName) {

        if (serviceName.equalsIgnoreCase("browserStack")) {
            RestAssured.reset();
            QueryParam = null;
            propertyLoader();
            dataLoader();
            RestAssured.config = RestAssuredConfig.newConfig().sslConfig(SSLConfig.sslConfig().allowAllHostnames());
//            RestAssured.authentication = RestAssured.certificate(CERTIFICATES, CERTIFICATE_PASS);
            RestAssured.baseURI = propLoader.prop.getProperty("baseURI");
            RestAssured.basePath = propLoader.prop.getProperty("basePATH");
            request = RestAssured.given().log().all();
            jsonFormater = new JsonFormater();
        }
         else {
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "No matching service endpoint available in config");
        }
    }

    /**
     * Set baseURI before starting the test
     */
    public void setBaseURI(String URI) {
        RestAssured.baseURI = URI;

    }

    public void setCurlConfig()
    {
        request=request.config(new CurlLoggingRestAssuredConfigBuilder().logStacktrace().build()).redirects().follow(false);
    }

    /***
     * Get the request object
     * @return RequestSpecification RequestSpecification
     */
    public RequestSpecification getRequest() {
        return request;
    }

    /**
     * Intialized contentType
     */
    public void setContentType(ContentType Type) {

        request = request.contentType(Type);
    }

    /**
     * set the content type as string
     *
     * @param contentType
     */
    public void setContentType(String contentType) {

        request = request.contentType(contentType);
    }

    /**
     * To print response body
     */
    public void toPrintResponseBodyonLog() {
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), response.body().print());
    }

    /**
     * To validate ResponseCode Using ValidatableResponse
     */
    public void validateResponseCode(int code) {
        responseTable = response.then().statusCode(code);
    }

    /**
     * To validate multiple response in ValidatableResponse
     */

    public void validata_multivalue_responseTable(Map<String, String> dataFields) {
        for (Map.Entry<String, String> dataitem : dataFields.entrySet()) {
            if (StringUtils.isNumeric(dataitem.getValue())) {
                responseTable.body(dataitem.getKey(), equalTo(Integer.parseInt(dataitem.getValue())));
            } else {
                responseTable.body(dataitem.getKey(), equalTo(dataitem.getValue()));
            }

        }
    }

    /**
     * /**
     * Initalizing base Path
     */
    public void setBasePath(String basePath) {
        RestAssured.basePath = basePath;
    }

    //Initalizing Given
    public void initializeGiven() {
        request = RestAssured.given();

    }

    /**
     * Reset BaseURI, after completing test
     */
    public void resetBaseURI() {
        RestAssured.baseURI = null;
    }

    /**
     * Reset BasePath, after completing test
     */
    public void resetBasePath() {
        RestAssured.basePath = null;
    }

    /**
     * Reset AllStufs, after completing test
     */
    public void resetRestAPI() {
        RestAssured.reset();
    }

    /**
     * Assinging Credentials, Headers for Content-type and Accept
     */
    public void multiHeader(String AuthorizationValue, String contentType, String AcceptFormat) {

        request = request.header("Authorization", AuthorizationValue)
                .header("Content-Type", contentType)
                .header("Accept", AcceptFormat)
                .urlEncodingEnabled(false);
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), request.toString());

    }
    public void multiHeader( String contentType, String AcceptFormat) {

        request = request.header("Content-Type", contentType)
                .header("Accept", AcceptFormat)
                .urlEncodingEnabled(false);
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), request.toString());

    }
    /**
     * Will return status line code for eg., HTTP/1.1 200 OK"
     */

    public String responseGettingStatusLineCode() {
        return response.getStatusLine();
    }

    /**
     * Assigning Payload in request
     */
    public void setBody(String FileName) {
        file = new File(REST_PAYLOAD + FileName);
        request = request.body(file);
    }

    /**
     * Setting the body
     * @param body String object
     */
    public void setBodyAsString(String body) {

        request = request.body(body);
    }
    /**
     * Initializing file attachment in requestSpecification
     *
     * @param attachmentUrl Path of File Attachment support xlsx and csv
     */
    public void setMultiPart(String attachmentUrl)
    {
        request=request.multiPart(new File(attachmentUrl));
    }

    public void setpathQueryParm(String URIpath) {
        QueryParam = URIpath;
    }

    /**
     * Initalizing QueryParam
     */
    public void setpathQueryParm(String Query, String param) {
        QueryParam = "?" + Query + "=" + param;
        if (QueryParam.length() <= 2)
            QueryParam = null;
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Setting QueryParam is: " + QueryParam);
    }

    /**
     * This method builds the queryparam string using the data structure
     *
     * @param parameters name,value pairs
     */
    public void buildQueryParam(Map<String, String> parameters) {
        StringBuilder sbQueryParam = new StringBuilder();
        parameters.forEach((name, value) -> {
            try {
                sbQueryParam.append(name).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            }
        });

        QueryParam = sbQueryParam.insert(0, "?").toString();

    }

    /**
     * Invoking GetRequest with/Without QueryParm
     */
    public void invokeGetRequest(String path) {
        if (QueryParam == null) {
            response = request.get(path);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "GET Request Invokedas: " + RestAssured.baseURI + RestAssured.basePath + path);
        } else {
            response = request.get(path + QueryParam);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "GET Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path + QueryParam);
        }
    }

    /**
     * Method to call GET request recursively till the condition is satisfied
     *
     * @param path        - request path
     * @param status      - status
     * @param maxnoofexec - number of times the GET needs to be invoked
     */
    public void invokeGetRequestRecursive(String path, String status, String maxnoofexec) {
        callExecution = 0;
        running = true;
        callGET(path, Integer.parseInt(maxnoofexec));
    }

    /**
     * Recursive method which invokes GET based on maxnoofexec
     *
     * @param path        API call path
     * @param maxnoofexec max no of times the recursive call have to be executed
     */
    private void callGET(String path, int maxnoofexec) {

        callExecution++;

        if ((callExecution <= maxnoofexec) && running) {
            try {
                Thread.sleep(20000);
                if (QueryParam == null) {
                    response = request.get(path);
                    LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "GET Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path);

                } else {
                    response = request.get(path + QueryParam);
                    LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "GET Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path + QueryParam);
                }
                running = (returnRestResponse().contains("RUNNING") || returnRestResponse().contains("UNKNOWN"));
                maxnoofexec--;
                LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Process status " + String.valueOf(running) + " in " + String.valueOf(callExecution) + " times");
            } catch (InterruptedException e) {
                LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            } catch (Exception e) {

                LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            }
            callGET(path, maxnoofexec);
        }
    }

    /**
     * Invoking PutRequest with/Without QueryParm
     */
    public void invokePutRequest(String path) {
        if (QueryParam == null) {
            response = request.put(path);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "PUT Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path);
        } else
            response = request.put(path + QueryParam);
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "PUT Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path + QueryParam);
    }

    /**
     * Invoking PostRequest with/Without QueryParm
     */
    public void invokePostRequest(String path) {
        if (QueryParam == null) {
            response = request.post(path);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "POST Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path);
        } else {
            response = request.post(path + QueryParam);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "POST Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path + QueryParam);
        }
    }

    /**
     * Invoking DeleteRequest with/Without QueryParm
     */
    public void invokeDeleteRequest(String path) {
        if (QueryParam == null) {
            response = request.delete(path);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "DELETE Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path);
        } else {
            response = request.delete(path + QueryParam);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "DELETE Request Invoked: " + RestAssured.baseURI + RestAssured.basePath + path + QueryParam);
        }
    }

    /*
    Return Response status code
     */
    public int returnStatusCode() {
        int statuscode;
        statuscode = response.statusCode();
        return statuscode;
    }

    /*
    validate Json element
     */

    public String returnJsonValue(String jsonQuery) {
        JsonPath jsonPath = response.jsonPath();
        String jsonValue = jsonPath.get(jsonQuery);
        return jsonValue;
    }

    public int returnJsonIntValue(String element) {
        JsonPath jsonPath = response.jsonPath();
        int jsonValue = jsonPath.getInt(element);
        return jsonValue;

    }

    public List<String> returnJsonCollections(String jsonQuery) {
        List<String> jsonValue;
        JsonPath jsonPath = response.jsonPath();
        jsonValue = jsonPath.get(jsonQuery);
        return jsonValue;
    }

    /*
        Return response body
    */
    public String returnRestResponse() throws IOException {
        JsonResponse = jsonFormater.jsonformat(response.body().asString());
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Response body is: " + JsonResponse);
        return JsonResponse;

    }

    public boolean returnRestResponseForXML(String text) {
        boolean status = false;
        String XMLResponse = response.body().asString();
        if(XMLResponse.contains(text))
            status = true;
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Response body is: " + XMLResponse);
        return status;

    }

    public void responseFileDownload(String fileDirPath,String fileName){
        File downloadedFile = new File(propLoader.prop.get(fileDirPath) + fileName);
        try (FileOutputStream out = new FileOutputStream(downloadedFile)) {
            try (InputStream in = response.asInputStream()) {
                LoggerUtil.logLoader_info(this.getClass().getSimpleName(),"Available: " + in.available());
                IOUtils.copy(in, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*To get all attribute as list
     */
    public boolean responseValidationhasItems(List<String> responseNode, List<String> comparisonValue) {
        boolean status = false;
        try {
            if (comparisonValue.size() >= 1 && responseNode.size() > 1) {
                for (int i = 0; i < comparisonValue.size(); i++) {
                    response.then().body(responseNode.get(i), hasItem(comparisonValue.get(i)));
                }
                status = true;
            } else if (comparisonValue.size() >= 1 && responseNode.size() == 1) {
                for (int i = 0; i < comparisonValue.size(); i++) {
                    response.then().body(responseNode.get(0), hasItem(comparisonValue.get(i)));
                }
                status = true;
            } else {
                status = false;
                throw new Exception();
            }
        } catch (Exception e) {
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), e.getMessage());
        }
        return status;
    }


    /*
    Return response body
 */
    public Response response() {
        return response;
    }

    /*
        Return Specific response element using JsonPath
     */
    public String returnJsonPath(String test) throws IOException {
        String actualPath;
        JsonPath jsonpath = JsonPath.from(returnRestResponse());
        actualPath = jsonpath.getString(test);
        return actualPath;
    }

    /*
        Return collection of response element using JsonPath
     */
    public List<String> returnElementLists(String element) {
        List<String> elementLists;
        jp = new JsonPath(JsonResponse);
        elementLists = jp.get(element);
        return elementLists;
    }


    /*Propery Loader
     */
    public void propertyLoader() {
        propLoader = new PropertyLoader();
        propLoader.loadProperty();
    }


    public void dataLoader() {
        DataLoader.getDataLoaderInstance().createRepoData();

    }

    /* This method is to remove special char, return numeric, text and dot
     */
    public String removeSpecialChar(String text) {
        String specialChar;
        specialChar = text.replaceAll("[^a-zA-Z0-9.]", "");
        return specialChar;
    }

    /* This method is to remove Numeric, special character and return only string
     */
    public String removeNumericSpecialChar(String text) {
        String NumericSpecialchar;
        NumericSpecialchar = text.replaceAll("[^a-zA-Z]", "");
        return NumericSpecialchar;
    }

    /* This method is to remove Characters,Special characters and return only numeric.
     */
    public String removeCharSpecialChar(String text) {
        String CharSpecialChar;
        CharSpecialChar = text.replaceAll("[^0-9.]", "");
        return CharSpecialChar;
    }

    /*
    Splitting delimit value at left side
    */
    public String returnSplitValueLeft(String text, String demlimit) {
        String[] splitText = text.split(demlimit);
        return splitText[0];
    }

    /*
    Splitting delimit value at right side
     */
    public String returnSplitValueRight(String text, String demlimit) {
        String[] splitText = text.split(demlimit);
        return splitText[1];
    }

    public String returnSplitValueRightWithLimit(String text, String demlimit,int limit) {
        String[] splitText = text.split(demlimit,limit);
        return splitText[1];
    }

    /* Comma sepearated from String to List
     */
    public List<String> CommaSeparateStringToList(String text) {
        String[] items = text.split(",");
        List<String> container = Arrays.asList(items);
        return container;
    }

    public String returnContentType() {
        return response.contentType();

    }

    public String returnNodeValue(String nodeName){
        return response.path(nodeName);
    }

    public List<String> returnNodeValuesAsList(String nodeName){
        List<String> values;
        values = response.path(nodeName);
        return values;
    }

    /**
     * Returns single value from the response
     *
     * @param path
     * @return
     */
    public String getValueFromResponse(String path) {
        return response.path(path);
    }

    /**
     * Validate the response with the given value
     *
     * @param path   JsonPath to the node which has the value
     * @param result expected result
     */
    public void validateResponseEqualsValue(String path, String result) {
        response().then().body(path, equalTo(result));
    }

    /**
     * Validate the response with the given list of values
     *
     * @param path    JsonPath to the node which has the value
     * @param results expected results
     */
    public void validateResponseContainsItems(String path, String... results) {
        response().then().body(path, hasItems(results));
    }

    /**
     * Returns a list of values from response by sending the JsonPath
     *
     * @param path JsonPath
     * @return Arraylist
     */
    public ArrayList<String> getListFromResponse(String path) {
        return response().getBody().jsonPath().get(path);
    }

    /**
     * Returns a set of values from response by sending the JsonPath
     *
     * @param path JsonPath
     * @return Map
     */
    public Map<String, String> getSetFromResponse(String path) {
        return response.path(path);
    }

    /**
     * This method will return response as a string object
     * @return String
     */
    public String getResponseAsString() {
        return this.response().getBody().asString();
    }

    /**
     * @param jsonPath jsonPath
     * @return
     */
    public String getJsonValueUsingJsonPath(String jsonPath) {
        String value=null;
        value = com.jayway.jsonpath.JsonPath.parse(response.body().asString()).read(jsonPath).toString().replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","");
        return value;
    }

    /**
     * this wrapper returns the list of values for given jsonpath
     * @param jsonPath - json path to get values from response
     * @return
     */
    public List<String> getJsonResponseValuesInList(String jsonPath){
        List<String> value=null;
        value=com.jayway.jsonpath.JsonPath.parse(response.body().asString()).read(jsonPath);
        return value;
    }

    public boolean validateEmptyResponse() {
        boolean status = false;
        JSONArray myArray = new JSONArray();
        if ((response.body().asString()).equals(myArray.toString())) {
            status=true;

        }
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Empty Response"+response.body());
        return status;

    }

}
