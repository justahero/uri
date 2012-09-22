package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIQueryTest {
    
    @Test
    public void shouldParesURIWithEmptyQuery() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com?");
        URIAssert.equals("", uri.query());
    }
    
    @Test
    public void shouldParseURIWithSingleKeyValuePair() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/?q=string");
        URIAssert.equals("q=string", uri.query());
        URIAssert.equals("/", uri.path());
    }
    
    @Test
    public void shouldParseURIWithSingleKeyValuePairAndFragment() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/test/resource?query=x#fragment");
        URIAssert.equals("query=x", uri.query());
        URIAssert.equals("fragment", uri.fragment());
    }
    
    @Test
    public void shouldConstructURIAndReassignQueryComponent() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withQuery("test=value");
        uri.withQuery("abc=foobar");
        URIAssert.equals("http://example.com?abc=foobar", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIWithTwoQueryParameters() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/test?temp=value&foo=bar#top");
        URIAssert.equals("temp=value&foo=bar", uri.query());
        URIAssert.equals("top", uri.fragment());
    }
    
    @Test
    public void shouldConstructURIWithComplexQueryParameter() throws URISyntaxException {
        URI uri = new URI().withQuery("one[two][three]=four");
        URIAssert.equals("one%5Btwo%5D%5Bthree%5D=four", uri.query());
    }
    
    @Test
    public void shouldParseURIWithComplexQueryParameter() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/path?one[two][three]=four#fragment");
        URIAssert.equals("one%5Btwo%5D%5Bthree%5D=four", uri.query());
    }
    
    @Test
    public void shouldParseURIAndSortWithoutQueryParameter() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/foo#bar").sortQuery();
        URIAssert.equals("http://example.com/foo#bar", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIAndSortSingleQueryParameter() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/foo?test=value#bottom").sortQuery();
        URIAssert.equals("test=value", uri.query());
        URIAssert.equals("http://example.com/foo?test=value#bottom", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIAndSortQueryParameters() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/bar?value=12&temp=true").sortQuery();
        URIAssert.equals("temp=true&value=12", uri.query());
        URIAssert.equals("http://example.com/bar?temp=true&value=12", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIAndSortQueryParametersWithSameKey() throws URISyntaxException {
        URI uri = URI.parse("http://example.com/foo/path/a?abc=def&abc=cba#def").sortQuery();
        URIAssert.equals("abc=cba&abc=def", uri.query());
        URIAssert.equals("http://example.com/foo/path/a?abc=cba&abc=def#def", uri.toASCII());
    }
    
    @Test
    public void shouldConstructURIAndSortAddedQueryParameters() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").withQuery("auth=2");
        uri.addParam("bcd", "22");
        uri.addParam("abc", "http");
        uri.sortQuery();
        URIAssert.equals("abc=http&auth=2&bcd=22", uri.query());
    }
    
    @Test
    public void shouldParseURIAndSortHMacAuthenticationQuery() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com/foo/bar?auth[time]=4444&auth[signature]=12345678&terminal=true");
        uri.sortQuery();
        URIAssert.equals("auth%5Bsignature%5D=12345678&auth%5Btime%5D=4444&terminal=true", uri.query());
        URIAssert.equals("http://www.example.com/foo/bar?auth%5Bsignature%5D=12345678&auth%5Btime%5D=4444&terminal=true", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIWithDifferentQuerySeparator() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com/path?auth=3333;test=foo");
        uri.queryDelimiter(';');
        Assert.assertEquals(2, uri.queries().size());
    }
    
    @Test
    public void shouldConstructURIWithDifferentQuerySeparator() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com").addParam("foo", "temp").addParam("page", "1");
        uri.queryDelimiter(';');
        URIAssert.equals("foo=temp;page=1", uri.query());
        URIAssert.equals("http://example.com?foo=temp;page=1", uri.toASCII());
    }
    
    /*
    @Test
    public void shouldNormalizeQuerySeparatorInParam() throws URISyntaxException {
        URI uri = URI.parse("http://example.com").addParam("foo", "temp").addParam(";colon", "2");
        uri.queryDelimiter(';');
        URIAssert.equals("foo=temp;;colon=2", uri.query());
    }
    */
}


