package com.uri;

import java.net.URISyntaxException;

import org.junit.Test;

public class URIPathTest {
    
    @Test
    public void pathSeparatorOnly() throws URISyntaxException {
        URIAssert.path("http://www.example.com/", "/");
    }
    
    @Test
    public void urlWithoutPath() throws URISyntaxException {
        URIAssert.path("http://test.de", "");
    }
}
