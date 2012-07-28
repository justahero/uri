package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIParseTest {
    
    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotPlus() throws URISyntaxException {
        new URI("+http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotHyphen() throws URISyntaxException {
        new URI("-http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotDot() throws URISyntaxException {
        new URI(".ftp://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void schemeMustNotContainOtherOctets() throws URISyntaxException {
        new URI("ft9p://example.com");
    }
    
    @Test
    public void schemeMustStartWithLetter() throws URISyntaxException {
        URI uri = new URI("http://example.com");
        Assert.assertEquals("http", uri.scheme());
    }
    
    @Test
    public void schemeAllowsDotHyphenAndPlus() throws URISyntaxException {
        URI uri = new URI("t.a+b-c://test.com");
        Assert.assertEquals("t.a+b-c", uri.scheme());
    }
    
    // NOTE not really sure if this correct syntax or if there must be at least one character when a
    // '@' sign appears in the authority part
    @Test(expected=URISyntaxException.class)
    public void userInfoCannotBeEmptyIfAtSignIsPresent() throws URISyntaxException {
        URI uri = new URI("http://@example.com");
        Assert.assertEquals("", uri.username());
        Assert.assertEquals("", uri.userpass());
    }
    
    @Test(expected=URISyntaxException.class)
    public void userInfoNameWithInvalidCharacter() throws URISyntaxException {
        new URI("http://te[]st:bla@example.com");
    }
    
    @Test
    public void userInfoWithNameOnly() throws URISyntaxException {
        URI uri = new URI("http://username@example.com");
        Assert.assertEquals("username", uri.username());
        Assert.assertEquals("", uri.userpass());
    }
    
    @Test
    public void userInfoWithNameAndColon() throws URISyntaxException {
        URI uri = new URI("http://username:@example.com");
        Assert.assertEquals("username", uri.username());
        Assert.assertEquals("", uri.userpass());
    }
    
    @Test
    public void userWithNameAndPass() throws URISyntaxException {
        URI uri = new URI("http://foo:bar@example.com");
        Assert.assertEquals("foo", uri.username());
        Assert.assertEquals("bar", uri.userpass());
    }
    
    @Test(expected=URISyntaxException.class)
    public void userInfoNameIsNotAllowedToHavePassOnly() throws URISyntaxException {
        new URI("http://:pass@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void userInfoOnlyAllowsSingleColon() throws URISyntaxException {
        new URI("http://test:double:pass@example.com");
    }
    
    @Test
    public void userNameAndPassAreEmptyWhenUserInfoNotGiven() throws URISyntaxException {
        URI uri = new URI("http://www.foobar.com");
        Assert.assertEquals("", uri.username());
        Assert.assertEquals("", uri.userpass());
    }
    
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


