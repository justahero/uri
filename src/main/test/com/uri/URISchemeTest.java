package com.uri;

import java.net.URISyntaxException;

import org.junit.Test;

public class URISchemeTest {
    
    //
    // See section 3.1 of RFC3986, the scheme component
    //
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotStartWithPlusCharacter() throws URISyntaxException {
        URI.parse("+http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotStartWithDashCharacter() throws URISyntaxException {
        URI.parse("-http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotStartWithDotCharacter() throws URISyntaxException {
        URI.parse(".http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotStartWithDigit() throws URISyntaxException {
        URI.parse("4http://www.test.com");
    }
    
    @Test
    public void shouldAllowDigitsInScheme() throws URISyntaxException {
        URI uri = URI.parse("ft9p://example.com");
        URIAssert.equals("ft9p", uri.scheme());
    }
    
    @Test
    public void schemeMustNotContainInvalidCharacters() throws URISyntaxException {
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
        URIAssert.equals("http", URI.parse("http://example").scheme());
        URIAssert.equals("ftp", URI.parse("ftp://user:pass@test.com/here").scheme());
    }
    
    @Test
    public void schemeAllowsDotHyphenAndPlus() throws URISyntaxException {
        URIAssert.equals("t.a+b-c", URI.parse("t.a+b-c://test.com").scheme());
    }
    
}
