package org.graduate.shoefastbe.automationtest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class TestView {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriverManager.chromedriver().setup();
        System.out.println("ChromeDriver is set up successfully.");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void testLogin() {
        driver.get("http://localhost:3000/sign-in");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        emailField.sendKeys("phuc1");
        passwordField.sendKeys("1");
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-outline-light")));
        loginBtn.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:3000/", currentUrl);
        sleep(3000);
        System.out.println("Đăng nhập thành công!!");
    }

    @Test
    public void testAddToCart() {
        driver.get("http://localhost:3000/sign-in");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        emailField.sendKeys("phuc1");
        passwordField.sendKeys("1");
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-outline-light")));
        loginBtn.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        WebElement addToCartField = driver.findElement(By.className("btn-outline-primary"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartField);
        sleep(3000);
        WebElement addSizeField = driver.findElement(By.className("form-check-input"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addSizeField);
        sleep(3000);
        List<WebElement> elements = driver.findElements(By.className("btn-outline-dark"));
        WebElement plusItemField = elements.get(1);
        Actions actions = new Actions(driver);
        actions.moveToElement(plusItemField).perform();
        sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", plusItemField);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 200)");
        sleep(2000);
        WebElement addField = driver.findElement(By.cssSelector(".btn.btn-primary.text-white"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addField);
        sleep(3000);
        WebElement goToCartField = driver.findElement(By.cssSelector(".btn.btn-primary.ml-2"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", goToCartField);
        sleep(3000);
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/cart"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:3000/cart", currentUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
