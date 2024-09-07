package org.spyne.listener;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.spyne.driverinitialization.WebDriverManager;
import org.spyne.reporter.ExtentTestManager;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class AutomationListener extends TestListenerAdapter
{

    @Override
    public void onTestFailure(final ITestResult result)
    {
        WebDriver driver = WebDriverManager.getDriver();
        String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        ExtentTestManager.getTest().log(Status.FAIL, "FAILED: " + ExtentTestManager.getTest().addScreenCaptureFromBase64String(screenshot)
                .getModel().getMedia().getFirst());
        super.onTestFailure(result);
    }

    @Override
    public void onTestSkipped(final ITestResult result)
    {
        ExtentTestManager.getTest().log(Status.SKIP, "SKIPPED: " + result.getName());
        super.onTestSkipped(result);
    }
}
