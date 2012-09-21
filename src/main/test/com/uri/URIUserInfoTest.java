package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIUserInfoTest {

    @Test
    public void shouldParseEmptyUserInfoWithAtSignPresent() throws URISyntaxException {
        URI uri = URI.parse("http://@example.com");
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseUserInfoWithSquareBrackets() throws URISyntaxException {
        URI.parse("http://te[]st:bla@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseUserInfoWithComparisonOperators() throws URISyntaxException {
        URI.parse("http://<tes>t:foo@example.com");
    }
    
    @Test
    public void shouldParseUsernameOnly() throws URISyntaxException {
        URI uri = URI.parse("http://username@example.com");
        URIAssert.equals("username", uri.username());
        Assert.assertNull(uri.userpass());
    }
    
    @Test
    public void shouldParseUsernameWithColon() throws URISyntaxException {
        URI uri = URI.parse("http://username:@example.com");
        URIAssert.equals("username", uri.username());
        Assert.assertNull(uri.userpass());
    }
    
    @Test
    public void shouldParseUsernameAndPass() throws URISyntaxException {
        URI uri = URI.parse("http://foo:bar@example.com");
        URIAssert.equals("foo", uri.username());
        URIAssert.equals("bar", uri.userpass());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseWithOnlyUserpass() throws URISyntaxException {
        URI.parse("http://:pass@example.com").toASCII();
    }
    
    @Test
    public void shouldParseWithOnlyColonInUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://:@example.com");
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
        URIAssert.equals("", uri.userinfo());
        URIAssert.equals("http://example.com", uri.toASCII());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseThreePartsUserInfo() throws URISyntaxException {
        URI.parse("http://test:double:pass@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseUserInfoWithTrailingExtraColon() throws URISyntaxException {
        URI.parse("http://test:bar:@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseUserInfoWithLreadingExtraColon() throws URISyntaxException {
        URI.parse("http://:bar:fault@example.com");
    }
    
    @Test
    public void shouldParseEmptyUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://www.foobar.com");
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
    }
    
    @Test
    public void shouldNotThrowExceptionWhenNameAndPassAreNull() throws URISyntaxException {
        URI uri = new URI().withUserInfo(null, null);
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
        URIAssert.equals("", uri.userinfo());
    }
    
    @Test
    public void shouldNotThrowExceptionWhenNameAndPassAreEmpty() throws URISyntaxException {
        URI uri = new URI().withUserInfo("", "");
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
        URIAssert.equals("", uri.userinfo());
    }
    
    @Test
    public void shouldConstructUserInfoAfterReplacingNameAndPass() throws URISyntaxException {
        URI uri = new URI().withUserInfo("test", "foo");
        URIAssert.equals("test:foo", uri.userinfo());
        uri.withUserInfo(null, null);
        Assert.assertNull(uri.username());
        Assert.assertNull(uri.userpass());
        URIAssert.equals("", uri.userinfo());
    }
}

