package com.simon.core.asserts;

import org.apache.commons.lang3.StringUtils;

import static com.simon.core.reporter.HtmlMarkup.lineBreak;

public interface ISmileAssert {

    void assertEquals(Object actual, Object expected);

    void assertEquals(Object actual, Object expected, String message);

    void assertNotNull(Object object, String message);

    void assertFalse(boolean condition, String message);

    void assertFalse(boolean condition);

    void assertTrue(boolean condition, String message);

    void assertTrue(boolean condition);

    default String getEqualsFormattedMessage(Object actual, Object expected, String message) {
        return """
                %s %s
                A: %s %s
                E: %s %s
                """.formatted(StringUtils.isEmpty(message) ? "" : message, lineBreak(), actual, lineBreak(), expected, lineBreak());
    }
}
