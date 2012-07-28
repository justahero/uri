package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URITest {
    
    @Test
    public void transformPercentEncodedCharactersLiterals() throws URISyntaxException {
        URI uri = new URI("http://www.%74%65%73%74.com");
        Assert.assertEquals("www.test.com", uri.host());
    }
    
    @Test
    public void transformPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URI uri = new URI("http://www.%66%6f%f%62%61%72.com");
        Assert.assertEquals("www.foobar.com", uri.host());
    }
    
    @Test
    public void transformPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        URI uri = new URI("http://www.%44%42%50.com"); // => 'DBP'
        Assert.assertEquals("www.dbp.com", uri.host());
    }
    
    @Test
    public void noTransformOfPercentEncodedSpecialCharacters() throws URISyntaxException {
        URI uri = new URI("http://www.test%7B%7D.com");
        Assert.assertEquals("www.test%7B%7D.com", uri.host());
    }
}
