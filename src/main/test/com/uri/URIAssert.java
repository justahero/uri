package com.uri;

import org.junit.Assert;

public class URIAssert {
    public static void equals(String expected, String actual) {
        Assert.assertTrue(expected.compareTo(actual) == 0);
    }
}
