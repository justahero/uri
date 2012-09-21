package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URISchemeTest {
    
    //
    // See section 3.1 of RFC3986, the scheme component
    //
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseSchemeWithPlusCharacter() throws URISyntaxException {
        URI.parse("+http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseSchemeWithDashCharacter() throws URISyntaxException {
        URI.parse("-http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseSchemeWithDotCharacter() throws URISyntaxException {
        URI.parse(".http://example.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shoulFailToParseSchemeWithLeadingDigit() throws URISyntaxException {
        URI.parse("4http://www.test.com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shoulFailToParseSchemeWithInvalidCharacters() throws URISyntaxException {
        URI.parse("ft<>p://example.com");
    }
    
    @Test
    public void shouldAllowDigitsInScheme() throws URISyntaxException {
        URI uri = URI.parse("ft9p://example.com");
        URIAssert.equals("ft9p", uri.scheme());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToConstructURIWithoutHierarchicalComponent() throws URISyntaxException {
        new URI().withScheme("ftp").toASCII();
    }
    
    @Test
    public void shouldStartWithLetter() throws URISyntaxException {
        URIAssert.equals("http", URI.parse("http://example").scheme());
        URIAssert.equals("ftp", URI.parse("ftp://user:pass@test.com/here").scheme());
    }
    
    @Test
    public void shouldAllowDotHyphenAndPlus() throws URISyntaxException {
        URIAssert.equals("t.a+b-c", URI.parse("t.a+b-c://test.com").scheme());
    }
    
    @Test
    public void shouldParseURIAndTransformSchemeToLowerCase() throws URISyntaxException {
        URI uri = URI.parse("hTTP://www.example.com");
        URIAssert.equals("http", uri.scheme());
        URIAssert.equals("http://www.example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIAndTransformSchemeToLowerCase() throws URISyntaxException {
        URI uri = new URI().withScheme("TELNET").withHost("example.com");
        URIAssert.equals("telnet", uri.scheme());
    }
    
    @Test
    public void shouldReassignSchemeAfterParsingURI() throws URISyntaxException {
        URI uri = URI.parse("http://example.com").withScheme(null);
        Assert.assertNull(uri.scheme());
    }
}
