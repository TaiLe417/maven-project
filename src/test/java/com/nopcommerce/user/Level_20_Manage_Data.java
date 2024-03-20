package com.nopcommerce.user;

import com.github.javafaker.Faker;
import commons.BaseTest;
import commons.PageGeneratorManager;
import data.UserDataMapper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.nopCommerce.user.*;
import reportConfig.ExtentTestManagerV5;

import java.lang.reflect.Method;

public class Level_20_Manage_Data extends BaseTest {
    UserDataMapper userData;
    private WebDriver driver;
    private String emailAddress;
    private UserHomePageObject userHomePageObject;
    private UserRegisterPageObject userRegisterPageObject;
    private UserLoginPageObject userLoginPageObject;
    private UserCustomerInfoPageObject userCustomerInfoPageObject;


    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        driver = getBrowserName(browserName);
        Faker faker = new Faker();
        userData = UserDataMapper.getUserData();
        userHomePageObject = PageGeneratorManager.getUserHomePageObject(driver);
        emailAddress = userData.getEmailAddress() + faker.number().randomNumber() + "@fakemail.com";
    }

    @Test
    public void User_01_Register(Method method) {
        ExtentTestManagerV5.startTest(method.getName(), "Register to system with valid Email and Password");
        log.info("Register - Step 01: Navigate to 'Register' page");
        userRegisterPageObject = userHomePageObject.clickRegisterLink();

        userRegisterPageObject.clickToRadioButtonByLabel(driver, "Male");

        log.info("Register - Step 02: Enter the Firstname textbox with value is '" + userData.getFirstName() + "'");
//        userRegisterPageObject.inputToFirstNameTextbox(firstName);
        userRegisterPageObject.inputToTextboxByID(driver, "FirstName", userData.getFirstName());

        log.info("Register - Step 03: Enter the Lastname textbox with value is '" + userData.getLastName() + "'");
//        userRegisterPageObject.inputToLastNameTextbox(lastName);
        userRegisterPageObject.inputToTextboxByID(driver, "LastName", userData.getLastName());

        userRegisterPageObject.selectToDropDownByName(driver, "DateOfBirthDay", userData.getDate());
        userRegisterPageObject.selectToDropDownByName(driver, "DateOfBirthMonth", userData.getMonth());
        userRegisterPageObject.selectToDropDownByName(driver, "DateOfBirthYear", userData.getYear());

        log.info("Register - Step 04: Enter the Email textbox with value is '" + emailAddress + "'");
//        userRegisterPageObject.inputToEmailTextbox(emailAddress);
        userRegisterPageObject.inputToTextboxByID(driver, "Email", emailAddress);

        userRegisterPageObject.clickToCheckboxByLabel(driver, "Newsletter");

        log.info("Register - Step 05: Enter the Password textbox with value is '" + userData.getPassword() + "'");
//        userRegisterPageObject.inputToPasswordTextbox(password);
        userRegisterPageObject.inputToTextboxByID(driver, "Password", userData.getPassword());

        log.info("Register - Step 06: Enter the Confirm Password textbox with value is '" + userData.getPassword() + "'");
//        userRegisterPageObject.inputToConfirmPasswordTextbox(password);
        userRegisterPageObject.inputToTextboxByID(driver, "ConfirmPassword", userData.getPassword());

        log.info("Register - Step 07: Click to 'Register button'");
        userRegisterPageObject.clickToButtonByText(driver, "Register");

        log.info("Register - Step 08: Verify register success message is displayed");
        verifyEquals(userRegisterPageObject.getRegisterSuccessMessage(), "Your registration completed");
    }

    @Test
    public void User_02_Login(Method method) {
        ExtentTestManagerV5.startTest(method.getName(), "Login to system with valid Email and Password");
        log.info("Login - Step 01:  Navigate to Login page");
        userLoginPageObject = userHomePageObject.clickLogInLink();

        log.info("Login - Step 02: enter the Email textbox with value is '" + emailAddress + "'");
        userLoginPageObject.inputToTextboxByID(driver, "Email", emailAddress);

        log.info("Login - Step 03: enter the Password textbox with value is '" + userData.getPassword() + "'");
        userLoginPageObject.inputToTextboxByID(driver, "Password", userData.getPassword());

        log.info("Login - Step 04: Click Log In button");
        userLoginPageObject.clickToButtonByText(driver, "Log in");
        userHomePageObject = PageGeneratorManager.getUserHomePageObject(driver);


        log.info("Login - Step 05: Verify 'My Account' link is displayed");
        verifyTrue(userHomePageObject.isMyAccountDisplayed());


    }

    @Test
    public void User_03_MyAccount(Method method) {
        ExtentTestManagerV5.startTest(method.getName(), "Verify My Account");
        log.info("My Account - Step 01: Navigate to 'My Account page'");
        userCustomerInfoPageObject = userHomePageObject.clickToMyAccountLink();

        log.info("My Account - Step 02: Customer Info page is displayed");
        Assert.assertTrue(userCustomerInfoPageObject.isCustomerInfoDisplayed());

        log.info("My Account - Step 03: Verify 'First Name' value is correctly");
        Assert.assertEquals(userCustomerInfoPageObject.getTextboxValueByID(driver, "FirstName"), userData.getFirstName());

        log.info("My Account - Step 04: Verify 'Last Name' value is correctly");
        Assert.assertEquals(userCustomerInfoPageObject.getTextboxValueByID(driver, "LastName"), userData.getLastName());

        log.info("My Account - Step 01:  Verify 'Email' value is correctly");
        Assert.assertEquals(userCustomerInfoPageObject.getTextboxValueByID(driver, "Email"), emailAddress);

    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeBrowserDriver();
    }
}
