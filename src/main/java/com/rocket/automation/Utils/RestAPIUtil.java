package com.rocket.automation.Utils;

import com.rocket.automation.Wrappers.RestAPIWrappers;
import com.rocket.automation.Wrappers.UIWrappers;
import org.testng.Assert;


import static com.rocket.automation.Utils.Constant.FEATURES;

//created by sid and thirveni
public class RestAPIUtil extends RestAPIWrappers {

    private UIWrappers UIwrapper;
    private JsonRead jsonRead;


    public void response_message_contains_value(String ElementValue) {
        // Write code here that turns the phrase above into concrete actions
        boolean status = false;
        try {
            //responseMessage = jsonFormater.jsonformat(returnRestResponse());
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Verifying element exist in API response");
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), returnRestResponse());
            if (!ElementValue.isEmpty()) {
                String response = returnRestResponse();
                status = returnRestResponse().contains(ElementValue);
                if (!status)
                    Assert.fail(ElementValue +"\r\n Such Element value not found in API Json Response \r\n"+returnRestResponse());
                else
                    LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Element is found in API Response");
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }

    }
    public void APIRequest(String Action, String url, String responseCode, String responseJsonPath, String responseMessage ,String runtimeResponseJsonPath, String body, String bodyJsonPath, String outPutFile, String outPutJson, String inputFile, String inputjsonPath,String Multipartfile ) throws Throwable {

        Boolean flag=false;
        try {
            switch (Action) {
                case "GET":
                    if (url.contains("dynamic")) {
                        String dynID = UIwrapper.getNUMfromString(JsonRead.getJsonValue(Constant.REST_DIR + inputFile, inputjsonPath));
                        url = url.replace("dynamic", dynID);

                        invokeGetRequest(url);

                    } else {
                        invokeGetRequest(url);
                    }
                    Assert.assertEquals(String.valueOf(returnStatusCode()), responseCode);
                    break;
                case "POST":
                    if (body != null && !body.isEmpty()) {
                        if (body.contains("json") || body.contains("txt") || body.contains("xml")) {
                            if (bodyJsonPath.isEmpty() || bodyJsonPath == null) {
                                setBody(body);
                            } else if (!bodyJsonPath.isEmpty() || bodyJsonPath != null) {
                                setBodyAsString(JsonRead.getJsonValue(Constant.REST_DIR + body, bodyJsonPath));
                            }
                            invokePostRequest(url);
                        } else {
                            setMultiPart(FEATURES + file);
                            invokePostRequest(url);
                        }

                    } else {
                        invokePostRequest(url);
                    }
                    Assert.assertEquals(String.valueOf(returnStatusCode()), responseCode);
                    break;

                case "PUT":
                    if (body != null && !body.isEmpty()) {
                        if (body.contains("json") || body.contains("txt") || body.contains("xml")) {
                            if (bodyJsonPath.isEmpty() && bodyJsonPath == null) {
                                setBody(body);
                            } else if (!bodyJsonPath.isEmpty() && bodyJsonPath != null) {
                                setBodyAsString(JsonRead.getJsonValue(Constant.REST_DIR + body, bodyJsonPath));
                            }
                            invokePutRequest(url);
                        } else {
                            setMultiPart(FEATURES + file);
                            invokePutRequest(url);
                        }
                    } else {
                        invokePutRequest(url);
                    }
                    Assert.assertEquals(String.valueOf(returnStatusCode()), responseCode);
                    break;
                case "DELETE":
                    invokeDeleteRequest(url);
                    Assert.assertEquals(String.valueOf(returnStatusCode()), responseCode);
                    break;

            }


            if (!outPutFile.isEmpty()) {
                if (!outPutJson.isEmpty()) {
                    String jsonValue = getJsonValueUsingJsonPath(responseJsonPath);
                    if (outPutFile.equals(inputFile)) {
                        JsonBuildUpdateUtil.updateJsonNode(Constant.REST_DIR + outPutFile, outPutJson, jsonValue);
                    } else {
                        FileUtil.createFileAndWriteData(Constant.REST_DIR + outPutFile, jsonValue);
                    }
                } else if (outPutJson.isEmpty()) {
                    FileUtil.createFileAndWriteData(Constant.REST_DIR + outPutFile, returnRestResponse());
                }

            }
            if (!runtimeResponseJsonPath.isEmpty() && runtimeResponseJsonPath != null) {
                String[] responses = responseMessage.split(",");
                for (String responsemsg : responses) {
                    String jsonValue = getJsonValueUsingJsonPath(runtimeResponseJsonPath);
                    flag = jsonValue.contains(responsemsg);
                    if (flag == true) {
                        LoggerUtil.logInfo( "Response message from UI and Actual Matched");
                    } else {
                        Assert.fail("Actual UI value= " + jsonValue + "didn't matched with the expected user value =" + responsemsg);
                    }
                }
            } else {
                String[] responses = responseMessage.split(",");
                for (String responsemsg : responses) {
                    response_message_contains_value(responsemsg);
                }
            }
        } catch (
                Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), "Expected values are mismatching with Response");
            Assert.fail("Expected values are mismatching with Response:  " + e.getMessage());
        }
    }


}
