package com.uri;

import java.net.URISyntaxException;

import org.junit.Test;

public class URISchemeTest {
    
    @Test
    public void schemeMustNotStartWithInvalidCharacter() throws URISyntaxException {
        URIAssert.exception("+http://example.com");
        URIAssert.exception("-http://example.com");
        URIAssert.exception(".ftp://example.com");
    }
    
    @Test
    public void schemeMustNotContainInvalidCharacters() throws URISyntaxException {
        URIAssert.exception("ft9p://example.com");
        URIAssert.exception("ft<>p://example.com");
    }
    
    @Test
    public void schemeMustNotBeNumbers() throws URISyntaxException {
        URIAssert.exception("1234://test.com");
        URIAssert.exception("99://test.com");
    }
    
    @Test
    public void schemeMustContainHierarchicalComponent() throws URISyntaxException {
        URIAssert.exception("ftp:");
        URIAssert.exception("http:");
        URIAssert.exception("mail_to:");
    }
    
    @Test
    public void schemeMustStartWithLetter() throws URISyntaxException {
        URIAssert.scheme("http://example.com", "http");
        URIAssert.scheme("ftp://user:pass@test.com/here", "ftp");
    }
    
    @Test
    public void schemeAllowsDotHyphenAndPlus() throws URISyntaxException {
        URIAssert.scheme("t.a+b-c://test.com", "t.a+b-c");
    }

}
