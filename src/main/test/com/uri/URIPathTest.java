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
    public void shouldParseURIWithPathSeparator() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com/");
        Assert.assertNotNull(uri.path());
        URIAssert.equals("/", uri.path());
        URIAssert.equals("/", uri.requestURI());
        URIAssert.equals("http://www.example.com", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIWithoutPath() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("test.de");
        Assert.assertEquals(null, uri.path());
        URIAssert.equals("http://test.de", uri.toASCII());
        URIAssert.equals("", uri.requestURI());
    }
    
    @Test
    public void shouldParseURIWithEmptyPath() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com");
        Assert.assertNull(uri.path());
        URIAssert.equals("", uri.requestURI());
    }
    
    @Test
    public void shouldConstructURIWithFirstPathThenHost() throws URISyntaxException {
        URI uri = new URI().withPath("path/to/resource").withHost("example.com").withScheme("http");
        URIAssert.equals("http://example.com/path/to/resource", uri.toASCII());
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
    public void shouldFailToParseAbsolutePathWithBeginningDoubleSlashes() throws URISyntaxException {
        URI.parse("http:////test/foo").toASCII();
    }
    
    @Test
    public void shouldParseAbsolutePathWithSubDelims() throws URISyntaxException {
        URI uri = URI.parse("http:path!$&'()*+,;=");
        URIAssert.equals("path!$&'()*+,;=", uri.path());
    }
    
    @Test
    public void shouldParseURIWithRelativePath() throws URISyntaxException {
        URI uri = URI.parse("/relative/path/to/file");
        Assert.assertNull(uri.scheme());
        URIAssert.equals("", uri.authority());
        URIAssert.equals("/relative/path/to/file", uri.path());
        URIAssert.equals("/relative/path/to/file", uri.toASCII());
    }
    /*
    @Test(expected=URISyntaxException.class)
    public void shouldFailToAssignRelativePathToAbsoluteURI() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withPath("acct:foo@test.com");
        uri.toASCII();
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToAssignRelativePathToAbsoluteURI2() throws URISyntaxException {
        URI uri = new URI().withScheme("urn").withPath("uuid:0b3ecf60-3f93-11df-a9c3-001f5bfffe12");
        uri.toASCII();
    }
    */
    
    @Test
    public void shouldConstructURIWithRelativePath() throws URISyntaxException {
        URI uri = new URI().withPath("/relative/foo/bar");
        Assert.assertNull(uri.scheme());
        URIAssert.equals("", uri.authority());
        URIAssert.equals("/relative/foo/bar", uri.path());
        URIAssert.equals("/relative/foo/bar", uri.toASCII());
    }
    
    @Test
    public void shouldParseRelativePathWithSubDelims() throws URISyntaxException {
        URI uri = URI.parse("/relative/!$&'()*+,;=/test");
        Assert.assertNotNull(uri.path());
        URIAssert.equals("/relative/!$&'()*+,;=/test", uri.path());
    }
    
    //
    // Example URIs in section 3.3
    //
    
    @Test
    public void shouldParseEMailURIWithPath() throws URISyntaxException {
        URI uri = URI.parse("mailto:fred@example.com");
        URIAssert.equals("fred@example.com", uri.path());
    }
    
    @Test
    public void shouldParsePathWithPercentEncodedOctetsAndNoScheme() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/test%20/");
        URIAssert.equals("/test%20/", uri.path());
    }
    
    @Test
    public void shouldConstructRelativePathWithPercentEncodedLetters() throws URISyntaxException {
        URI uri = new URI().withPath("/%74%65%73%74/foo");
        URIAssert.equals("/test/foo", uri.path());
    }
    
    @Test
    public void shouldConstructRelativePathWithUpperCasePercentEncodedLetters() throws URISyntaxException {
        URI uri = new URI().withPath("/%46%4f%4F/bar");
        URIAssert.equals("/FOO/bar", uri.path());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldFailToParseRelativePathWithMisformedPercentOctet() throws URISyntaxException {
        new URI().withPath("/foo/%gg/").toASCII();
    }
    
    //
    // Normalizing and Percent Encoding of path component
    //
    
    @Test
    public void shouldParseURIWithEncodedUnreservedCharacter() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/~foo/");
        URIAssert.equals(URI.parse("http://example.com/%7Efoo/").toASCII(), uri.toASCII());
        URIAssert.equals(URI.parse("http://example.com/%7efoo/").toASCII(), uri.toASCII());
    }
    
    //
    // Removal of dot segments, see section 5.2.4 and 5.4.x of RFC 3986
    //
    
    @Test
    public void shouldRemoveLeadingSingleDotSegment() throws URISyntaxException {
        URIAssert.equals("", new URI().withPath("./").path());
        URIAssert.equals("test/foo/bar", new URI().withPath("./test/foo/bar").path());
    }
    
    @Test
    public void shouldRemoveLeadingDoubleDotSegment() throws URISyntaxException {
        URIAssert.equals("", new URI().withPath("../").path());
        URIAssert.equals("bar/temp.html", new URI().withPath("../bar/temp.html").path());
    }
    
    @Test
    public void shouldRemoveLeadingPathSeparatorAndDot() throws URISyntaxException {
        URIAssert.equals("/test/index.html", new URI().withPath("/./test/index.html").path());
        URIAssert.equals("http://abc.com/test/index.html", URI.parse("http://abc.com/./test/index.html").toASCII());
    }
    
    @Test
    public void shouldRemoveMixedDotSegmentsDotSegmentsWithMixedDotSegments() throws URISyntaxException {
        URIAssert.equals("foo/bar.html", new URI().withPath("../foo/temp/../bar.html").path());
        URIAssert.equals("/bar.html", new URI().withPath("./test/.././bar.html").path());
    }
    
    @Test
    public void shouldRemovePathSeparatorAndCompleteDotPath() throws URISyntaxException {
        URIAssert.equals("/", new URI().withPath("/.").path());
    }
    
    @Test
    public void shouldRemoveSingleDotSegmentOnlyPath() throws URISyntaxException {
        URIAssert.equals("", new URI().withPath(".").path());
    }
    
    @Test
    public void shouldRemoveDoubleDotSegmentOnlyPath() throws URISyntaxException {
        URIAssert.equals("", new URI().withPath("..").path());
    }
    
    @Test
    public void shouldRemovePathSeparatorAndCompleteDoubleDotPath() throws URISyntaxException {
        URIAssert.equals("", new URI().withPath("/..").path());
        URIAssert.equals("/bar", new URI().withPath("/../bar").path());
    }
    
    //
    // Example taken from section 5.2.4 of RFC 3986
    //
    
    @Test
    public void shouldResolvePathWithMixedDotSegments() throws URISyntaxException {
        URIAssert.equals("/a/g", new URI().withPath("/a/b/c/./../../g").path());
        URIAssert.equals("mid/6", new URI().withPath("mid/content=5/../6").path());
    }
    
    @Test
    public void shouldRemovePathSeparatorAndDoubleDotSegmentFromPath() throws URISyntaxException {
        URIAssert.equals("/test", new URI().withPath("/test/foo/..").path());
        URIAssert.equals("/test", new URI().withPath("/test/foo/bar/../..").path());
        URIAssert.equals("/test/", new URI().withPath("/foo/../test/bar/../").path());
    }
    
    @Test
    public void shouldParseURIWithLeadingDoubleDotSegments() throws URISyntaxException {
        URIAssert.equals("http://a.c/test", URI.parse("http://a.c/../test").toASCII());
        URIAssert.equals("http://a.c/test", URI.parse("http://a.c/../../test").toASCII());
        URIAssert.equals("http://a.c/foo/test", URI.parse("http://a.c/../../../foo/test").toASCII());
    }
    
    //
    // Relative path related, see section 5.2 of RFC 3986
    //
    
    @Test
    public void shouldJoinEmptyStringWithURI() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/path");
        URIAssert.equals("http://example.com/path", uri.join("").toASCII());
    }
    
    @Test
    public void shouldJoinWithRelativePathString() throws URISyntaxException {
        URI uri = URI.parse("http://example.com");
        URIAssert.equals("http://example.com/relative/path", uri.join("relative/path").toASCII());
    }
    
    @Test
    public void shouldJoinURIWithRelativePath() throws URISyntaxException {
        URI uri = URI.parse("http://example.com");
        URI relativeURI = URI.parse("relative/path");
        URIAssert.equals("http://example.com/relative/path", uri.join(relativeURI).toASCII());
    }
    
    @Test
    public void shouldJoinRelativePathWithAnotherOne() throws URISyntaxException {
        URI uri = URI.parse("relative/path/to");
        URIAssert.equals("relative/path/another/relative/path", uri.join("another/relative/path").toASCII());
    }
    
    @Test
    public void shouldJoinRelativePathWithAnotherRelativeURI() throws URISyntaxException {
        URI uri = URI.parse("relative/path/to");
        URI relativeURI = URI.parse("another/relative/path");
        URIAssert.equals("relative/path/another/relative/path", uri.join(relativeURI).toASCII());
    }
    
    @Test
    public void shouldJoinPathWithoutSlashesWithAnotherPath() throws URISyntaxException {
        URI uri = URI.parse("path_with_no_slashes");
        URIAssert.equals("foo_bar_path", uri.join("foo_bar_path").toASCII());
    }
    
    @Test
    public void shouldJoinPathWithTrailingSlashWithAnotherPath() throws URISyntaxException {
        URI uri = URI.parse("path_with_trailing_slash/");
        URI relativeURI = URI.parse("another_path");
        URIAssert.equals("path_with_trailing_slash/another_path", uri.join(relativeURI).toASCII());
    }
    
}


