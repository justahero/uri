package com.uri;

import java.net.URISyntaxException;

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
    public void namedHostWithUpperCaseLetters() throws URISyntaxException {
        URIAssert.host("http://www.EXAMPLE.cOm", "www.example.com");
    }
    
    @Test
    public void namedHostWithPortNumber() throws URISyntaxException {
        URIAssert.port("http://www.test.com:80", "80");
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
    public void portSeparatorOnlyThrowsException() throws URISyntaxException {
        URIAssert.exception("http://www.example.com:");
    }

    @Test
    public void portContainsInvalidCharacter() throws URISyntaxException {
        URIAssert.exception("http://www.example.com:9a");
    }
    
    @Test
    public void portNumberIsTooLong() throws URISyntaxException {
        URIAssert.exception("http://www.test.de:123456");
        URIAssert.exception("http://www.test.de:0");
        URIAssert.exception("http://www.test.de:-10");
    }
    
    @Test
    public void ipV6HostUnicast() throws URISyntaxException {
        URIAssert.host("http://[1080:0:0:0:8:800:200C:417A]", "1080:0:0:0:8:800:200C:417A");
    }
    
    @Test
    public void ipV6HostWithIpV4Part() throws URISyntaxException {
        URIAssert.host("http://[0:0:0:0:0:0:13.1.68.3]/", "0:0:0:0:0:0:13.1.68.3");
        URIAssert.host("http://[0:0:0:0:0:FFFF:129.144.52.38]", "0:0:0:0:0:FFFF:129.144.52.38");
    }
    
    @Test
    public void ipV6HostWithPort() throws URISyntaxException {
        URIAssert.port("http://[1080:0:0:0:8:800:200C:417A]:4040", "4040");
        URIAssert.host("http://[1080:0:0:0:8:800:200C:417A]:4040", "1080:0:0:0:8:800:200C:417A");
    }
    
    @Test
    public void ipV6HostWithPortAndPathSeparator() throws URISyntaxException {
        URIAssert.port("http://[1080:0:0:0:8:800:200C:417A]:4040/test", "4040");
        URIAssert.host("http://[1080:0:0:0:8:800:200C:417A]:8080/", "1080:0:0:0:8:800:200C:417A");
    }
}


