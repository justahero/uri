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
        Assert.assertEquals("test", uri.path());
    }
    
    @Test(expected=URISyntaxException.class)
    public void hostWithInvalidCharactersThrowsException() throws URISyntaxException {
        new URI().withHost("<invalid>");
    }
    
    @Test(expected=URISyntaxException.class)
    public void hostWithSpaceCharactersThrowsException() throws URISyntaxException {
        new URI().withHost("\t \n");
    }
    
    @Test(expected=URISyntaxException.class)
    public void schemeWithAllNumbersThrowsException() throws URISyntaxException {
        new URI().withScheme("1234");
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
    
    @Test(expected=URISyntaxException.class)
    public void throwsExceptionWithOnlyUsername() throws URISyntaxException {
        new URI().withUserInfo("username", null).toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void throwsExceptionWithOnlyUserpass() throws URISyntaxException {
        new URI().withUserInfo(null, "userpass").toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void throwsExceptionWithOnlySchemeAndFragment() throws URISyntaxException {
        new URI().withScheme("http").withFragment("fragment").toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void throwsExceptionWithOnlyUserInfoAndPort() throws URISyntaxException {
        new URI().withUserInfo("user", "pass").withPort(80).toASCII();
    }
    
    @Test
    public void constructsWithSchemeAndHost() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http://example.com", uri.toASCII());
        Assert.assertEquals(uri.toASCII(), URI.parse("http://example.com").toASCII());
    }
    
    @Test
    public void constructsWithSchemeHostAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withPath("path");
        Assert.assertEquals("http://example.com/path", uri.toASCII());
    }
    
    @Test
    public void constructsWithSchemeAndPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withPath("path");
        Assert.assertEquals("http:path", uri.toASCII());
    }
    
    @Test
    public void parsesURIWithSchemeAndPath() throws URISyntaxException {
        URI uri = URI.parse("http:path");
        Assert.assertEquals("http:path", uri.toASCII());
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("path", uri.path());
    }
    
    @Test
    public void siteConstructsWithHostAndScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http://example.com", uri.site());
        Assert.assertTrue(uri.site().compareTo(URI.parse("http://example.com").toASCII()) == 0);
    }
    
    @Test
    public void hostTransformsToPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URI uri = URI.parse("http://www.%66%6f%6f%62%61%72.com");
        Assert.assertEquals("www.foobar.com", uri.host());
    }
    
    @Test
    public void transformPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        URI uri = URI.parse("http://www.%44%42%50.com"); // => 'DBP'
        Assert.assertEquals("www.dbp.com", uri.host());
    }
    
    @Test
    public void noTransformOfPercentEncodedSpecialCharacters() throws URISyntaxException {
        URI uri = URI.parse("http://www.Test%7B%7D.com");
        Assert.assertEquals("www.test%7B%7D.com", uri.host());
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
    public void shouldParseFtpURICorrectly() throws URISyntaxException {
        URI uri = URI.parse("ftp://ftp.is.co.za/rfc/");
//        URI uri = URI.parse("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        URIAssert.equals("ftp", uri.scheme());
        URIAssert.equals("ftp.is.co.za", uri.host());
        URIAssert.equals("/rfc/rfc1808.txt", uri.path());
        URIAssert.equals("21", uri.inferredPort());
    }
    public void shouldConstructFtpURICorrectly() {
//        URI uri = new URI()
//            .withScheme("ftp")
//            .withHost()
    }
/*
# Section 1.1.2 of RFC 3986
describe Addressable::URI, "when parsed from " +
    "'ftp://ftp.is.co.za/rfc/rfc1808.txt'" do
  before do
    @uri = Addressable::URI.parse("ftp://ftp.is.co.za/rfc/rfc1808.txt")
  end

  it "should use the 'ftp' scheme" do
    @uri.scheme.should == "ftp"
  end

  it "should be considered to be ip-based" do
    @uri.should be_ip_based
  end

  it "should have a host of 'ftp.is.co.za'" do
    @uri.host.should == "ftp.is.co.za"
  end

  it "should have inferred_port of 21" do
    @uri.inferred_port.should == 21
  end

  it "should have a path of '/rfc/rfc1808.txt'" do
    @uri.path.should == "/rfc/rfc1808.txt"
  end

  it "should not have a request URI" do
    @uri.request_uri.should == nil
  end

  it "should be considered to be in normal form" do
    @uri.normalize.should be_eql(@uri)
  end

  it "should have an origin of 'ftp://ftp.is.co.za'" do
    @uri.origin.should == 'ftp://ftp.is.co.za'
  end
end
 */
}


