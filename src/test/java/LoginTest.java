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
    public void testValidLoginWithDifferentUser() {
        LoginPage page = loginPage.get();
        page.login("harsh.wardhan-2vpx@force.com", "Harsh@73792610");
        Assert.assertFalse(page.isLoginErrorDisplayed(), "Login should be successful, but error is displayed.");
    }

    @AfterMethod
    public void tearDown() {
        loginPage.get().closeBrowser();
        loginPage.remove();
    }
}

