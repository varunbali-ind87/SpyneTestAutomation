package org.spyne.reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager
{
    public static final ExtentReports reporter = new ExtentReports();

    public static ExtentReports createExtentReport()
    {
        var sparkReporter = new ExtentSparkReporter("target/SpyneAutomationReport.html");
        reporter.attachReporter(sparkReporter);
        return reporter;
    }
}
