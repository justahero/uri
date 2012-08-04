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
    
    @Test(expected=URISyntaxException.class)
    public void schemeMustContainHierarchicalComponent() throws URISyntaxException {
        new URI().withScheme("ftp").toASCII();
    }
    
    @Test
    public void schemeMustStartWithLetter() throws URISyntaxException {
        URIAssert.equals("http",URI.parse("http://example").scheme());
        URIAssert.equals("ftp",URI.parse("ftp://user:pass@test.com/here").scheme());
    }
    
    @Test
    public void schemeAllowsDotHyphenAndPlus() throws URISyntaxException {
        URIAssert.equals("t.a+b-c", URI.parse("t.a+b-c://test.com").scheme());
    }

}
