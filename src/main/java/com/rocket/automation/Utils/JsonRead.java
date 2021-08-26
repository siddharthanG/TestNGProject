package com.rocket.automation.Utils;

import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("DefaultFileTemplate")
public class JsonRead {

    private HashMap<Object, String> pairs = new HashMap<Object, String>();
    private JSONParser parser;
    String data = null;
    Object obj;

//    //To read Json value by supplying page and field value
//    public String readJSon(String pagename, String fieldname) {
//
//        parser = new JSONParser();
//        try {
//            obj = parser.parse(new FileReader(IDC_JSONCONFIG));
//            JSONObject jsonobj = new JSONObject(obj.toString());
//            JSONArray AppPageName = jsonobj.getJSONArray(pagename);
//            for (int i = 0; i < AppPageName.length(); i++) {
//                JSONObject j = AppPageName.optJSONObject(i);
//                Iterator it = j.keys();
//                while (it.hasNext()) {
//                    String n = it.next().toString();
//                    pairs.put(n, j.getString(n));
//                }
//                data = pairs.get(fieldname);
//                LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
//        }
//        return data;
//    }

    //To read Json value by supplying Rest Payload location, page and field value
    public String readJSon(String FilePath, String pagename, String fieldname) {

        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(FilePath));
            JSONObject jsonobj = new JSONObject(obj.toString());
            JSONArray AppPageName = jsonobj.getJSONArray(pagename);
            for (int i = 0; i < AppPageName.length(); i++) {
                JSONObject j = AppPageName.optJSONObject(i);
                Iterator it = j.keys();
                while (it.hasNext()) {
                    String n = it.next().toString();
                    pairs.put(n, j.getString(n));
                }
                data = pairs.get(fieldname);
                LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }

    //To read Json value by supplying file location field value
    public String readJSonFromQuery(String FilePath, String fieldname) {

        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(FilePath));
            JSONObject jsonobj = new JSONObject(obj.toString());

            data = (String) jsonobj.get(fieldname);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);
        } catch (ParseException e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        } catch (FileNotFoundException e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }

    //To read Json value by supplying file location, pagename and field value
    public String readJSonFromQuery(String FilePath, String pagename, String fieldname) {

        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(FilePath));
            JSONObject jsonobj = new JSONObject(obj.toString());

            JSONArray AppPageName = jsonobj.getJSONArray(pagename);
            for (int i = 0; i < AppPageName.length(); i++) {
                JSONObject j = AppPageName.optJSONObject(i);
                data = (String) j.get(fieldname);
                LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }

    //To read Json value by supplying file location, pagename and field value
    public String readJSonFromChildWithType(String FilePath, String pagename, String fieldname, String Value) {

        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(FilePath));
            JSONObject jsonobj = new JSONObject(obj.toString());

            JSONArray AppPageName = jsonobj.getJSONArray(pagename);

            int len = AppPageName.length();

            for (int i = 0; i < len; i++) {
                JSONObject j = AppPageName.getJSONObject(i);
                data = (String) j.get("type");
                if(data.equalsIgnoreCase(Value)){
                    data = (String) j.get(fieldname);
                    break;
                }
            }
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }

    public String readJSonFromLineage(String FilePath, String pagename, String fieldname) {

        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(FilePath));
            JSONObject jsonobj = new JSONObject(obj.toString());

            JSONObject jsonValues = jsonobj.getJSONObject("lineage");
            JSONArray fields = jsonValues.getJSONArray(pagename);

            for (int i = 0; i < fields.length(); i++) {
                data = fields.getString(i);
                }
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), data);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }




    public String readJsonfromPayload(String filePath, String fieldName) {
        parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader(filePath));
            String item = obj.toString();
            JSONArray itemArray = new JSONArray(item);
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject j = itemArray.optJSONObject(i);
                Iterator it = j.keys();
                while (it.hasNext()) {
                    String n = it.next().toString();
                    pairs.put(n, j.getString(n));
                }
                data = pairs.get(fieldName);
            }

        } catch (Exception e) {
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), e.getMessage());
        }
        return data;
    }

    /**
     * Method to read the json and return the jsonpath
     * @param filePath send the filepath of json location
     * @return JsonPath
     */
    public static JsonPath readJSONObject(String filePath)
    {
        JsonPath jsonPath = null;
        try {
            jsonPath =  JsonPath.from(FileUtil.createFile(filePath));
        }catch (Exception e){
            LoggerUtil.logLoader_info("JsonRead", e.getMessage());
        }
        return jsonPath;
    }


    public static boolean isKeyPresentinJSONObject(String filePath, String key) {
        boolean result = false;
        try {
            org.json.simple.JSONObject keyCheck;
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader(filePath));
            keyCheck = (org.json.simple.JSONObject) obj;
            if(keyCheck.containsKey(key)){
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Key Presence check failed");
        }
        return result;
    }


    public String readFile(String filePath) throws IOException {
        String result=null;
        BufferedReader file=null;
        try {
            file = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line = file.readLine();
            while (line != null) {
                sb.append(line);
                line = file.readLine();
            }
            result = sb.toString();
        }
        catch(Exception e) {
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), e.getMessage());
        }
        finally {
            if(file!=null)
            file.close();
        }
        return result;
    }

    /**
     * This method is to return json data as object either it can be json object or json array
     * @param filePath - file path of IDA json
     * @param jsonPath - json path to get the data
     * @return
     * @throws ParseException
     */
    public static Object readJsonObject(String filePath,String jsonPath) throws ParseException {
        JSONParser js=new JSONParser();
        Object object = null;
        HashMap<String,String> hm= (HashMap<String,String>)object;
        List<String> ls= (ArrayList)object;
        try{
            Object obj=js.parse(new FileReader(filePath));
            if(com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath).toString().contains(",")||com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath).toString().contains(":")){
                return com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);
            } else
                return com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;

    }

    /**
     * This method is to return array list value from the object returned in readJsonObject method
     * @param filePath json file path
     * @param jsonPath json path to get the data
     * @return
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<String> returnJsonObjectAsList(String filePath,String jsonPath) throws FileNotFoundException, ParseException {
        Object obj = JsonRead.readJsonObject(filePath, jsonPath);
        ArrayList ls = null;
        if (obj instanceof ArrayList) {
            ls = (ArrayList) obj;
        }
        return ls;
    }
    /**
     * This method is to sort list of json objects based on the value provided
     * @param filePath json file path
     * @param jsonPath json path to get the data
     * @return
     */
    public static List<String> sortJsonObject(String filePath,String jsonPath,String value) throws SecurityException {
        Object object = null;
        JSONParser js = new JSONParser();
        List<String> objects=new ArrayList<>();
        try {

            HashMap<String, String> hm = (HashMap<String, String>) object;

            Object obj = js.parse(new FileReader(filePath));
            objects = com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);

            Collections.sort(objects, new Comparator<Object>() {
                @Override public int compare(Object p1, Object p2) {
                    String a =null;
                    String b =null;
                    try {
                        a =com.jayway.jsonpath.JsonPath.parse(p1).read(value).toString();
                        b =com.jayway.jsonpath.JsonPath.parse(p2).read(value).toString();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return (a.compareTo(b));
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Assert.fail(filePath + " is invalid");
        }
        return objects;
    }


    public static List<Long> returnJsonObjectAsLongList(String filePath, String jsonPath) throws FileNotFoundException, ParseException {
        Object obj = JsonRead.readJsonObject(filePath, jsonPath);
        ArrayList ls = null;
        if (obj instanceof ArrayList) {
            ls = (ArrayList) obj;
        }

        return ls;
    }

    /**
     * This method is to return key value in map from the object returned in readJsonObject method
     * @param filePath
     * @param jsonPath
     * @return
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static HashMap<String,String> returnJsonObjectAsHashMap(String filePath,String jsonPath) throws FileNotFoundException, ParseException {
        Object obj = JsonRead.readJsonObject(filePath, jsonPath);
        HashMap<String,String> hm=new HashMap<>();
        String[] val = obj.toString().split(":");
        for (int i = 0; i < val.length; i += 2) {
            hm.put(val[i], val[i + 1]);
        }
        return hm;
    }

    /**
     * This method returns a String value from Json File using the Json Path
     * @param filePath - file path of IDA json
     * @param jsonPath - json path to get the data
     * @return
     * @throws FileNotFoundException
     */
    public static String getJsonValue(String filePath, String jsonPath) throws FileNotFoundException {
        String value = null;
        JSONParser js = new JSONParser();
        Object object = null;
        HashMap<String, String> hm = (HashMap<String, String>) object;
        List<String> ls = (ArrayList) object;
        try {
            Object obj = js.parse(new FileReader(filePath));
            value = com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(filePath + " is invalid");

        }
        return value;

    }


    /**
     * This method returns a String value from Json File using the Json Path
     * @param filePath - file path of IDA json
     * @param jsonPath - json path to get the data
     * @return
     */
    public static List<String> getJsonValueAsList(String filePath, String jsonPath) {
        List<String> ls=new ArrayList<>();
        List<String> objects=new ArrayList<>();
        JSONParser js = new JSONParser();
        Object object = null;
        HashMap<String, String> hm = (HashMap<String, String>) object;
        try {
            Object obj = js.parse(new FileReader(filePath));
            objects = com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);
            for(String value:objects){
                ls.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(filePath + " is invalid");
        }
        Collections.sort(ls);
        return ls;

    }

    /**
     * Method to return json value in Map from json file
     * @param filePath
     * @param jsonPath
     * @return
     */
    public Map<String, String> returnMapFromJsonObject(String filePath, String jsonPath) {
        Map<String, String> newMap = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            Object object = jsonParser.parse(new FileReader(filePath));
            Map<String, Object> objectValues = com.jayway.jsonpath.JsonPath.parse(object).read(jsonPath);
            for (Map.Entry<String, Object> hm1 : objectValues.entrySet()) {
                if (hm1.getValue() instanceof String) {
                    newMap.put(hm1.getKey(), (String) hm1.getValue());
                } else {
                    newMap.put(hm1.getKey(), String.valueOf(hm1.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMap;
    }



    public Map<String, List<String>> returnMapWithListFromJsonObject(String filePath, String jsonPath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Map<String, List<String>> newMap = new HashMap<>();
        Object object = jsonParser.parse(new FileReader(filePath));
        Map<String, Object> objectValues = com.jayway.jsonpath.JsonPath.parse(object).read(jsonPath);
        for (Map.Entry<String, Object> hm1 : objectValues.entrySet()) {
            if (hm1.getValue() instanceof org.json.simple.JSONArray) {
                List<Object> ls1 = Arrays.asList(((org.json.simple.JSONArray)hm1.getValue()).toArray());
                List<String> strList = ls1.stream()
                        .map( Object::toString )
                        .collect( Collectors.toList() );
                Collections.sort(strList);
                newMap.put(hm1.getKey(), strList);
            }
        }
        return newMap;
    }

    /**
     * Method to return list of integer value from json file using jsonpath
     * @param filePath
     * @param jsonPath
     * @return
     */
    public static List<Integer> returnIntegerList(String filePath, String jsonPath) {
        List<Integer> integerList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        JSONParser js = new JSONParser();
        try {
            Object obj = js.parse(new FileReader(filePath));
            integerList = com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);
            intList.clear();
            intList.addAll(integerList);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(filePath + " is invalid");

        }
        return intList;

    }

    /**
     * method to return json values as Integer from json file
     * @param filePath
     * @param jsonPath
     * @return
     */
    public static long getJsonValueAsInteger(String filePath, String jsonPath) {
        JSONParser js = new JSONParser();
        Object jsonArray1 = new JSONArray();
        int val = 0;
        try {
            Object obj = js.parse(new FileReader(filePath));
            if (com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath) instanceof net.minidev.json.JSONArray) {
                jsonArray1 = ((net.minidev.json.JSONArray) com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath)).get(0);
                val = ((Long) jsonArray1).intValue();
            }else{
                jsonArray1 = com.jayway.jsonpath.JsonPath.parse(obj).read(jsonPath);
                val = ((Long) jsonArray1).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(filePath + " is invalid");

        }
        return val;

    }
}
