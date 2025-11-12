package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import base.BaseClass;
import pages.LoginPage;
import pages.DashboardPage;
import utils.WaitUtil;
import utils.ScreenshotUtil;

import com.aventstack.extentreports.Status;

public class DashboardTest extends BaseClass {

	LoginPage loginPage;
	DashboardPage dashboardPage;
	WaitUtil wait;

	@BeforeMethod
	public void setUpBrowser() {
		setUp();
		loginPage = new LoginPage(driver);
		dashboardPage = new DashboardPage(driver);
		wait = new WaitUtil(driver);
		test = extent.createTest("Dashboard Test - " + this.getClass().getSimpleName());
	}

	@Test(priority = 1)
	public void verifyDashboardAndLogout() throws InterruptedException {
		test.log(Status.INFO, "Starting Dashboard and Logout test");

		// Login...
		test.log(Status.INFO, "Logging in with valid credentials");
		loginPage.login("Admin", "admin123");
		wait.waitForUrlContains("dashboard");
		test.log(Status.PASS, "Login successful, dashboard loaded");

		// Verify dashboard header...
		test.log(Status.INFO, "Verifying dashboard header visibility");
		Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard not displayed!");
		String headerText = dashboardPage.getHeaderText();
		test.log(Status.INFO, "Dashboard Header: " + headerText);

		// Logout...
		test.log(Status.INFO, "Logging out of the application");
		dashboardPage.logout();
		wait.waitForUrlContains("login");

		// Verify user is back to login page...
		String title = loginPage.getPageTitle();
		Assert.assertTrue(title.contains("OrangeHRM"), "Logout failed!");
		test.log(Status.PASS, "Logout successful and user returned to login page");
	}

	@AfterMethod
	public void closeBrowser(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + result.getName() + ".png";
			ScreenshotUtil.captureScreenshot(driver, result.getName());
			test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
			test.addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test passed successfully");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
		}
		tearDown();
	}

	@AfterSuite
	public void endReport() {
		flushReport();
	}
}