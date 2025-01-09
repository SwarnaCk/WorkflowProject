import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginTest {
    private ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();
    private static final int WAIT_TIMEOUT = 20; // 10 seconds timeout

    @BeforeMethod
    public void setUp() {
        loginPage.set(new LoginPage());
        loginPage.get().navigateToLoginPage();
    }

    @Test
    public void testValidLogin() {
        LoginPage page = loginPage.get();
        page.login("swarna.roy@cloudkaptan.com.dev", "Swarna880#");

        // Add wait before checking for absence of error
        WebDriverWait wait = new WebDriverWait(page.getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        try {
            // Wait for any loading indicators to disappear (if they exist)
            wait.until(webDriver -> !page.isLoginErrorDisplayed());
        } catch (Exception e) {
            // If timeout occurs, it means error message is still displayed
            Assert.fail("Login should be successful, but error is still displayed after " + WAIT_TIMEOUT + " seconds.");
        }

        Assert.assertFalse(page.isLoginErrorDisplayed(), "Login should be successful, but error is displayed.");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage page = loginPage.get();
        page.login("invalid_user@example.com", "wrongpassword");

        // Add explicit wait for error message
        WebDriverWait wait = new WebDriverWait(page.getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        try {
            // Wait for error message to be visible
            wait.until(webDriver -> page.isLoginErrorDisplayed());
        } catch (Exception e) {
            Assert.fail("Error message did not appear after " + WAIT_TIMEOUT + " seconds for invalid credentials.");
        }

        // Now verify the error is displayed and has correct message
        Assert.assertTrue(page.isLoginErrorDisplayed(),
                "Login error message should be displayed for invalid credentials.");
        Assert.assertEquals(page.getLoginErrorMessage(),
                "Your login attempt has failed. Make sure the username and password are correct.");
    }

    @AfterMethod
    public void tearDown() {
        if (loginPage.get() != null) {
            loginPage.get().closeBrowser();
            loginPage.remove();
        }
    }
}