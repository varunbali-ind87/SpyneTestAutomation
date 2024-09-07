package org.spyne.reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;

public class ExtentTestManager
{
    private static final HashMap<Integer, ExtentTest> extentTestMap = new HashMap<>();
    private static final ExtentReports reporter = ExtentReportManager.createExtentReport();

    private ExtentTestManager()
    {
    }

    public static synchronized ExtentTest getTest()
    {
        return extentTestMap.get((int) (Thread.currentThread().threadId()));
    }

    public static synchronized void startTest(final String testName, final String description)
    {
        var extentTest = reporter.createTest(testName, description);
        extentTestMap.put((int) (Thread.currentThread().threadId()), extentTest);
    }
}
