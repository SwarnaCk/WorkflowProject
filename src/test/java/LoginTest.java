import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
    private ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        loginPage.set(new LoginPage());
        loginPage.get().navigateToLoginPage();
    }

    @Test
    public void testValidLogin() {
        LoginPage page = loginPage.get();
        page.login("swarna.roy@cloudkaptan.com.dev", "Swarna880#");
        Assert.assertFalse(page.isLoginErrorDisplayed(), "Login should be successful, but error is displayed.");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage page = loginPage.get();
        page.login("invalid_user@example.com", "wrongpassword");
        Assert.assertTrue(page.isLoginErrorDisplayed(), "Login error message should be displayed for invalid credentials.");
        Assert.assertEquals(page.getLoginErrorMessage(), "Your login attempt has failed. Make sure the username and password are correct.");
    }

    @AfterMethod
    public void tearDown() {
        loginPage.get().closeBrowser();
        loginPage.remove();
    }
}

