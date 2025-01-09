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
        options.addArguments("--headless");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateToLoginPage() {
        driver.get("https://cloudkaptan-16d-dev-ed.develop.my.salesforce.com/");
    }

    public void login(String username, String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtUsername));
            txtUsername.clear();
            txtUsername.sendKeys(username);

            txtPassword.clear();
            txtPassword.sendKeys(password);

            wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
            btnLogin.click();

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

    public WebDriver getDriver() {
        return driver;
    }
}
