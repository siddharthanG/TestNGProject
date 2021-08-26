package com.rocket.automation.Utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.util.Properties;

import com.rocket.automation.Wrappers.UIWrappers;

import static com.rocket.automation.Utils.Constant.*;

public class DynamicPropertyLoader extends UIWrappers {


    OutputStream outputStream = null;
    Properties prop;

    public DynamicPropertyLoader() {
        prop = new Properties();
    }


    public void DynamicLoader(String... host) {

        String userDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        System.out.println(userDirectory);
        try {

            prop.setProperty("baseURI", "https://" + host[0]);
            prop.setProperty("basePATH", "");
            prop.setProperty("JDBC", "DemoURlV2");
            prop.setProperty("Env", "browserStack");


//                prop.setProperty("browserName",host[1]);
//                prop.setProperty("hostName",host[1]);


            //
            outputStream = new FileOutputStream(UI_CONFIG);
            prop.store(outputStream, "Property file created Dynamically");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void main(String[] args) {
        //This is for temporary set environment in local machine
        DynamicPropertyLoader dynamicPropertyLoader = new DynamicPropertyLoader();
        dynamicPropertyLoader.DynamicLoader("www.browserstack.com");

    }
}
