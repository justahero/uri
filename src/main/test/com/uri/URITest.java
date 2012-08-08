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
        URIAssert.equals("http", new URI().withScheme("http").scheme());
        URIAssert.equals("ftp", new URI().withScheme("ftp").scheme());
    }
    
    @Test
    public void shouldConstructURIWithHost() throws URISyntaxException {
        URIAssert.equals("example.com", new URI().withHost("example.com").host());
    }
    
    @Test
    public void shouldConstructURIWithRepeatedScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("mailto").withScheme("http").withScheme("ftp");
        URIAssert.equals("ftp", uri.scheme());
    }
    
    @Test
    public void shouldConstructURIWithSchemeAndHost() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        URIAssert.equals("http", uri.scheme());
        URIAssert.equals("example.com", uri.host());
        URIAssert.equals("http://example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIWithPath() throws URISyntaxException {
        URI uri = new URI().withPath("test");
        URIAssert.equals("test", uri.path());
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
    
    //
    // Section 2.3 - Replacements of data octets, that represent characters from the unreserved set
    //
    
    @Test
    public void shouldDecodeHostWithPercentEncodedCharacters() throws URISyntaxException {
        URI uri = URI.parse("http://www.%74%65%73%74.com");
        URIAssert.equals("www.test.com", uri.host());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWhenOnlySingleDigitInOctet() throws URISyntaxException {
        URI.parse("http://www.ex%4.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWithInvalidLetterInOctet() throws URISyntaxException {
        URI.parse("http://www.ex%er%01.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailWithDoublePercentageSigns() throws URISyntaxException {
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
        URIAssert.equals("http://example.com", uri.toASCII());
        URIAssert.equals(uri.toASCII(), URI.parse("http://example.com").toASCII());
    }
    
    @Test
    public void shouldConstructWithSchemeHostAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withPath("path");
        URIAssert.equals("http://example.com/path", uri.toASCII());
    }
    
    @Test
    public void shouldConstructWithSchemeAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withPath("path");
        URIAssert.equals("http:path", uri.toASCII());
    }
    
    @Test
    public void shouldParseWithSchemeAndPath() throws URISyntaxException {
        URI uri = URI.parse("http:path");
        URIAssert.equals("http:path", uri.toASCII());
        URIAssert.equals("http", uri.scheme());
        URIAssert.equals("path", uri.path());
    }
    
    @Test
    public void shouldConstructSiteWithHostAndScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        URIAssert.equals("http://example.com", uri.site());
        URIAssert.equals(uri.site(), URI.parse("http://example.com").toASCII());
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
    
    //
    // Section 1.1.2 of RFC 3986
    //
    @Test
    public void shouldParseFtpURI() throws URISyntaxException {
        URI uri = URI.parse("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        URIAssert.equals("ftp", uri.scheme());
        URIAssert.equals("ftp.is.co.za", uri.host());
        URIAssert.equals("/rfc/rfc1808.txt", uri.path());
        URIAssert.equals("21", uri.inferredPort());
    }
    
    @Test
    public void shouldConstructFtpURI() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("ftp")
            .withHost("ftp.is.co.za")
            .withPath("/rfc/rfc1808.txt");
        URIAssert.equals("ftp://ftp.is.co.za/rfc/rfc1808.txt", uri.toASCII());
    }
    
    @Test
    public void shouldParseHttpURI() throws URISyntaxException {
        URI uri = URI.parse("http://www.ietf.org/rfc/rfc2396.txt");
        URIAssert.equals("http", uri.scheme());
        URIAssert.equals("www.ietf.org", uri.host());
        URIAssert.equals("/rfc/rfc2396.txt", uri.path());
        URIAssert.equals("80", uri.inferredPort());
    }
    
    @Test
    public void shouldConstructHttpURI() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("http")
            .withHost("www.ietf.org")
            .withPath("/rfc/rfc2396.txt");
        URIAssert.equals("http://www.ietf.org/rfc/rfc2396.txt", uri.toASCII());
    }
    
    @Test
    public void shouldParseLdapURI() throws URISyntaxException {
        URI uri = URI.parse("ldap://[2001:db8::7]/c=GB?objectClass?one");
        URIAssert.equals("ldap", uri.scheme());
        URIAssert.equals("[2001:db8::7]", uri.host());
        URIAssert.equals("389", uri.inferredPort());
        URIAssert.equals("/c=GB", uri.path());
        URIAssert.equals("objectClass?one", uri.query());
    }
    
    @Test
    public void shouldConstructLdapURI() throws URISyntaxException {
        URI uri = new URI()
            .withScheme("ldap")
            .withIPV6Host("[2001:db8::7]")
            .withPath("/c=GB")
            .withQuery("objectClass?one");
        URIAssert.equals("ldap://[2001:db8::7]/c=GB?objectClass?one", uri.toASCII());
    }
    
    @Test
    public void shouldParseMailtoURI() throws URISyntaxException {
        URI uri = URI.parse("mailto:John.Doe@example.com");
        URIAssert.equals("mailto", uri.scheme());
        URIAssert.equals("John.Doe@example.com", uri.path());
    }
    
    @Test
    public void shouldConstructMailtoURI() throws URISyntaxException {
        URI uri = new URI().withScheme("mailto").withPath("John.Doe@example.com");
        URIAssert.equals("mailto:John.Doe@example.com", uri.toASCII());
    }
    
    @Test
    public void shouldParseNewsgroupURI() throws URISyntaxException {
        URI uri = URI.parse("news:comp.infosystems.www.servers.unix");
        URIAssert.equals("news", uri.scheme());
        URIAssert.equals("comp.infosystems.www.servers.unix", uri.path());
    }
    
    @Test
    public void shouldConstructNewsgroupURI() throws URISyntaxException {
        URI uri = new URI().withScheme("news").withPath("comp.infosystems.www.servers.unix");
        URIAssert.equals("news:comp.infosystems.www.servers.unix", uri.toASCII());
    }
    
    @Test
    public void shouldParseTelURI() throws URISyntaxException {
        URI uri = URI.parse("tel:+1-816-555-1212");
        URIAssert.equals("tel", uri.scheme());
        URIAssert.equals("+1-816-555-1212", uri.path());
    }
    
    @Test
    public void shouldConstructTelURI() throws URISyntaxException {
        URI uri = new URI().withScheme("tel").withPath("+1-816-555-1212");
        URIAssert.equals("tel:+1-816-555-1212", uri.toASCII());
    }
    
    @Test
    public void shouldParseTelnetURI() throws URISyntaxException {
        URI uri = URI.parse("telnet://192.0.2.16:80/");
        URIAssert.equals("telnet", uri.scheme());
        URIAssert.equals("192.0.2.16", uri.host());
        URIAssert.equals("80", uri.port());
    }
    
    @Test
    public void shouldConstructTelnetURI() throws URISyntaxException {
        URI uri = new URI().withScheme("telnet").withHost("192.0.2.16").withPort(80);
        URIAssert.equals("telnet://192.0.2.16:80", uri.toASCII());
    }
    
    @Test
    public void shouldParseUrnURI() throws URISyntaxException {
        URI uri = URI.parse("urn:oasis:names:specification:docbook:dtd:xml:4.1.2");
        URIAssert.equals("urn", uri.scheme());
        URIAssert.equals("oasis:names:specification:docbook:dtd:xml:4.1.2", uri.path());
    }
    
    @Test
    public void shouldConstructUrnURI() throws URISyntaxException {
        URI uri = new URI().withScheme("urn").withPath("oasis:names:specification:docbook:dtd:xml:4.1.2");
        URIAssert.equals("urn:oasis:names:specification:docbook:dtd:xml:4.1.2", uri.toASCII());
    }
}


