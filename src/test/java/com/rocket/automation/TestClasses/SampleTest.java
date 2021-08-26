package com.rocket.automation.TestClasses;


import org.testng.Assert;
import org.testng.annotations.*;

import com.rocket.automation.PageObjects.SamplePageObjects;
import com.rocket.automation.Utils.LoggerUtil;
import com.rocket.automation.Wrappers.UIWrappers;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

// created by sid and thirveni

@Listeners({com.rocket.automation.Listeners.TestAllureReportListener.class})
public class SampleTest extends UIWrappers {


    SamplePageObjects objects;

    public SampleTest() {
        propertyLoader();
    }

    // this method will be executed before every @test method
    @BeforeMethod(alwaysRun = true)
    public void firstTest() {
        this.driver = launchBrowser();
        driver.get(propLoader.prop.getProperty("baseURI"));
        objects = new SamplePageObjects(driver);
        LoggerUtil.logInfo("SampleTestOne BeforeTest: " + driver.toString());
        LoggerUtil.logInfo("SampleTestOne Thread ID: " + Thread.currentThread().getId());
    }

    @AfterMethod(alwaysRun = true)
    public void lastTest() {
        closeBrowser(driver);

    }


    @Test(priority = 0, groups= {"SmokeTest"}, description = "verifying the header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case Description: Verify login page title test on Login Page")
    @Story("Story Name: To check login page title in Firefox browser")
    public void verifyValidURL() {
        try {
            objects.veryHeader();
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), objects.getHeader() + "Text is available as header");
        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            Assert.fail(e.getMessage());

        }
    }

    @Test(priority = 1, groups= {"Sanity"} , description = "Verify Successful login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case Description: Verify Successful login")
    @Story("Story Name: To check login page title in Firefox browser")
    public void SucessfullLogin() {
        try {
            objects.clickSignIn();
            objects.enterFullName("test_123@gmail.com");
            objects.enterPassword("test@123");
//		objects.clickSignIn();

            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Login Success");
        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            Assert.fail(e.getMessage());

        }
    }

}
