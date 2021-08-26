package com.rocket.automation.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Nirmal.Balasundaram on 9/5/2017.
 * This class is used to manipulate json and return the updated json
 */

public class JsonBuildUpdateUtil {

    private JsonBuildUpdateUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

    /**
     * This method is used to update a json node using jsonPath expression
     *
     * @param jsonData jsonData which needs to be updated
     * @param jsonPath jsonPath string
     * @param node     node which needs to be updated/added
     * @param value    value which needs to be updated
     * @return ObjectNode
     */
    public static ObjectNode insertJsonNode(String jsonData, String jsonPath, String node, Object value) {

        ObjectNode objectNode = JsonPath.using(configuration).parse(jsonData).put(jsonPath, node, value).json();
        return objectNode;
    }

    /**
     * This method is used to update a JSON Object value using jsonpath
     *
     * @param filePath path of the file
     * @param jsonPath jsonpath in the file
     * @param value    value which needs to be updated
     * @throws IOException
     */
    public static void updateJsonNode(String filePath, String jsonPath, String value) throws IOException {
        FileWriter fW = new FileWriter(filePath, true);
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            JSONObject json = (JSONObject) js.parse(fileReader);
            Configuration configuration = Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build();
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).set(jsonPath, value).json();
            fW = new FileWriter(filePath);
            fW.write(updatedJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(value + " not updated in json path");
        } finally {
            fW.flush();
            fW.close();
        }
    }


    public static void updateJsonNodeWithBoolean(String filePath, String jsonPath, boolean value) throws IOException {
        FileWriter fW = new FileWriter(filePath,true);
        try {
            JSONParser js = new JSONParser();
            JsonFormater jf = new JsonFormater();
            FileReader fileReader = new FileReader(filePath);
            JSONObject json = (JSONObject) js.parse(fileReader);
            Configuration configuration = Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build();
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).set(jsonPath, value).json();
            fW = new FileWriter(filePath);
            fW.write(jf.jsonformat(updatedJson.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(value + " not updated in json path");
        } finally {
            fW.flush();
            fW.close();
        }
    }

    public static void updateJsonNodeWithInteger(String filePath, String jsonPath, int value) throws IOException {
        FileWriter fW = new FileWriter(filePath, true);
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            JSONObject json = (JSONObject) js.parse(fileReader);
            Configuration configuration = Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build();
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).set(jsonPath, value).json();
            fW = new FileWriter(filePath);
            fW.write(updatedJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(value + " not updated in json path");
        } finally {
            fW.flush();
            fW.close();
        }
    }

    /**
     * This method is used to update integer value in Json Array
     *
     * @param value Integer value to be updated
     * @param jsonPath jsonPath string
     * @return ObjectNode
     */

    public static void updateJsonArrayWithInteger(String filePath, String jsonPath, int value) throws IOException {
        FileWriter fW = new FileWriter(filePath, true);
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            JSONArray json = (JSONArray) js.parse(fileReader);
            Configuration configuration = Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build();
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).set(jsonPath, value).json();
            fW = new FileWriter(filePath);
            fW.write(updatedJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(value + " not updated in json path");
        } finally {
            fW.flush();
            fW.close();
        }
    }

    /**
     * This method is used to delete a json node using jsonPath expression
     *
     * @param jsonData jsonData which needs to be updated
     * @param jsonPath jsonPath string
     * @return ObjectNode
     */
    public static ObjectNode removeJsonNode(String jsonData, String jsonPath) {

        ObjectNode objectNode = JsonPath.using(configuration).parse(jsonData).delete(jsonPath).json();
        return objectNode;
    }

    /**
     * @param filePath
     * @param jsonKey
     */
    public static void removeJsonNodeUsingKey(String filePath, String jsonKey) throws IOException {
        try {
            FileReader fileReader = new FileReader(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(fileReader);
            for (JsonNode personNode : jsonNode) {
                if (personNode instanceof ObjectNode) {
                    ObjectNode objectNode = (ObjectNode) personNode;
                    if (objectNode.has(jsonKey)) {
                        objectNode.remove(jsonKey);
                    }
                }
            }
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(String.valueOf(jsonNode));
            fileWriter.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param filePath   filepath where the json array has to be added
     * @param index      position of the json array
     * @param arrayValue json array value to be updated
     * @throws IOException
     */
    public static void addJsonArrayValueToFile(String filePath, int index, String arrayValue) throws IOException {
        FileWriter file = null;
        JSONArray js = new JSONArray();
        js.add(index, arrayValue);
        try {

            file = new FileWriter(filePath);
            file.write(js.toJSONString());
        } catch (IOException e) {
            Assert.fail(filePath + " not found");
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

    }

    //Method to add String Value to file using FileOutputStream
    public static void addStringValueToFile(String filePath, String arrayValue) throws IOException {
        FileOutputStream file = null;
        try {
            byte[] bs = arrayValue.getBytes();
            file = new FileOutputStream(filePath);
            file.write(bs);
        } catch (IOException e) {
            Assert.fail(filePath+" not found");
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

    }

    //Method to append the response value with a special charater to file
    public static void appendJsonObjectValueToFile(String filePath, int index, String arrayValue, String splCharater) throws IOException {
        BufferedWriter file = null;
        JSONArray js = new JSONArray();
        js.add(index, arrayValue);
        try {
            file = new BufferedWriter(new FileWriter(filePath, true));
            file.append(splCharater + js);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

    }


    public static void addJsonObjectValueToArray(String filePath, String jsonPath, String key, String value) throws IOException, ParseException {
        FileWriter fW = null;
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            JSONObject json = (JSONObject) js.parse(fileReader);
            fW = new FileWriter(filePath);
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).put(jsonPath, key, value).json();
            fW.write(updatedJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fW.flush();
            fW.close();
        }

    }

    public static void addJsonObjectValueToArrayUsingMap(String filePath, String jsonPath, Map<String, Map<String, String>> values) throws IOException, ParseException {
        FileWriter fW = null;
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            JSONObject json = (JSONObject) js.parse(fileReader);
            fW = new FileWriter(filePath);
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).add(jsonPath, values).json();
            fW.write(updatedJson.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fW.flush();
            fW.close();
        }

    }


    /**
     * This method is used to update a JSON Array value using jsonpath
     *
     * @param filePath path of the file
     * @param jsonPath jsonpath in the file
     * @param value    value which needs to be updated
     * @throws IOException
     * @throws ParseException
     */
    public static void updateArrayValue(String filePath, String jsonPath, String value) throws IOException, ParseException {

        JSONParser js = new JSONParser();
        FileReader fileReader = new FileReader(filePath);
        JSONArray json = (JSONArray) js.parse(fileReader);
        Configuration configuration = Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
        JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).set(jsonPath, value).json();
        FileWriter fW = new FileWriter(filePath);
        fW.write(updatedJson.toString());
        fW.flush();
        fW.close();
    }

    /**
     * Method to update a new json object to an existing json object array using jsonpath
     *
     * @param parentJsonFilePath
     * @param newObjectFilePath
     * @param jsonPath
     * @throws IOException
     */
    public static void appendNewObjectFromFileToJSON(String parentJsonFilePath, String newObjectFilePath, String jsonPath) throws IOException {
        FileWriter fW = null;
        try {
            JSONParser js = new JSONParser();
            FileReader fileReader = new FileReader(parentJsonFilePath);
            JSONObject json = (JSONObject) js.parse(fileReader);

            FileReader fs = new FileReader(newObjectFilePath);
            JSONObject json1 = (JSONObject) js.parse(fs);
            JsonNode updatedJson = JsonPath.using(configuration).parse(json.toJSONString()).add(jsonPath, json1).json();
            fW = new FileWriter(parentJsonFilePath);
            fW.write(updatedJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fW.flush();
            fW.close();
        }
    }

    /**
     * This method is used to delete a json node using jsonPath expression
     *
     * @param filePath jsonData which needs to be deleted
     * @param jsonPath jsonPath string
     * @return ObjectNode
     */
    public static boolean deleteJSONNodeUsingPath(String filePath, String jsonPath) throws IOException, ParseException {
        boolean status = false;
        FileWriter fW = null;
        try {
            FileReader fileReader = new FileReader(filePath);
            Configuration configuration = Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build();
            JSONParser js = new JSONParser();
            JSONObject json = (JSONObject) js.parse(fileReader);
            if (JsonRead.getJsonValue(filePath, jsonPath) != null) {
                JsonNode objectNode = JsonPath.using(configuration).parse(json.toJSONString()).delete(jsonPath).json();
                fW = new FileWriter(filePath);
                fW.write(objectNode.toString());
                status = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(filePath + " not valid");
        } finally {
            fW.flush();
            fW.close();
        }
        return status;
    }


    /**
     * To add new json object and value
     */
    public static void addJsonObjectToJSONFile(String filePath, Object key, Object value) throws IOException {
        FileWriter fileWriter = null;
        FileReader fileReader=null;
        try {
            JSONObject jsonObject = new JSONObject();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
            jsonObject.put(key, value);
            fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            fileWriter.close();
        }
    }

    /**
     *To store list and map data in json file
     */
    public static void addListAndMapValuesToJSONFile(String filePath, String arrListName, List<String> arrList, String mapName, Map<String, Integer> map) throws IOException {
        FileWriter fileWriter = null;
        try {

            JSONObject jsonObject = new JSONObject();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(arrList);
            jsonObject.put(arrListName, jsonArray);
            jsonObject.put(mapName, map);
            fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }


    /**
     *To store list and map data in json file
     */
    public static void addListValuesToJSONFile(String filePath, String arrListName, List<String> arrList) throws IOException {
        FileWriter fileWriter = null;
        try {

            JSONObject jsonObject = new JSONObject();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(arrList);
            jsonObject.put(arrListName, jsonArray);
            fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }

}