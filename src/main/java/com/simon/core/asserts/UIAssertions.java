package com.simon.core.asserts;

import com.simon.core.reporter.Reporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;

public class UIAssertions implements ISmileAssert {
    private final Reporter reporter = Reporter.getInstance();

    private final WebDriver webDriver;

    public UIAssertions(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    @Override
    public void assertEquals(Object actual, Object expected) {
        String formattedMessage = getEqualsFormattedMessage(actual, expected, "Actual and Expected");
        try {
            Assert.assertEquals(actual, expected);
            reporter.log(PASS, formattedMessage);
        } catch (AssertionError e) {
            reporter.log(FAIL, formattedMessage, getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        String formattedMessage = getEqualsFormattedMessage(actual, expected, message);
        try {
            Assert.assertEquals(actual, expected, message);
            reporter.log(PASS, formattedMessage);
        } catch (AssertionError e) {
            reporter.log(FAIL, formattedMessage, getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertNotNull(Object object, String message) {
        try {
            Assert.assertNotNull(object, message);
            reporter.log(PASS, message);
        } catch (AssertionError e) {
            reporter.log(FAIL, message, getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        try {
            Assert.assertFalse(condition, message);
            reporter.log(PASS, message);
        } catch (AssertionError e) {
            reporter.log(FAIL, message, getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertFalse(boolean condition) {
        try {
            Assert.assertFalse(condition);
            reporter.log(PASS, "Expected result is false");
        } catch (AssertionError e) {
            reporter.log(FAIL, "Expected result is false but true", getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
            reporter.log(PASS, message);
        } catch (AssertionError e) {
            reporter.log(FAIL, message, getScreenShotImageBase64());
            throw e;
        }
    }

    @Override
    public void assertTrue(boolean condition) {
        try {
            Assert.assertTrue(condition);
            reporter.log(PASS, "Expected result is true");
        } catch (AssertionError e) {
            reporter.log(FAIL, "Expected result is true but false", getScreenShotImageBase64());
            throw e;
        }
    }

    private String getScreenShotImageBase64() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);
    }
}
