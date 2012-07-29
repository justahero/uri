package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URIPathTest {
    
    private void assertPath(String url, String expectedPath) throws URISyntaxException {
        Assert.assertEquals(new URI(url).path(), expectedPath);
    }
    
    @Test
    public void pathSeparatorOnly() throws URISyntaxException {
        assertPath("http://www.example.com/", "/");
    }
    
    @Test
    public void urlWithoutPath() throws URISyntaxException {
        assertPath("http://test.de", "");
    }
}
