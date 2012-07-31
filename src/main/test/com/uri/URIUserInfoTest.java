package com.uri;

import java.net.URISyntaxException;

import org.junit.Test;

public class URIUserInfoTest {

    // NOTE not really sure if this correct syntax or if there must be at least one character when a
    // '@' sign appears in the authority part
    @Test
    public void userInfoCannotBeEmptyIfAtSignIsPresent() throws URISyntaxException {
        URIAssert.exception("http://@example.com");
    }
    
    @Test
    public void userInfoNameWithInvalidCharacter() throws URISyntaxException {
        URIAssert.exception("http://te[]st:bla@example.com");
        URIAssert.exception("http://<tes>t:foo@example.com");
    }
    
    @Test
    public void userInfoWithNameOnly() throws URISyntaxException {
        URIAssert.user("http://username@example.com", "username", "");
    }
    
    @Test
    public void userInfoWithNameAndColon() throws URISyntaxException {
        URIAssert.user("http://username:@example.com", "username", "");
    }
    
    @Test
    public void userWithNameAndPass() throws URISyntaxException {
        URIAssert.user("http://foo:bar@example.com", "foo", "bar");
    }
    
    @Test
    public void userInfoNameIsNotAllowedToHavePassOnly() throws URISyntaxException {
        URIAssert.exception("http://:pass@example.com");
    }
    
    public void userInfoNameMustNotHaveColonSeparatorOnly() {
        URIAssert.exception("http://:@example.com");
    }
    
    @Test
    public void userInfoOnlyAllowsSingleColon() throws URISyntaxException {
        URIAssert.exception("http://test:double:pass@example.com");
        URIAssert.exception("http://test:bar:@example.com");
        URIAssert.exception("http://:bar:fault@example.com");
    }
    
    @Test
    public void userNameAndPassAreEmptyWhenUserInfoNotGiven() throws URISyntaxException {
        URIAssert.user("http://www.foobar.com", null, null);
    }

}
