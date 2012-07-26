package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URITest {
        
    @Test
    public void validSchemeAndUserName() throws URISyntaxException {
        URI uri = new URI("http://user:pw@example.com");
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("user", uri.username());
        Assert.assertEquals("pw", uri.userpass());
        Assert.assertEquals("user:pw", uri.userinfo());
    }
}

