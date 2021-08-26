package com.rocket.automation.PageObjects;

import static org.testng.Assert.assertEquals;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// created by sid and thirveni

public class SamplePageObjects {
    WebDriver driver;

    @FindBy(xpath = "//h1")
    WebElement Header;

    @FindBy(xpath = "//*[@id='user_full_name']")
    WebElement userName;

    @FindBy(xpath = "//*[@id='user_password']")
    WebElement password;

    @FindBy(xpath = "//a[contains(.,'Sign in')]")
    WebElement signIn;

    public SamplePageObjects(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Verify the Header text ")
    public void veryHeader() {
        String getHeaderText = Header.getText().trim();
        assertEquals("App & Browser Testing Made Easy", getHeaderText);
    }


    public void enterFullName(String arg1) {
        userName.sendKeys(arg1);
    }


    public void enterPassword(String arg1) {
        password.sendKeys(arg1);
    }

    @Step("user Click on Sign in Button ")
    public void clickSignIn() {
        signIn.click();
    }

    @Step("Get Header text from UI")
    public String getHeader() {
        return Header.getText().trim();

    }
}
