package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URIEqualsTest {
    
    //
    // Tests related to URI.equals method and equivalence of URIs
    //
    
    @Test
    public void shouldEqualEmptyURIWithEmptyString() throws URISyntaxException {
        Assert.assertTrue(URI.parse("").equals(""));
    }
    
    @Test
    public void shouldNotEqualURIWithNullObject() throws URISyntaxException {
        Assert.assertFalse(URI.parse("http://example.com").equals(null));
    }
    
    @Test
    public void shouldNotEqualEmptyURIWithDifferentObject() throws URISyntaxException {
        Assert.assertFalse(URI.parse("").equals(new Integer(1)));
    }
    
    @Test
    public void shouldEqualIdenticalURI() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com/path?query=true#fragment");
        Assert.assertTrue(uri.equals(uri));
    }
    
    @Test
    public void shouldEqualURIWithSameString() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com/path?query=true#fragment");
        Assert.assertTrue(uri.equals("http://www.example.com/path?query=true#fragment"));
    }
    
}


