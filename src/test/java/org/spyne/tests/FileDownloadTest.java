package org.spyne.tests;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spyne.constants.ResourceConstants;
import org.spyne.driverinitialization.WebDriverManager;
import org.spyne.reporter.ExtentReportManager;
import org.spyne.reporter.ExtentTestManager;
import org.spyne.utilities.FolderUtils;
import org.spyne.utilities.ImageUtils;
import org.spyne.utilities.InboxUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;

public class FileDownloadTest
{
    private static final By UPLOAD = By.xpath("//*[@id='__next']/div[2]/section[2]/div/div/div[1]/div[2]/input");
    public static final By PROCESS = By.xpath("//*[@id='__next']//../button[text() = 'Process']");
    private static final String IMAGE_TO_UPSCALE = "flowers_name_in_english.jpg";
    WebDriver driver;

    @Parameters({"browser"})
    @BeforeClass
    public void launchBrowser(String browser) throws IOException, URISyntaxException
    {
        var driverManager = new WebDriverManager();
        driver = driverManager.createDriver(browser);
        driver.manage().window().maximize();
    }

    @Test
    public void fileDownloadTest(final ITestContext testContext) throws IOException
    {
        ExtentTestManager.startTest(testContext.getCurrentXmlTest().getName(), "Test to validate the download feature of upscaled image");
        driver.get("https://www.spyne.ai/image-upscaler");

        // Determine the image resolution of the image to be uploaded
        String inputImagePath = String.valueOf(Paths.get(ResourceConstants.RESOURCES_DIR, IMAGE_TO_UPSCALE));
        int inputImageWidth = ImageUtils.getImageWidth(inputImagePath);
        int inputImageHeight = ImageUtils.getImageHeight(inputImagePath);

        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(arg ->
        {
            try
            {
                driver.findElement(UPLOAD).sendKeys(inputImagePath);
                return true;
            }
            catch (StaleElementReferenceException e)
            {
                return null;
            }
        });

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']/div[2]/div[2]/div/div[2]/div/div[2]/div/div[2]/img")));
        wait.until(ExpectedConditions.elementToBeClickable(PROCESS)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text() = 'Continue with Email']"))).click();
        String randomEmailAddress = InboxUtils.createInbox();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name = 'emailId']"))).sendKeys(randomEmailAddress);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("next"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name = 'owner_name']"))).sendKeys("Varun");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()= 'Continue']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = 'Your account has been created']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text() = 'You’ve got Free Credits!']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id='free-credits-modal']/div[2]/div")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[text() = 'You’ve got Free Credits!']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__next']/div[2]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/button[contains(text(), 'Download')]"))).click();
        String fileExtension = StringUtils.substringAfter(IMAGE_TO_UPSCALE, ".");
        String downloadedFilePath = FolderUtils.getDownloadedFilePath(FolderUtils.getTempDownloadDirectory(), fileExtension);

        int downloadedImageWidth = ImageUtils.getImageWidth(downloadedFilePath);
        int downloadedImageHeight = ImageUtils.getImageHeight(downloadedFilePath);
        Assert.assertEquals(downloadedImageWidth, inputImageWidth * 4);
        Assert.assertEquals(downloadedImageHeight, inputImageHeight * 4);

        ExtentReportManager.reporter.flush();
    }

    @AfterTest
    public void closeBrowser()
    {
        if (driver != null)
            driver.quit();
    }
}
