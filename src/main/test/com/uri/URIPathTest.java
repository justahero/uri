package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIPathTest {
    
    @Test
    public void shouldConstructURIWithSinglePathSeparator() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("www.example.com").withPath("/");
        URIAssert.equals("/", uri.path());
        URIAssert.equals("http://www.example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIWithoutPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("test.de");
        Assert.assertEquals(null, uri.path());
        Assert.assertEquals("http://test.de", uri.toASCII());
    }
}
