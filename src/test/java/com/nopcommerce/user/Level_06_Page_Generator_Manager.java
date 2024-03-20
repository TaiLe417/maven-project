package com.nopcommerce.user;

import com.github.javafaker.Faker;
import commons.BaseTest;
import commons.PageGeneratorManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.nopCommerce.user.UserCustomerInfoPageObject;
import pageObjects.nopCommerce.user.UserHomePageObject;
import pageObjects.nopCommerce.user.UserLoginPageObject;
import pageObjects.nopCommerce.user.UserRegisterPageObject;

import java.time.Duration;

public class Level_06_Page_Generator_Manager extends BaseTest {
    private WebDriver driver;
    private final String projectPath = System.getProperty("user.dir");
    private String emailAddress, firstName, lastName, password, emailNotFound, unsuccessfulMessage;
    private UserHomePageObject userHomePageObject;
    private UserRegisterPageObject userRegisterPageObject;
    private UserLoginPageObject userLoginPageObject;
    private UserCustomerInfoPageObject userCustomerInfoPageObject;


    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        driver = getBrowserName(browserName);
        Faker faker = new Faker();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get("https://demo.nopcommerce.com/");

        emailAddress = faker.internet().emailAddress();
        emailNotFound = faker.internet().emailAddress();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        password = "123456";
        unsuccessfulMessage = "Login was unsuccessful. Please correct the errors and try again.\nThe credentials provided are incorrect";
        userHomePageObject = PageGeneratorManager.getUserHomePageObject(driver);

        System.out.println("Pre-condition Step 1: Click Register link");
        userRegisterPageObject = userHomePageObject.clickRegisterLink();

        System.out.println("Pre-condition Step 2: Input data into textbox");
        userRegisterPageObject.inputToFirstNameTextbox(firstName);
        userRegisterPageObject.inputToLastNameTextbox(lastName);
        userRegisterPageObject.inputToEmailTextbox(emailAddress);
        userRegisterPageObject.inputToPasswordTextbox(password);
        userRegisterPageObject.inputToConfirmPasswordTextbox(password);

        System.out.println("Pre-condition Step 3: Click register button");
        userRegisterPageObject.clickRegisterButton();

        System.out.println("Pre-condition Step 4: Verify register success message displayed");
        Assert.assertEquals(userRegisterPageObject.getRegisterSuccessMessage(), "Your registration completed");

//        System.out.println("Pre-condition Step 5: Log out");
//        Assert.assertEquals(registerPageObject.getRegisterSuccessMessage(), "Your registration completed");
    }

    @Test
    public void Login_01_EmptyData() {
        System.out.println("Login_01 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_01 Step 2: Click log in button");
        userLoginPageObject.clickLogInButton();

        System.out.println("Login_01 Step 3: Verify email error message displayed");
        Assert.assertEquals(userLoginPageObject.getEmailErrorMessage(), "Please enter your email");
    }

    @Test
    public void Login_02_InvalidData() {
        System.out.println("Login_02 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_02 Step 2: Input invalid mail");
        userLoginPageObject.inputToEmailTextBox("lksjnkn@.mnjs.inc");

        System.out.println("Login_02 Step 3: Click log in button");
        userLoginPageObject.clickLogInButton();

        System.out.println("Login_02 Step 4: Verify wrong email error message displayed");
        Assert.assertEquals(userLoginPageObject.getEmailErrorMessage(), "Wrong email");
    }

    @Test
    public void Login_03_EmailNotFound() {
        System.out.println("Login_03 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_03 Step 2: Input mail unregister");
        userLoginPageObject.inputToEmailTextBox(emailNotFound);

        System.out.println("Login_03 Step 3: Click log in button");
        userLoginPageObject.clickLogInButton();

        System.out.println("Login_03 Step 4: Verify unsuccessful error message displayed");
        Assert.assertEquals(userLoginPageObject.getUnsuccessfulErrorMessage(), "Login was unsuccessful. Please correct the errors and try again.\nNo customer account found");
    }

    @Test
    public void Login_04_ExistEmailEmptyPassword() {
        System.out.println("Login_04 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_04 Step 2: Input mail");
        userLoginPageObject.inputToEmailTextBox(emailAddress);

        System.out.println("Login_04 Step 3: Click log in button");
        userLoginPageObject.clickLogInButton();

        System.out.println("Login_04 Step 4: Verify unsuccessful error message displayed");
        Assert.assertEquals(userLoginPageObject.getUnsuccessfulErrorMessage(), unsuccessfulMessage);
    }

    @Test
    public void Login_05_ExistEmailWrongPassword() {
        System.out.println("Login_05 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_05 Step 2: Input mail");
        userLoginPageObject.inputToEmailTextBox(emailAddress);

        System.out.println("Login_05 Step 3: Input password");
        userLoginPageObject.inputToPasswordTextBox("123321");

        System.out.println("Login_05 Step 2: Click log in button");
        userLoginPageObject.clickLogInButton();

        System.out.println("Login_05 Step 3: Verify unsuccessful error message displayed");
        Assert.assertEquals(userLoginPageObject.getUnsuccessfulErrorMessage(), unsuccessfulMessage);
    }

    @Test
    public void Login_06_ValidEmailPassword() {
        System.out.println("Login_06 Step 1: Click log in link");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        System.out.println("Login_06 Step 2: Input mail");
        userLoginPageObject.inputToEmailTextBox(emailAddress);

        System.out.println("Login_06 Step 3: Input password");
        userLoginPageObject.inputToPasswordTextBox(password);

        System.out.println("Login_06 Step 4: Click log in button");
        userHomePageObject = userLoginPageObject.clickLogInButton();

        System.out.println("Login_06 Step 5: Verify login successful");
        Assert.assertTrue(userHomePageObject.isMyAccountDisplayed());

        userCustomerInfoPageObject = userHomePageObject.clickToMyAccountLink();
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}
