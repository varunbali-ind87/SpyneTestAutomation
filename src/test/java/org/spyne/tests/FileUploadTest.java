package org.spyne.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spyne.dataprovider.ImageProvider;
import org.spyne.driverinitialization.WebDriverManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;

public class FileUploadTest
{
    private static final By UPLOAD = By.xpath("//*[@id='__next']/div[2]/section[2]/div/div/div[1]/div[2]/input");
    public static final By PROCESS = By.xpath("//*[@id='__next']//../button[text() = 'Process']");
    WebDriver driver;

    @Parameters({"browser"})
    @BeforeClass
    public void launchBrowser(String browser) throws IOException, URISyntaxException
    {
        var driverManager = new WebDriverManager();
        driver = driverManager.createDriver(browser);
        driver.manage().window().maximize();
    }

    @Test(dataProvider = "imageprovider", dataProviderClass = ImageProvider.class)
    public void imageUploadTest(final String imageToUpload)
    {
        driver.get("https://www.spyne.ai/image-upscaler");
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(arg ->
        {
            try
            {
                driver.findElement(UPLOAD).sendKeys(imageToUpload);
                return true;
            }
            catch (StaleElementReferenceException e)
            {
                return null;
            }
        });
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']/div[2]/div[2]/div/div[2]/div/div[2]/div/div[2]/img")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PROCESS));
    }

    @AfterTest
    public void closeBrowser()
    {
        if (driver != null)
            driver.quit();
    }
}
