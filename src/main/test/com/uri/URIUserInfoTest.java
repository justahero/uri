package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URIUserInfoTest {

    // NOTE not really sure if this correct syntax or if there must be at least one character when a
    // '@' sign appears in the authority part
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseEmptyUserInfoWithAtSignPresent() throws URISyntaxException {
        URI.parse("http://@example.com");
    }
    
    @Test
    public void shouldNotParseInvalidCharacterInUsername() throws URISyntaxException {
        URIAssert.exception("http://te[]st:bla@example.com");
        URIAssert.exception("http://<tes>t:foo@example.com");
    }
    
    @Test
    public void shouldParseUsernameOnly() throws URISyntaxException {
        URI uri = URI.parse("http://username@example.com");
        URIAssert.equals("username", uri.username());
        URIAssert.equals("", uri.userpass());
    }
    
    @Test
    public void shouldParseUsernameWithColon() throws URISyntaxException {
        URI uri = URI.parse("http://username:@example.com");
        URIAssert.equals("username", uri.username());
        URIAssert.equals("", uri.userpass());
    }
    
    @Test
    public void shouldParseUsernameAndPass() throws URISyntaxException {
        URI uri = URI.parse("http://foo:bar@example.com");
        URIAssert.equals("foo", uri.username());
        URIAssert.equals("bar", uri.userpass());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseWithOnlyUserpass() throws URISyntaxException {
        URI.parse("http://:pass@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseWithOnlyColonInUserinfo() throws URISyntaxException {
        URI.parse("http://:@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseThreePartsUserInfo() throws URISyntaxException {
        URI.parse("http://test:double:pass@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseUserInfoWithTrailingExtraColon() throws URISyntaxException {
        URI.parse("http://test:bar:@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseUserInfoWithLreadingExtraColon() throws URISyntaxException {
        URI.parse("http://:bar:fault@example.com");
    }
    
    @Test
    public void shouldParseEmptyUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://www.foobar.com");
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
    }

}
