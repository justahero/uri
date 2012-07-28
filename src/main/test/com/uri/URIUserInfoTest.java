package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIUserInfoTest {

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

}
