package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URITest {
    
    @Test
    public void shouldHaveEmptyPropertiesWhenCreated() {
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
    public void shouldConstructURIWithScheme() throws URISyntaxException {
        Assert.assertEquals("http", new URI().withScheme("http").scheme());
        Assert.assertEquals("ftp", new URI().withScheme("ftp").scheme());
    }
    
    @Test
    public void shouldConstructURIWithHost() throws URISyntaxException {
        Assert.assertEquals("example.com", new URI().withHost("example.com").host());
    }
    
    @Test
    public void shouldConstructURIWithRepeatedScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("mailto").withScheme("http").withScheme("ftp");
        Assert.assertEquals("ftp", uri.scheme());
    }
    
    @Test
    public void shouldConstructURIWithSchemeAndHost() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("example.com", uri.host());
        Assert.assertEquals("http://example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIWithPath() throws URISyntaxException {
        URI uri = new URI().withPath("test");
        Assert.assertEquals("test", uri.path());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWithHostContainingInvalidCharacters() throws URISyntaxException {
        new URI().withHost("<invalid>");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenHostContainsSpaces() throws URISyntaxException {
        new URI().withHost("\t \n");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenHostIsAllNumbers() throws URISyntaxException {
        new URI().withScheme("1234");
    }
    
    @Test
    public void shouldDecodeHostWithPercentEncodedCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%74%65%73%74.com", "www.test.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenOnlySingleDigitInOctet() throws URISyntaxException {
        URI.parse("http://www.ex%4.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailedWithInvalidLetterInOctet() throws URISyntaxException {
        URI.parse("http://www.ex%er%01.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailedWithDoublePercentageSigns() throws URISyntaxException {
        URI.parse("http://www.tes%%t.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenOnlyUserNameIsPresent() throws URISyntaxException {
        new URI().withUserInfo("username", null).toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenOnlyUserPassIsPresent() throws URISyntaxException {
        new URI().withUserInfo(null, "userpass").toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWithOnlySchemeAndFragment() throws URISyntaxException {
        new URI().withScheme("http").withFragment("fragment").toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWithOnlyUserInfoAndPort() throws URISyntaxException {
        new URI().withUserInfo("user", "pass").withPort(80).toASCII();
    }
    
    @Test
    public void shouldConstructWithSchemeAndHost() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http://example.com", uri.toASCII());
        Assert.assertEquals(uri.toASCII(), URI.parse("http://example.com").toASCII());
    }
    
    @Test
    public void shouldConstructWithSchemeHostAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withPath("path");
        Assert.assertEquals("http://example.com/path", uri.toASCII());
    }
    
    @Test
    public void shouldConstructWithSchemeAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withPath("path");
        Assert.assertEquals("http:path", uri.toASCII());
    }
    
    @Test
    public void shouldParseWithSchemeAndPath() throws URISyntaxException {
        URI uri = URI.parse("http:path");
        Assert.assertEquals("http:path", uri.toASCII());
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("path", uri.path());
    }
    
    @Test
    public void shouldConstructSiteWithHostAndScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http://example.com", uri.site());
        Assert.assertTrue(uri.site().compareTo(URI.parse("http://example.com").toASCII()) == 0);
    }
    
    @Test
    public void shouldTransformLetterOctetsToLowerCase() throws URISyntaxException {
        URI uri = URI.parse("http://www.%66%6f%6f%62%61%72.com");
        URIAssert.equals("www.foobar.com", uri.host());
    }
    
    @Test
    public void shouldTransformUppercaseOctetsToLowercase() throws URISyntaxException {
        URI uri = URI.parse("http://www.%44%42%50.com"); // => 'DBP'
        URIAssert.equals("www.dbp.com", uri.host());
    }
    
    @Test
    public void shouldNotTransformSpecialEncodedCharacters() throws URISyntaxException {
        URI uri = URI.parse("http://www.Test%7B%7D.com");
        URIAssert.equals("www.test%7B%7D.com", uri.host());
    }
    
    @Test
    public void shouldParseURIWithSeveralSlashesInPath() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/rfc/rfc");
        URIAssert.equals("/rfc/rfc", uri.path());
    }
    
    @Test
    public void shouldConstructURIWithSeveralSlashesInPath() throws URISyntaxException {
        URI uri = new URI().withPath("/test/foo/bar");
        URIAssert.equals("/test/foo/bar", uri.path());
    }
    
    @Test
    public void test() throws URISyntaxException {
       URI.parse("ldap://[2001:db8::7]/c=GB?objectClass");
        //URI.parse("ldap://[2001:db8::7]/c=GB?objectClass?one");
    }
    
    //
    // Section 1.1.2 of RFC 3986
    //
    @Test
    public void shouldParseFtpURICorrectly() throws URISyntaxException {
        URI uri = URI.parse("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        URIAssert.equals("ftp", uri.scheme());
        URIAssert.equals("ftp.is.co.za", uri.host());
        URIAssert.equals("/rfc/rfc1808.txt", uri.path());
        URIAssert.equals("21", uri.inferredPort());
    }
    
    @Test
    public void shouldConstructFtpURICorrectly() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("ftp")
            .withHost("ftp.is.co.za")
            .withPath("/rfc/rfc1808.txt");
        URIAssert.equals("ftp://ftp.is.co.za/rfc/rfc1808.txt", uri.toASCII());
    }
    
    @Test
    public void shouldParseHttpURICorrectly() throws URISyntaxException {
        URI uri = URI.parse("http://www.ietf.org/rfc/rfc2396.txt");
        URIAssert.equals("http", uri.scheme());
        URIAssert.equals("www.ietf.org", uri.host());
        URIAssert.equals("/rfc/rfc2396.txt", uri.path());
        URIAssert.equals("80", uri.inferredPort());
    }
    
    @Test
    public void shouldConstructHttpURICorrectly() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("http")
            .withHost("www.ietf.org")
            .withPath("/rfc/rfc2396.txt");
        URIAssert.equals("http://www.ietf.org/rfc/rfc2396.txt", uri.toASCII());
    }
    
    @Test
    public void shouldParseLdapURICorrectly() throws URISyntaxException {
        URI uri = URI.parse("ldap://[2001:db8::7]/c=GB?objectClass?one");
        URIAssert.equals("ldap", uri.scheme());
        URIAssert.equals("[2001:db8::7]", uri.host());
        URIAssert.equals("389", uri.inferredPort());
        URIAssert.equals("/c=GB", uri.path());
        URIAssert.equals("objectClass?one", uri.query());
    }
    
    @Test
    public void shouldConstructLdapURICorrectly() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("ldap")
            .withHost("[2001:db8:7]")
            .withPath("/c=GB")
            .withQuery("objectClass?one");
        URIAssert.equals("ldap://[2001:db8::7]/c=GB?objectClass?one", uri.toASCII());
    }
    
    // ldap://[2001:db8::7]/c=GB?objectClass?one
}



