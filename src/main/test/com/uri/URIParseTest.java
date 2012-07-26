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
    
    @Test(expected=URISyntaxException.class)
    public void userInfoCannotBeEmptyIfAtSignIsPresent() throws URISyntaxException {
        new URI("http://@example.com");
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
    
    // NOTE this point is not exactly clear in the RFC 3986 but it seems a user name must be given
    // when a user pass is present.
    @Test(expected=URISyntaxException.class)
    public void userInfoNameIsNotAllowedToHavePassOnly() throws URISyntaxException {
        new URI("http://:pass@example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void userInfoOnlyAllowsSingleColon() throws URISyntaxException {
        new URI("http://test:double:pass@example.com");
    }
}

