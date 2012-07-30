package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

public class URIAssert {
    
    public static void exception(String url) {
        try {
            new URI(url);
            Assert.fail("Should fail for url: " + url);
        } catch (URISyntaxException e) {
        }
    }
    
    public static void assertScheme(String url, String expectedScheme) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedScheme, uri.scheme());
    }
    
    public static void host(String url, String expectedHost) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedHost, uri.host());
    }
    
    public static void port(String url, String expectedPort) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedPort, uri.port());
    }
    
    public static void asserPath(String url, String expectedPath) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedPath, uri.path());
    }

    public static void scheme(String url, String expectedScheme) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedScheme, uri.scheme());
    }

    public static void path(String url, String expectedPath) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedPath, uri.path());
    }

    public static void user(String url, String username, String userpass) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(username, uri.username());
        Assert.assertEquals(userpass, uri.userpass());
    }
}
