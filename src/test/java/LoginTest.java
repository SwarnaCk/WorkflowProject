import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import org.testng.Reporter;

public class LoginTest {
    private ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();
    private static final int WAIT_TIMEOUT = 20;

    @BeforeMethod
    public void setUp() {
        loginPage.set(new LoginPage());
        loginPage.get().navigateToLoginPage();
    }

    @Test
    public void testValidLogin() {
        LoginPage page = loginPage.get();
        page.login("swarna.roy@cloudkaptan.com.dev", "Swarna880#");

        WebDriverWait wait = new WebDriverWait(page.getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        try {
            wait.until(webDriver -> !page.isLoginErrorDisplayed());
        } catch (Exception e) {
            Assert.fail("Login should be successful, but error is still displayed after " + WAIT_TIMEOUT + " seconds.");
        }

        Assert.assertFalse(page.isLoginErrorDisplayed(), "Login should be successful, but error is displayed.");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage page = loginPage.get();

        // Log the current URL before login
        Reporter.log("Current URL before login: " + page.getDriver().getCurrentUrl());

        page.login("invalid_user@example.com", "wrongpassword");

        // Log the URL after login attempt
        Reporter.log("URL after login attempt: " + page.getDriver().getCurrentUrl());

        // Add a small initial wait to let the page load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebDriverWait wait = new WebDriverWait(page.getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));

        try {
            // Try different approaches to find the error message
            boolean errorFound = false;

            // Check for the standard error message
            if (page.isLoginErrorDisplayed()) {
                errorFound = true;
            }

            // Check for common error message containers if not found
            if (!errorFound) {
                try {
                    WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(".error-message, .alert-error, .alert-danger, [role='alert']")));
                    if (errorElement != null && errorElement.isDisplayed()) {
                        errorFound = true;
                        Reporter.log("Found error message in alternative container: " + errorElement.getText());
                    }
                } catch (Exception e) {
                    Reporter.log("No alternative error containers found");
                }
            }

            // If still not found, try checking for any visible error text
            if (!errorFound) {
                String pageSource = page.getDriver().getPageSource();
                Reporter.log("Page source after login attempt: " + pageSource);
            }

            // Final assertion
            Assert.assertTrue(errorFound, "No error message found after invalid login attempt");

        } catch (Exception e) {
            Reporter.log("Exception during error message check: " + e.getMessage());
            Assert.fail("Error message did not appear after " + WAIT_TIMEOUT + " seconds for invalid credentials.");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (loginPage.get() != null) {
            loginPage.get().closeBrowser();
            loginPage.remove();
        }
    }
}