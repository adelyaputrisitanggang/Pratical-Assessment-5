package com.automation;

import com.automation.utils.BrowserManager;
import com.automation.utils.PropertiesLoader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

public class BaseTest {
    protected static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

    public static synchronized WebDriver getDriver() {
        return tdriver.get();
    }

    @BeforeMethod
    public void setUp() throws IOException {
        // Load URL from properties
        String url = PropertiesLoader.loadProperty("url");
        System.out.println("Loaded URL: " + url);  // Debugging URL loading

        // Ensure the URL is not null
        if (url == null) {
            throw new IllegalArgumentException("URL property is not set or loaded correctly.");
        }

        // Setup the WebDriver using BrowserManager
        WebDriver driver = BrowserManager.doBrowserSetup();

        // Ensure WebDriver is not null
        if (driver == null) {
            throw new IllegalStateException("WebDriver setup failed. Please check your BrowserManager configuration.");
        }

        // Set the WebDriver in ThreadLocal
        tdriver.set(driver);

        // Navigate to the URL
        getDriver().get(url);
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser and clean up the ThreadLocal instance
        if (getDriver() != null) {
            getDriver().quit();
            tdriver.remove();
        }
    }
}
