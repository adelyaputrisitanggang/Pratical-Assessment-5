package com.automation;

import io.qameta.allure.*;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        accessLaptopCategory();
        goToFirstLaptop();
        addProductToCart();
        dismissAlert();
        navigateToCartPage();
        checkLaptopAdded();
    }

    @Step("Access Laptop Category")
    public void accessLaptopCategory() throws InterruptedException {
        ensurePageIsFullyLoaded();

        By locator = By.xpath("//a[text()=\"Laptops\"]");
        WebElement clickableElement = waitForElementToBeClickable(locator);
        clickableElement.click();
        Thread.sleep(3000);
    }

    @Step("Go to First Laptop")
    public void goToFirstLaptop() {
        getDriver().findElement(By.xpath("//div[@id=\"tbodyid\"][1]/div[1]//a")).click();
    }

    @Step("Add Product to Cart")
    public void addProductToCart() {
        String currentUrl = getDriver().getCurrentUrl();
        assertTrue("The URL is not correct", currentUrl.contains("prod.html"));

        ensurePageIsFullyLoaded();

        By locator = By.xpath("//a[text()=\"Add to cart\"]");
        WebElement clickableElement = waitForElementToBeClickable(locator);

        productName = getDriver().findElement(By.xpath("//h2[@class=\"name\"]")).getText();
        clickableElement.click();
    }

    @Step("Dismiss Alert")
    public void dismissAlert() throws InterruptedException {
        int attempts = 0;
        while (attempts++ < 5) {
            try {
                getDriver().switchTo().alert().accept();
                break;
            } catch (NoAlertPresentException e) {
                Thread.sleep(500);
            }
        }
    }

    @Step("Navigate to Cart Page")
    public void navigateToCartPage() {
        getDriver().findElement(By.xpath("//a[text()=\"Cart\"]")).click();
    }

    @Step("Check Laptop Added Correctly")
    public void checkLaptopAdded() {
        ensurePageIsFullyLoaded();

        By locator = By.xpath("//tbody/tr");
        waitForElementToBeVisible(locator);
        List<WebElement> productRows = getDriver().findElements(By.xpath("//tbody//tr[1]/td[2]"));

        boolean isProductFound = false;
        for (WebElement productRow : productRows) {
            if (productRow.getText().equals(productName)) {
                isProductFound = true;
            }
        }

        assertTrue("The product was not found in the cart", isProductFound);
    }

    public void ensurePageIsFullyLoaded() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10L));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
