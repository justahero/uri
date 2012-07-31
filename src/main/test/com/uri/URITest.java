package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URITest {
    
    @Test
    public void emptyWhenCreated() {
        URI uri = new URI();
        Assert.assertEquals(null, uri.scheme());
        Assert.assertEquals(null, uri.userinfo());
        Assert.assertEquals(null, uri.host());
        Assert.assertEquals(null, uri.port());
        Assert.assertEquals(null, uri.path());
        Assert.assertEquals(null, uri.query());
        Assert.assertEquals(null, uri.fragment());
    }
    
    @Test
    public void constructsURIWithScheme() throws URISyntaxException {
        Assert.assertEquals("http", new URI().withScheme("http").scheme());
        Assert.assertEquals("ftp", new URI().withScheme("ftp").scheme());
    }
    
    @Test
    public void constructsURIWithHost() throws URISyntaxException {
        Assert.assertEquals("example.com", new URI().withHost("example.com").host());
    }
    
    @Test
    public void constructURIWithRepeatedScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("mailto").withScheme("http").withScheme("ftp");
        Assert.assertEquals("ftp", uri.scheme());
    }
    
    @Test
    public void constructsURIWithSchemeAndHostName() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("example.com", uri.host());
        Assert.assertEquals("http://example.com", uri.toASCII());
    }
    
    @Test
    public void constructsURIWithPath() throws URISyntaxException {
        URI uri = new URI().withPath("test");
        Assert.assertEquals("/test", uri.path());
    }
    
    @Test
    public void hostWithPercentEncodedCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%74%65%73%74.com", "www.test.com");
    }
    
    @Test
    public void hostWithSingleDigitPercentEncodedCharacter() {
        URIAssert.exception("http://www.ex%4.com");
    }
    
    @Test
    public void hostWithLetterInPercentEncodedOctet() {
        URIAssert.exception("http://www.ex%er%01.com");
    }
    
    @Test
    public void hostWithDoublePercentageSigns() {
        URIAssert.exception("http://www.tes%%t.com");
    }
    
    @Test
    public void hostTransformsToPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%66%6f%6f%62%61%72.com", "www.foobar.com");
    }
    
    @Test
    public void transformPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        // => 'DBP'
        URIAssert.host("http://www.%44%42%50.com", "www.dbp.com");
    }
    
    @Test
    public void noTransformOfPercentEncodedSpecialCharacters() throws URISyntaxException {
        URIAssert.host("http://www.Test%7B%7D.com", "www.test%7B%7D.com");
    }
}
