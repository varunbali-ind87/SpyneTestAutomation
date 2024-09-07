package org.spyne.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spyne.driverinitialization.WebDriverManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;

public class SiteLoadTest
{
    WebDriver driver;

    @Parameters({"browser"})
    @Test
    public void launchBrowser(String browser) throws IOException, URISyntaxException
    {
        var driverManager = new WebDriverManager();
        driver = driverManager.createDriver(browser);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get("https://www.spyne.ai/image-upscaler");
        js.executeScript("window.localStorage.clear();");
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.titleIs("AI Image Upscaler: Upscale Your Image Quality with AI For Free"));
        wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id='__next']//../h1/span"), "Free AI"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']//../button[text() = 'Get a Demo']")));
    }

    @AfterTest
    public void closeBrowser()
    {
        if (driver != null)
            driver.quit();
    }
}
