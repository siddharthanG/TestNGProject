package com.rocket.automation.Utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    protected LoggerUtil loggerUtil;

	
	  public Properties prop;



	    public void loadProperty() {
	        File file = new File(Constant.UI_CONFIG);
	        FileInputStream fileInput = null;
	        try {
	            fileInput = new FileInputStream(file);
	        } catch (FileNotFoundException e) {
	            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
	        }
	        prop = new Properties();
	        //load properties file
	        try {
	            prop.load(fileInput);
	        } catch (IOException e) {
	            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
	        }
	    }
}
