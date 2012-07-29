package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIHostTest {
    
    private void assertHost(String url, String expectedHost) throws URISyntaxException {
        URI uri = new URI(url);
        Assert.assertEquals(expectedHost, uri.host());
    }
    
    @Test
    public void namedHostWithoutUserInfo() throws URISyntaxException {
        assertHost("http://www.example.com", "www.example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void namedHostWithInvalidCharacter() throws URISyntaxException {
        new URI("http://www.foo[bar].com");
    }
    
    @Test
    public void namedHostWithUpperCaseLetters() throws URISyntaxException {
        assertHost("http://www.EXAMPLE.cOm", "www.example.com");
    }
    
    @Test
    public void namedHostWithPortNumber() throws URISyntaxException {
        URI uri = new URI("http://www.test.com:80");
        Assert.assertEquals("80", uri.port());
    }
    
    @Test
    public void ipV4HostWithoutUserInfo() throws URISyntaxException {
        assertHost("http://127.0.0.1", "127.0.0.1");
    }
    
    @Test
    public void ipV4HostWithUserInfo() throws URISyntaxException {
        assertHost("http://foo:bar@33.33.33.10", "33.33.33.10");
    }
    
    @Test
    public void ipV4HostWithPathSeparator() throws URISyntaxException {
        assertHost("http://127.0.0.1/", "127.0.0.1");
    }

    @Test(expected=URISyntaxException.class)
    public void urlWithPortSeparatorButNoNumber() throws URISyntaxException {
        new URI("http://www.example.com:");
    }

    @Test(expected=URISyntaxException.class)
    public void urlContainsInvalidCharactersInPort() throws URISyntaxException {
        new URI("http://www.example.com:9a");
    }

    @Test(expected=URISyntaxException.class)
    public void urlContainsTooLongPort() throws URISyntaxException {
        new URI("http://www.test.de:123456");
    }
    
    @Test
    public void ipV6HostUnicast() throws URISyntaxException {
        assertHost("http://[1080:0:0:0:8:800:200C:417A]", "1080:0:0:0:8:800:200C:417A");
    }
    
    @Test
    public void ipV6HostWithIpV4Part() throws URISyntaxException {
        assertHost("http://[0:0:0:0:0:0:13.1.68.3]/", "0:0:0:0:0:0:13.1.68.3");
        assertHost("http://[0:0:0:0:0:FFFF:129.144.52.38]", "0:0:0:0:0:FFFF:129.144.52.38");
    }
}


