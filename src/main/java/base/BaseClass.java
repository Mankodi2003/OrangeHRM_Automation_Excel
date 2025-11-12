package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import utils.ExtentReportManager;

public class BaseClass {

	protected static WebDriver driver;
	protected static ExtentReports extent;
	protected static ExtentTest test;

	public static void setUp() {

		if (extent == null) {
			extent = ExtentReportManager.getReportInstance();
		}
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://opensource-demo.orangehrmlive.com/");
	}

	public static void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	public void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}
}