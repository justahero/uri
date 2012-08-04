package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIPathTest {
    
    @Test
    public void pathSeparatorOnly() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("www.example.com").withPath("/");
        Assert.assertEquals("/", uri.path());
        Assert.assertTrue(uri.toASCII().compareTo("http://www.example.com/") == 0);
    }
    
    @Test
    public void urlWithoutPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("test.de");
        Assert.assertEquals(null, uri.path());
        Assert.assertEquals("http://test.de", uri.toASCII());
    }
}
