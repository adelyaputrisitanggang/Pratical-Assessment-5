package com.automation;

import io.qameta.allure.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class SeleniumTest extends BaseTest {
    public String productName = "";


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("DemoBlaze Testing")
    @Feature("Add Product to Cart")
    public void testAddToCart() throws InterruptedException {
        openLaptopCategory();
        navigateToFirstLaptop();
        addToCart();
        closeAlert();
        openCartPage();
        verifyAddedLaptop();
    }

    @Step("Open Laptop Category")
    public void openLaptopCategory() throws InterruptedException {
        waitUntilPageIsFullyLoaded();

        By by = By.xpath("//a[text()=\"Laptops\"]");
        WebElement element = waitUntilElementIsClickable(by);
        element.click();
        Thread.sleep(3000);
    }

    @Step("Navigate to first laptop")
    public void navigateToFirstLaptop() {
        getDriver().findElement(By.xpath("//div[@id=\"tbodyid\"][1]/div[1]//a")).click();
    }

    @Step("Add product to chart")
    public void addToCart() {
        String url = getDriver().getCurrentUrl();
        assertTrue("url is incorrect", url.contains("prod.html"));

        waitUntilPageIsFullyLoaded();

        By by = By.xpath("//a[text()=\"Add to cart\"]");
        WebElement element = waitUntilElementIsClickable(by);

        productName = getDriver().findElement(By.xpath("//h2[@class=\"name\"]")).getText();
        element.click();
    }

    @Step("Close Alert")
    public void closeAlert() throws InterruptedException {
        int i = 0;
        while (i++ < 5) {
            try {
                getDriver().switchTo().alert().accept();
                break;
            } catch (NoAlertPresentException e) {
                Thread.sleep(500);
            }
        }
    }

    @Step("Open Cart Page")
    public void openCartPage() {
        getDriver().findElement(By.xpath("//a[text()=\"Cart\"]")).click();
    }

    @Step("Verify Laptop is Added Correctly")
    public void verifyAddedLaptop() {
        waitUntilPageIsFullyLoaded();

        By by = By.xpath("//tbody/tr");
        waitUntilElementIsVisible(by);
        List<WebElement> productNames = getDriver().findElements(By.xpath("//tbody//tr[1]/td[2]"));

        boolean productPresent = false;
        for (WebElement productRowName : productNames) {
            if (productRowName.getText().equals(productName)) {
                productPresent = true;
            }
        }

        assertTrue("product isn't found in cart", productPresent);
    }

    public void waitUntilPageIsFullyLoaded() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public WebElement waitUntilElementIsClickable(By by) {
        WebDriverWait w = new WebDriverWait(getDriver(), Duration.ofSeconds(10L));
        return w.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void waitUntilElementIsVisible(By by) {
        WebDriverWait w = new WebDriverWait(getDriver(), Duration.ofSeconds(10L));
        w.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}