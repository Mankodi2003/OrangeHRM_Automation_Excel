package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

	public static ExtentReports extent;

	public static ExtentReports getReportInstance() {
		if (extent == null) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String reportName = "TestReport_" + timeStamp + ".html";
			String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;

			ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

			reporter.config().setReportName("OrangeHRM Automation Report");
			reporter.config().setDocumentTitle("Automation Test Report");

			extent = new ExtentReports();

			extent.attachReporter(reporter);

			extent.setSystemInfo("Project", "OrangeHRM");
			extent.setSystemInfo("Jr. Automation Tester", "Prasiddh Mankodi");
			extent.setSystemInfo("Browser", "chrome");
		}
		return extent;
	}
}