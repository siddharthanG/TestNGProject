package com.rocket.automation.TestClasses;


import com.rocket.automation.Utils.LoggerUtil;
import com.rocket.automation.Utils.RestAPIUtil;
import com.rocket.automation.Wrappers.RestAPIWrappers;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

// created by sid and thirveni

@Listeners({com.rocket.automation.Listeners.TestAllureReportListener.class})
public class SampleAPITest extends RestAPIUtil {


    public SampleAPITest() {
        propertyLoader();
    }

    // this method will be executed before every @test method
    @BeforeMethod(alwaysRun = true)
    public void firstAPITest() {
        initializeRestAPI(propLoader.prop.getProperty("Env"));
        LoggerUtil.logLoader_info(this.getClass().getSimpleName(),"Before Method : Rest Initialized");
    }

    @AfterMethod(alwaysRun = true)
    public void lastAPITest() {
       resetRestAPI();

    }


    @Test(priority = 0, groups= {"SmokeTest"}, description = "verifying the Get request API")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case Description: verifying the Get request API")
    @Story("Story Name: To check Get request")
    public void verifyValidAPIURL() throws  Throwable {
        try {
            setContentType("application/json");
            APIRequest("GET","user/check_email","200","","true","$.exists","","","check_email.json","","","","");
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(),"response"+ returnRestResponse());
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Status code of Post Request invoked :"+ returnStatusCode());
        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            Assert.fail(e.getMessage());

        }
    }



}
