package tests;

import org.testng.Assert;
import org.testng.annotations.*;

import base.BaseClass;
import pages.LoginPage;
import utils.ExcelUtil;
import utils.WaitUtil;
import utils.ScreenshotUtil;
import com.aventstack.extentreports.Status;

public class LoginTest extends BaseClass {

	LoginPage loginPage;
	WaitUtil wait;

	@BeforeMethod
	public void setUpBrowser() {
		setUp();
		loginPage = new LoginPage(driver);
		wait = new WaitUtil(driver);
		test = extent.createTest("Login Data Driven Test");
	}

	@DataProvider(name = "LoginData")
	public Object[][] getData() {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/TestData.xlsx";
		return ExcelUtil.getSheetData(filePath, "Login");
	}

	@Test(dataProvider = "LoginData")
	public void verifyLoginWithExcel(String username, String password, String expected) {
		test.log(Status.INFO, "Trying login with: " + username + " / " + password);
		loginPage.login(username, password);

		if (expected.equalsIgnoreCase("Pass")) {
			wait.waitForUrlContains("dashboard");
			String title = loginPage.getPageTitle();
			Assert.assertTrue(title.contains("OrangeHRM"));
			test.log(Status.PASS, "Login successful for valid credentials");
		} else {
			String msg = loginPage.getErrorMessage();
			Assert.assertTrue(msg.contains("Invalid credentials"));
			test.log(Status.PASS, "Error message verified for invalid credentials");
		}
	}

	@AfterMethod
	public void closeBrowser(org.testng.ITestResult result) {
		if (result.getStatus() == org.testng.ITestResult.FAILURE) {
			ScreenshotUtil.captureScreenshot(driver, result.getName());
			test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
		}
		tearDown();
	}

	@AfterSuite
	public void endReport() {
		flushReport();
	}
}