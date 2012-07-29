package com.uri;

import java.net.URISyntaxException;

import org.junit.Test;

public class URITest {
    
    @Test
    public void hostWithPercentEncodedCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%74%65%73%74.com", "www.test.com");
    }
    
    @Test
    public void hostWithSingleDigitPercentEncodedCharacter() {
        URIAssert.exception("http://www.ex%4.com");
    }
    
    @Test
    public void hostWithLetterInPercentEncodedOctet() {
        URIAssert.exception("http://www.ex%er%01.com");
    }
    
    @Test
    public void hostWithDoublePercentageSigns() {
        URIAssert.exception("http://www.tes%%t.com");
    }
    
    @Test
    public void hostTransformsToPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%66%6f%6f%62%61%72.com", "www.foobar.com");
    }
    
    @Test
    public void transformPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        // => 'DBP'
        URIAssert.host("http://www.%44%42%50.com", "www.dbp.com");
    }
    
    @Test
    public void noTransformOfPercentEncodedSpecialCharacters() throws URISyntaxException {
        URIAssert.host("http://www.Test%7B%7D.com", "www.test%7B%7D.com");
    }
}
