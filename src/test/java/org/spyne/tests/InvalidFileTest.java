package org.spyne.tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spyne.constants.ResourceConstants;
import org.spyne.driverinitialization.WebDriverManager;
import org.spyne.reporter.ExtentReportManager;
import org.spyne.reporter.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;

public class InvalidFileTest
{
    private static final By UPLOAD = By.xpath("//*[@id='__next']/div[2]/section[2]/div/div/div[1]/div[2]/input");
    private static final By PROCESS = By.xpath("//*[@id='__next']//../button[text() = 'Process']");
    private static final By TOAST_ERROR = By.xpath("//*[@class = 'Toastify__toast-body']/div[2]");

    WebDriver driver;
    @Parameters({"browser"})
    @BeforeTest
    public void launchBrowser(String browser) throws IOException, URISyntaxException
    {
        var driverManager = new WebDriverManager();
        driver = driverManager.createDriver(browser);
        driver.manage().window().maximize();
    }

    @Test
    public void invalidFileUploadTest(final ITestContext testContext)
    {
        ExtentTestManager.startTest(testContext.getCurrentXmlTest().getName(), "Test to validate invalid file upload feature.");
        driver.get("https://www.spyne.ai/image-upscaler");
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(arg ->
        {
            try
            {
                driver.findElement(UPLOAD).sendKeys(String.valueOf(Paths.get(ResourceConstants.RESOURCES_DIR, "v24.17-fix1.1337")));
                return true;
            }
            catch (StaleElementReferenceException e)
            {
                return null;
            }
        });
        ExtentTestManager.getTest().log(Status.PASS, "Upload button found.");
        wait.until(ExpectedConditions.textToBe(TOAST_ERROR, "someting Went wrong"));
        ExtentTestManager.getTest().log(Status.PASS, "Error message found.");
        //Assert.assertEquals(element.getText(), "someting Went wrong");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(TOAST_ERROR));
        ExtentTestManager.getTest().log(Status.PASS, "Error message removed.");
        ExtentReportManager.reporter.flush();
    }

    @AfterTest
    public void closeBrowser()
    {
        if (driver != null)
            driver.quit();
    }
}
