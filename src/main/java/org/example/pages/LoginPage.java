package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement txtUsername;

    @FindBy(id = "password")
    private WebElement txtPassword;

    @FindBy(id = "Login")
    private WebElement btnLogin;

    @FindBy(xpath = "//div[contains(@class, 'error') or contains(@class, 'loginError')]")
    private WebElement loginErrorMessage;

    public LoginPage() {
        setupDriver();
        PageFactory.initElements(driver, this);
    }

    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.setPageLoadTimeout(Duration.ofSeconds(15));
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateToLoginPage() {
        driver.get("https://cloudkaptan-16d-dev-ed.develop.my.salesforce.com/");
    }

    public void login(String username, String password) {
        try {
            // Wait for username field and enter username
            wait.until(ExpectedConditions.visibilityOf(txtUsername));
            txtUsername.clear();
            txtUsername.sendKeys(username);

            // Enter password
            txtPassword.clear();
            txtPassword.sendKeys(password);

            // Click login button
            wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
            btnLogin.click();

            // Wait for login to complete (wait for URL change or specific element on next page)
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.urlToBe("https://cloudkaptan-16d-dev-ed.develop.my.salesforce.com/")
            ));

        } catch (Exception e) {
            throw new RuntimeException("Failed to login: " + e.getMessage());
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginErrorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getLoginErrorMessage() {
        if (isLoginErrorDisplayed()) {
            return loginErrorMessage.getText();
        }
        return "";
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Method to get the driver instance if needed by other classes
    public WebDriver getDriver() {
        return driver;
    }
}