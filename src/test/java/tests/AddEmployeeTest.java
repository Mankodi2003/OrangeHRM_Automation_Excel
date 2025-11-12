package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import base.BaseClass;
import pages.LoginPage;
import pages.AddEmployeePage;
import utils.WaitUtil;
import utils.ScreenshotUtil;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;

public class AddEmployeeTest extends BaseClass {

	LoginPage loginPage;
	AddEmployeePage addEmpPage;
	WaitUtil wait;

	@BeforeMethod
	public void setUpBrowser() {
		setUp();
		loginPage = new LoginPage(driver);
		addEmpPage = new AddEmployeePage(driver);
		wait = new WaitUtil(driver);
		test = extent.createTest("Add Employee Test - " + this.getClass().getSimpleName());
	}

	@Test(priority = 1)
	public void verifyAddEmployee() throws InterruptedException {
		test.log(Status.INFO, "Starting Add Employee test");

		// Step 1: Login
		test.log(Status.INFO, "Logging in with valid credentials");
		loginPage.login("Admin", "admin123");
		wait.waitForUrlContains("dashboard");
		test.log(Status.PASS, "Login successful, dashboard loaded");

		// Step 2: Navigate to Add Employee page
		test.log(Status.INFO, "Navigating to Add Employee page");
		addEmpPage.openAddEmployeePage();

		// Step 3: Enter employee details and save
		test.log(Status.INFO, "Entering employee details");
		addEmpPage.enterEmployeeDetails("John", "D", "Smith");
		addEmpPage.clickSave();

		// Step 4: Wait for and verify Personal Details header
		wait.waitForElementToBeVisible(By.xpath("//h6[text()='Personal Details']"));
		String header = driver.findElement(By.xpath("//h6[text()='Personal Details']")).getText();
		test.log(Status.INFO, "Header text found: " + header);

		Assert.assertEquals(header, "Personal Details",
				"Employee not added successfully — Personal Details page not displayed!");
		test.log(Status.PASS, "Employee added successfully and verified");
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