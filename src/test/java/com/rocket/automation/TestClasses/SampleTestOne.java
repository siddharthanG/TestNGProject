package com.rocket.automation.TestClasses;


import com.rocket.automation.PageObjects.SignInPageObjects;
import com.rocket.automation.Utils.ExcelDataLoaderUtil;
import com.rocket.automation.Utils.LoggerUtil;
import com.rocket.automation.Wrappers.UIWrappers;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

// created by sid and thirveni

@Listeners({com.rocket.automation.Listeners.TestAllureReportListener.class})
public class SampleTestOne extends UIWrappers {

    SignInPageObjects signInObjects;
    WebDriver driver;
    public ExcelDataLoaderUtil ExcelUtil = new ExcelDataLoaderUtil();

    public SampleTestOne() {

        propertyLoader();
    }

    @BeforeMethod(alwaysRun = true)
    public void firstTest() {
        this.driver = launchBrowser();
        driver.get(propLoader.prop.getProperty("baseURI"));
        signInObjects = new SignInPageObjects(driver);
        LoggerUtil.logInfo("SampleTestOne BeforeTest: " + driver.toString());
        LoggerUtil.logInfo("SampleTestOne Thread ID: " + Thread.currentThread().getId());
    }

    @AfterMethod(alwaysRun = true)
    public void lastTest() {
        closeBrowser(driver);

    }


    @Test(priority = 0, groups = {"SmokeTest"}, description = "Enter the email", dataProvider = "DataFeed", dataProviderClass = ExcelDataLoaderUtil.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case Description: Verify Email field is enabled")
    @Story("Story Name: To check login page title in Chrome Browser")
    public void TestOne(String ExcelValues) {
        try {
            signInObjects.clickSignIn();
            signInObjects.enterBusinessEmail(ExcelValues);
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "SampleTestOne TestMethod1: Email Entered");

        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 1, groups = {"Sanity"}, description = "Enter the Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case Description: Verify Email field is enabled")
    @Story("Story Name: To check login page title in Chrome Browser")
    public void TestSecond() {
        try {
            signInObjects.clickSignIn();
            signInObjects.enterPassword("test@123");
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "SampleTestOne TestMethod2: Password Entered");

        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
            Assert.fail(e.getMessage());

        }
    }

}
