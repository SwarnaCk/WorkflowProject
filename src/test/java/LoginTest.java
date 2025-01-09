import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
    }

    @Test
    public void testValidLogin() {
        loginPage.login("swarna.roy@cloudkaptan.com.dev", "Swarna880#");
        Assert.assertFalse(loginPage.isLoginErrorDisplayed(), "Login should be successful, but error is displayed.");
    }

    @AfterMethod
    public void tearDown() {
        loginPage.closeBrowser();
    }
}
