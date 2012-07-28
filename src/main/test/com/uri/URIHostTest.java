package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIHostTest {
    
    @Test
    public void namedHostWithoutUserInfo() throws URISyntaxException {
        URI uri = new URI("http://www.example.com");
        Assert.assertEquals("www.example.com", uri.host());
    }
    
    @Test(expected=URISyntaxException.class)
    public void namedHostWithInvalidCharacter() throws URISyntaxException {
        new URI("http://www.foo[bar].com");
    }
    
    @Test
    public void namedHostWithPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URI uri = new URI("http://www.%66%6f%f%62%61%72.com");
        Assert.assertEquals("www.foobar.com", uri.host());
    }
    
    @Test
    public void namedHostWithPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        URI uri = new URI("http://www.%44%42%50.com"); // => 'DBP'
        Assert.assertEquals("www.dbp.com", uri.host());
    }
    
    @Test
    public void namedHostWithUpperCaseLetters() throws URISyntaxException {
        URI uri = new URI("http://www.EXAMPLE.cOm");
        Assert.assertEquals("www.example.com", uri.host());
    }
    
    @Test
    public void namedHostWithPortNumber() throws URISyntaxException {
        URI uri = new URI("http://www.test.com:80");
        Assert.assertEquals("80", uri.port());
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
    public void ipV4HostWithoutUserInfo() throws URISyntaxException {
        URI uri = new URI("http://127.0.0.1");
        Assert.assertEquals("127.0.0.1", uri.host());
    }
    
    @Test
    public void ipV4HostWithUserInfo() throws URISyntaxException {
        URI uri = new URI("http://foo:bar@33.33.33.10");
        Assert.assertEquals("33.33.33.10", uri.host());
    }
    
    @Test
    public void ipV4HostWithPathSeparator() throws URISyntaxException {
        URI uri = new URI("http://127.0.0.1/");
        Assert.assertEquals("127.0.0.1", uri.host());
    }
    
}


