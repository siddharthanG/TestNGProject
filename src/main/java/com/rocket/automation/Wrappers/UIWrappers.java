package com.rocket.automation.Wrappers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rocket.automation.Utils.LoggerUtil;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.rocket.automation.Utils.PropertyLoader;
import com.rocket.automation.Utils.Constant;
import io.github.bonigarcia.wdm.WebDriverManager;


// created by sid and thirveni
public class UIWrappers {

    public String temp;
    public List<WebElement> tempList;
    public int tempValue;

    public UIWrappers() {
        propertyLoader();
    }

    protected PropertyLoader propLoader;

    public WebDriver driver;
    public static ThreadLocal<WebDriver> thread_driver = new ThreadLocal<WebDriver>();

    /**
     * Function Name : LaunchBrowser with user provided URL Description : To Launch
     * Browser either Chrome, Firefox, etc.,
     */
    @Step("Open browser and maximize the window")
    public WebDriver launchBrowser() {
        String BrowserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
                .getParameter("browserType");
        if (BrowserType.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (BrowserType.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (BrowserType.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        thread_driver.set(driver);
        windowMaximize(driver);
        return getDriver();

    }

    public String getAttributeValue(WebElement element, String value) {
        return element.getAttribute(value);
    }

    /**
     * Method Name : clickOn
     * Description : To Click on either button or checkbox for clicking action
     */
    public void clickOn(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method Name : sleepForSec
     *
     * @param msecs : Number of seconds to be wait
     */
    public void sleepForSec(long msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
            //LoggerUtil.Log.info("Exception raised on Thread.Sleep method");
        }
    }


    public static synchronized WebDriver getDriver() {
        return thread_driver.get();
    }

    @Step("Maximized the window")
    public void windowMaximize(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public void propertyLoader() {
        propLoader = new PropertyLoader();
        propLoader.loadProperty();
    }

    /**
     * Function Name : takeScreenShot Description : To take screenshot of the
     * current webpage
     */
    public void takeScreenShot(String className, WebDriver driver) {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(Constant.SCREENSHOTS + className + ".jpg"));
            // log code
        } catch (IOException e) {
            System.out.println("Screenshot cannot be taken, IOException is thrown" + e.getMessage());
        }
    }

    /**
     * Method Name : DropdownSelectbyText Description : To select dropdown by Text
     */
    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean isElementNotPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean isNotElementPresent(WebElement element) {
        try {
            Assert.assertFalse(element.isDisplayed());

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    public boolean isElementSelected(WebElement element) {
        try {
            element.isSelected();

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Method Name : isElementsListPresent Description : It verifies the list of
     * elements present
     */
    public boolean isElementsListPresent(List<WebElement> list) {

        return !list.isEmpty();
    }

    /**
     * Method Name : acceptAlert Description : To accept alert
     */
    public void acceptAlert(WebDriver driver) throws Exception {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            throw new Exception("No Alert Present");
        }
    }

    // Wait for Angular Load
    public static void waitForAngularLoad(WebDriver webDriver) {
        WebDriver jsWaitDriver = webDriver;

        WebDriverWait wait = new WebDriverWait(jsWaitDriver, 10);
        // Get Angular is Ready
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver)
                .executeAsyncScript("var callback = arguments[arguments.length - 1];"
                        + "if (document.readyState !== 'complete') {" + "  callback('document not ready');" + "} else {"
                        + "  try {" + "    var testabilities = window.getAllAngularTestabilities();"
                        + "    var count = testabilities.length;" + "    var decrement = function() {"
                        + "      count--;" + "      if (count === 0) {" + "        callback('complete');" + "      }"
                        + "    };" + "    testabilities.forEach(function(testability) {"
                        + "      testability.whenStable(decrement);" + "    });" + "  } catch (err) {"
                        + "    callback(err.message);" + "  }" + "}")
                .toString().equals("complete");

        try {
            wait.until(expectation);
        } catch (Exception e) {
            new Exception("Timeout waiting for Page Load Request to complete.");
        }
    }

    /**
     * Method Name : Close Browser Description : To close browser
     */
    @Step("Closing the driver {0}")
    public void closeBrowser(WebDriver driver) {
        try {
            driver.quit();
            // LoggerUtil.Log.info("Launched Browser closed successfully");
        } catch (Exception e) {
            // LoggerUtil.Log.info("Browser cant be closed, exception throws");
        }
    }

    public String getElementText(WebElement webElement) {
        String text;
        text = webElement.getText().trim();
        return text;
    }

    /**
     * Method Name : storeTemporaryText Description : Storing run time information
     * into temp variable
     */
    public void storeTemporaryText(String text) {
        temp = text;
    }

    /**
     * Method Name : getTemporaryText Description : Getting run time information
     * from temp variable
     */
    public String getTemporaryText() {
        return temp;
    }



    /**
     * Method Name : traverseListContainsElementText
     * Description : Traverse list and returns if element contains the expected text
     */

    public Boolean traverseListContainsElementText(List<WebElement> list, String expected) {
        int flag = 0;
        for (WebElement ele : list) {
            Boolean bool = ele.getText().trim().toLowerCase().equalsIgnoreCase(expected.toLowerCase());
            if (bool) {
                flag = 1;
                break;
            }
        }

        return flag == 1;

    }

    /***
     ** @ purpose : To Switch to a frame using the Web element
     */
    public void switchToFrame(WebDriver driver, WebElement element) {
        driver.switchTo().frame(element);
    }

    public void clickAndHold(WebDriver driver, WebElement element) {
        Actions builder = new Actions(driver);
        builder.clickAndHold(element).build().perform();
    }

    /**
     * Enable to scroll true or false, searching element by scrolling
     *
     * @param driver
     * @param element
     * @param status
     */
    public void scrolltoElement(WebDriver driver, WebElement element, boolean status) {
        if (status) {
            JavascriptExecutor je = (JavascriptExecutor) driver;
            je.executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }

    public boolean waitandFindElement(WebDriver driver, WebElement element, int noofAttempts, boolean scrollRequired) {
        boolean elementVisible = false;
        int i = 1;
        try {
            while (i <= noofAttempts) {
                try {
                    elementVisible = element.isDisplayed();
                    if (elementVisible) {
                        if (scrollRequired) {
                            scrolltoElement(driver, element, scrollRequired);
                        }
                        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Element " + element + " is found");
                        break;
                    } else {
                        i++;
                        sleepForSec(1000);
                        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "Trying " + i + "  attempts");
                    }
                    if (i == noofAttempts) {
                        LoggerUtil.logLoader_info(this.getClass().getSimpleName(), "No " + element + " found");
                    }
                } catch (StaleElementReferenceException SE) {
                    i++;
                }

            }
        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), "No " + element + " found");
        }
        return elementVisible;
    }


    /**
     * Method Name : storeTemporaryValue
     * Description : Storing run time information into temp value
     */
    public void storeTemporaryValue(int value) {
        tempValue = value;
    }

    /**
     * Method Name : getTemporaryValue
     * Description : Getting run time information from temp variable
     */
    public int getTemporaryValue() {
        return tempValue;
    }

    /**
     * Method Name : storeTemporaryList
     * Description : Storing run time List of elements  into temp list
     */
    public void storeTemporaryList(List<WebElement> list) {
        for (WebElement ele : list) {
            tempList.add(ele);
        }
    }

    /**
     * Method Name : getTemporaryList
     * Description : Getting run time List of elements from temp list
     */
    public List<WebElement> getTemporaryList() {
        return tempList;
    }

    /**
     * Method Name : traverseListRemovesElements
     * Description : Traverse list and removes another list of elements
     */


    public List<String> convertWebElementListIntoStringList(List<WebElement> list) {
        List<String> strings = new ArrayList<String>();
        for (WebElement e : list) {
            strings.add(e.getText().trim());
        }
        return strings;
    }

    public void dragAndDropwithActions(WebDriver driver, WebElement from, WebElement to) {
        Actions builder = new Actions(driver);
        builder.dragAndDropBy(from, to.getLocation().getX(), to.getLocation().getY()).build().perform();
    }

    /**
     * Method Name : EnterKeyPressEvent
     * Description : Enter key will be pressed using this method
     */

    public void pressEnterKey(WebDriver driver) {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception e) {
            LoggerUtil.logLoader_error(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Method Name : scrollToWebElement
     * Description : This method will make a browser to scroll till the mentioned element is found
     */
    public void scrollToWebElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Method Name : scrollToWebElementParamFalse
     * Description : This method will make a browser to scroll till the mentioned element is found
     */
    public void scrollToWebElementParamFalse(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public Boolean horizantalScrollBarCheck(WebDriver driver) {
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        Boolean horzscrollStatus = (Boolean) javascript.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
        return horzscrollStatus;
    }


    /**
     * Method Name : traverseListContainsElementReturnsElement
     * Description : Traverse list and if element is present returns the element
     */
    public WebElement traverseListContainsElementTextReturnsElement(List<WebElement> list, String expected) {
        WebElement element = null;
        int flag = 0;
        for (WebElement ele : list) {
            Boolean bool = ele.getText().trim().equalsIgnoreCase(expected);
            if (bool) {
                flag = 1;
                element = ele;
                break;
            }
        }

        if (flag == 1) {
            return element;
        } else return null;

    }

    public String traverseListContainsElementTextReturnsElementText(List<WebElement> list, String expected) {
        String element = null;
        int flag = 0;
        for (WebElement ele : list) {
            Boolean bool = ele.getText().trim().contains(expected);
            if (bool) {
                flag = 1;
                element = ele.getText().trim();
                break;
            }
        }

        if (flag == 1) {
            return element;
        } else return null;

    }

    //Method to get Element text with attribute
    public boolean traverseListContainsElementTextViaAttribute(List<WebElement> list, String expected, String attributeValue) {
        int flag = 0;
        for (WebElement ele : list) {
            Boolean bool = getAttributeValue(ele, attributeValue).trim().contains(expected);
            if (bool) {
                flag = 1;
                break;
            }
        }

        return flag == 1;

    }



    /**
     * @param element    webElement which needs to be scrolled
     * @param action     whether to zoom In or zoom Out
     * @param noOfScrols no of In Scroll or Out Scroll
     */
    public void scrollUsingSendKeys(WebElement element, String action, String noOfScrols) {
        switch (action) {
            case "zoomIn":
                for (int i = 0; i < Integer.parseInt(noOfScrols); i++) {
                    element.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
                    sleepForSec(1000);
                }
                break;
            case "zoomOut":
                for (int i = 0; i < Integer.parseInt(noOfScrols); i++) {
                    element.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
                    sleepForSec(1000);
                    break;
                }
        }
    }

    /**
     * @param driver
     * @param element webelement which position needs to be changed
     * @param width   x coordinates of the object
     * @param height  y coordinates of the object
     */
    public void moveToCoordinates(WebDriver driver, WebElement element, String width, String height) {
        Actions act = new Actions(driver);
        act.clickAndHold(element).moveByOffset(Integer.parseInt(width), Integer.parseInt(height)).release(element).build().perform();
    }

    /**
     * @param driver
     * @param element webelement which position needs to be changed
     * @param width   x coordinates of the object
     * @param height  y coordinates of the object
     */
    public void moveToCoordinatesAndClick(WebDriver driver, WebElement element, String width, String height) {
        Actions act = new Actions(driver);
        act.moveToElement(element).moveByOffset(Integer.parseInt(width), Integer.parseInt(height)).click(element).perform();
    }


    public void scrollDownUsingJS(WebDriver driver, List<WebElement> value, int scrollValue){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'start' });", value.get(value.size()-scrollValue));    }

    //Returns Webelement list value to String List
    public List<String> getStringListFromElementsList(List<WebElement> values){
        List<String> list = new ArrayList<>();
        for(WebElement ele: values){
            list.add(ele.getText());
        }
        return list;
    }

    //Returns Webelement list value to String List
    public List<String> getLowerCaseTextListFromElementsList(List<WebElement> values){
        List<String> list = new ArrayList<>();
        for(WebElement ele: values){
            list.add(ele.getText().toLowerCase());
        }
        return list;
    }

    public String getNUMfromString(String text) {
        String CharSpecialChar;
        CharSpecialChar = text.replaceAll("[^0-9.]", "");
        return CharSpecialChar;
    }
}

