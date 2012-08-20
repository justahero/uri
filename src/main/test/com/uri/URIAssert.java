package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

public class URIAssert {
    
    public static void exception(String url) {
        try {
            URI.parse(url);
            Assert.fail("Should fail for url: " + url);
        } catch (URISyntaxException e) {
        }
    }
    
    public static void user(String url, String username, String userpass) throws URISyntaxException {
        URI uri = URI.parse(url);
        Assert.assertEquals(username, uri.username());
        Assert.assertEquals(userpass, uri.userpass());
    }
    
    public static void equals(String expected, String actual) {
        Assert.assertTrue(expected.compareTo(actual) == 0);
    }
}
