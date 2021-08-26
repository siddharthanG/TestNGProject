package com.rocket.automation.Utils;


import org.testng.ITestResult;

public class    RetryFailedTestCases implements org.testng.IRetryAnalyzer {

    private int retry = 0;
    private int MaxRetry = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (retry < MaxRetry) {
            System.out.println("Retrying" + iTestResult.getName() + " again and count is " + (retry + 1));
            retry++;
            return true;

        }
        return false;
    }
}
