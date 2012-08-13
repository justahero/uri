package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIPathTest {
    
    @Test
    public void shouldConstructURIWithSinglePathSeparator() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("www.example.com").withPath("/");
        URIAssert.equals("/", uri.path());
        URIAssert.equals("http://www.example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIWithoutPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("test.de");
        Assert.assertEquals(null, uri.path());
        Assert.assertEquals("http://test.de", uri.toASCII());
    }
    
    //
    // Section 3.3 Path sub component
    //
    
    @Test
    public void shouldParseURIWithSeveralSlashesInPath() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/rfc/rfc");
        URIAssert.equals("/rfc/rfc", uri.path());
    }
    
    @Test
    public void shouldParseURIWithEmptyPath() throws URISyntaxException {
        Assert.assertNull(URI.parse("http://www.example.com").path());
        URIAssert.equals("/", URI.parse("http://www.example.com/").path());
    }
    
    @Test
    public void shouldParseURIWithPath() throws URISyntaxException {
        URIAssert.equals("/path", URI.parse("http://test.com/path").path());
        URIAssert.equals("/foo/bar", URI.parse("http://test.com/foo/bar").path());
        URIAssert.equals("/foo/bar%20", URI.parse("http://test.com/foo/bar%20").path());
        URIAssert.equals("/foo@test", URI.parse("http://test.com/foo@test").path());
        URIAssert.equals("/test/:40", URI.parse("http://test.com/test/:40").path());
    }
    
    @Test
    public void shouldParseURIWithAbsolutePath() throws URISyntaxException {
        URIAssert.equals("/c/dev/test.rb", URI.parse("file:///c/dev/test.rb").path());
        URIAssert.equals("/test", URI.parse("file:///test").path());
    }
    
    @Test
    public void shouldParseURIWithAbsolutePathAndSubDelims() throws URISyntaxException {
        URIAssert.equals("/!$&'()*+,;=:@/test", URI.parse("ftp://test@example.com/!$&'()*+,;=:@/test").path());
    }
    
    @Test
    public void shouldParseAbsolutePathWithoutSegment() throws URISyntaxException {
        URIAssert.equals("/", URI.parse("file:///").path());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseAbsolutePathWithBeginningDoubleSlashes() throws URISyntaxException {
        URI.parse("http:////test/foo");
    }
    
    @Test
    public void shouldParseAbsolutePathWithSubDelims() throws URISyntaxException {
        URI uri = URI.parse("http:path!$&'()*+,;=");
        URIAssert.equals("path!$&'()*+,;=", uri.path());
    }
}


