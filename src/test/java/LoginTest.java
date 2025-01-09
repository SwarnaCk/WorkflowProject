import org.example.pages.LoginPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = new LoginPage();
    }

    @Test
    void testLoginSuccess() {
        loginPage.navigateToLoginPage();
        loginPage.login(
                System.getenv("SALESFORCE_USERNAME"),
                System.getenv("SALESFORCE_PASSWORD")
        );
        assertFalse(loginPage.isLoginErrorDisplayed(), "Login should be successful");
    }

    @AfterEach
    void tearDown() {
        if (loginPage != null) {
            loginPage.closeBrowser();
        }
    }
}