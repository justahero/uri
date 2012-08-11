package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URIHostTest {
    
    @Test
    public void namedHostWithoutUserInfo() throws URISyntaxException {
        URIAssert.host("http://www.example.com", "www.example.com");
    }
    
    @Test
    public void namedHostWithInvalidCharacter() throws URISyntaxException {
        URIAssert.exception("http://www.foo[bar].com");
        URIAssert.exception("http://www.<test>.com");
    }
    
    @Test
    public void namedHostWithInvalidFormat() throws URISyntaxException {
        URIAssert.exception("http://<test>");
    }
    
    @Test
    public void namedHostWithUpperCaseLetters() throws URISyntaxException {
        URIAssert.equals("www.example.com", URI.parse("http://www.EXAMPLE.cOm").host());
    }
    
    @Test
    public void namedHostWithPortNumber() throws URISyntaxException {
        Assert.assertEquals(80, URI.parse("http://www.test.com:80").port());
    }
    
    @Test
    public void ipV4HostWithoutUserInfo() throws URISyntaxException {
        URIAssert.host("http://127.0.0.1", "127.0.0.1");
    }
    
    @Test
    public void ipV4HostWithUserInfo() throws URISyntaxException {
        URIAssert.host("http://foo:bar@33.33.33.10", "33.33.33.10");
    }
    
    @Test
    public void ipV4HostWithPathSeparator() throws URISyntaxException {
        URIAssert.host("http://127.0.0.1/", "127.0.0.1");
    }

    @Test
    public void portSeparatorOnlyIsOmitted() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com:");
        URIAssert.equals(URI.parse("http://www.example.com").toASCII(), uri.toASCII());
    }

    @Test(expected=URISyntaxException.class)
    public void portContainsInvalidCharacter() throws URISyntaxException {
        URI.parse("http://www.example.com:9a");
    }
    
    @Test
    public void portNumberIsTooLong() throws URISyntaxException {
        URIAssert.exception("http://www.test.de:123456");
        URIAssert.exception("http://www.test.de:0");
        URIAssert.exception("http://www.test.de:-10");
    }
    
    @Test
    public void ipV6HostUnicast() throws URISyntaxException {
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", URI.parse("http://[1080:0:0:0:8:800:200C:417A]").host());
    }
    
    @Test
    public void ipV6HostWithIpV4Part() throws URISyntaxException {
        URIAssert.equals("[0:0:0:0:0:0:13.1.68.3]", URI.parse("http://[0:0:0:0:0:0:13.1.68.3]/").host());
        URIAssert.equals("[0:0:0:0:0:FFFF:129.144.52.38]", URI.parse("http://[0:0:0:0:0:FFFF:129.144.52.38]").host());
    }
    
    @Test
    public void ipV6HostWithPort() throws URISyntaxException {
        URI uri = URI.parse("http://[1080:0:0:0:8:800:200C:417A]:4040");
        Assert.assertEquals(4040, uri.port());
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", uri.host());
    }
    
    @Test
    public void ipV6HostWithPortAndPathSeparator() throws URISyntaxException {
        URI uri = URI.parse("http://[1080:0:0:0:8:800:200C:417A]:4040/test");
        Assert.assertEquals(4040, uri.port());
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", uri.host());
    }
}


