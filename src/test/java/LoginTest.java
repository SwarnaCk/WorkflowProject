import org.example.pages.LoginPage;

public class LoginTest {
    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();
        try {
            loginPage.navigateToLoginPage();
            loginPage.login("swarna.roy@cloudkaptan.com.dev", "Swarna880#");

            if (!loginPage.isLoginErrorDisplayed()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed: " + loginPage.getLoginErrorMessage());
            }
        } finally {
            loginPage.closeBrowser();
        }
    }
}