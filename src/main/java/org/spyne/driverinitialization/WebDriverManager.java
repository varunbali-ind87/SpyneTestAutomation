package org.spyne.driverinitialization;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.spyne.utilities.FolderUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebDriverManager
{

    public static final String URL = "http://127.0.0.1:4444/wd/hub";
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public WebDriver createDriver(String browser) throws IOException, URISyntaxException
    {
        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome"))
        {
            var chromeOptions = new ChromeOptions();
            chromeOptions.setCapability("browserName", "chrome");
            // Set download directory
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", FolderUtils.createTempDownloadDirectory());
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            chromeOptions.setExperimentalOption("prefs", prefs);
            driver = new RemoteWebDriver(new URI(URL).toURL(), chromeOptions);
            threadDriver.set(driver);
            return driver;
        }
        else if (browser.equalsIgnoreCase("firefox"))
        {
            var firefoxOptions = new FirefoxOptions();
            driver = new RemoteWebDriver(new URI(URL).toURL(), firefoxOptions);
            threadDriver.set(driver);
            return driver;
        }
        else if (browser.equalsIgnoreCase("edge"))
        {
            var edgeOptions = new EdgeOptions();
            // Set download directory
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", FolderUtils.createTempDownloadDirectory());
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            edgeOptions.setExperimentalOption("prefs", prefs);
            var capabilities = new DesiredCapabilities();
            capabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
            driver = new RemoteWebDriver(new URI(URL).toURL(), capabilities);
            threadDriver.set(driver);
            return driver;
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported browser: " + browser);
        }
    }

    public static WebDriver getDriver()
    {
        return threadDriver.get();
    }
}
