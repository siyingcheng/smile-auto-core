package com.simon.core.asserts;

import com.simon.core.reporter.Reporter;
import org.testng.Assert;

import java.util.Optional;

public class SoftAssertions implements ISmileAssert {
    private final Reporter reporter = Reporter.getInstance();

    @Override
    public void assertEquals(Object actual, Object expected) {
        assertEquals(actual, expected, "Actual and Expected");
    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        Optional<AssertionError> assertionError;
        if (!actual.getClass().equals(expected.getClass())) {
            assertionError = softAssertResult(() -> Assert.assertEquals(actual, expected, message));
        } else {
            assertionError = softAssertResult(() -> Assert.assertEquals(actual.toString(), expected.toString(), message));
        }
        String formattedMessage = getEqualsFormattedMessage(actual, expected, message);
        reporter.reportAssertResult(assertionError.isPresent(), formattedMessage);
    }

    @Override
    public void assertNotNull(Object object, String message) {
        Optional<AssertionError> assertionError = softAssertResult(() -> Assert.assertNotNull(object, message));
        reporter.reportAssertResult(assertionError.isPresent(), message);
    }

    @Override
    public void assertFalse(boolean condition) {
        Optional<AssertionError> assertionError = softAssertResult(() -> Assert.assertFalse(condition));
        reporter.reportAssertResult(assertionError.isPresent(), "Expected result is false");
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        Optional<AssertionError> assertionError = softAssertResult(() -> Assert.assertFalse(condition, message));
        reporter.reportAssertResult(assertionError.isPresent(), message);
    }

    @Override
    public void assertTrue(boolean condition) {
        Optional<AssertionError> assertionError = softAssertResult(() -> Assert.assertTrue(condition));
        reporter.reportAssertResult(assertionError.isPresent(), "Expected result is true");
    }

    @Override
    public void assertTrue(boolean condition, String message) {
        Optional<AssertionError> assertionError = softAssertResult(() -> Assert.assertTrue(condition, message));
        reporter.reportAssertResult(assertionError.isPresent(), message);
    }

    private Optional<AssertionError> softAssertResult(SoftAssertWrapper wrapper) {
        try {
            wrapper.doAssert();
            return Optional.empty();
        } catch (AssertionError e) {
            return Optional.of(e);
        }
    }
}
