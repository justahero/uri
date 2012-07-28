package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URISchemeTest {

    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotPlus() throws URISyntaxException {
        new URI("+http://example.com");
    }

    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotHyphen() throws URISyntaxException {
        new URI("-http://example.com");
    }

    @Test(expected=URISyntaxException.class)
    public void schemeMustStartWithLetterAndNotDot() throws URISyntaxException {
        new URI(".ftp://example.com");
    }

    @Test(expected=URISyntaxException.class)
    public void schemeMustNotContainOtherOctets() throws URISyntaxException {
        new URI("ft9p://example.com");
    }

    @Test
    public void schemeMustStartWithLetter() throws URISyntaxException {
        URI uri = new URI("http://example.com");
        Assert.assertEquals("http", uri.scheme());
    }

    @Test
    public void schemeAllowsDotHyphenAndPlus() throws URISyntaxException {
        URI uri = new URI("t.a+b-c://test.com");
        Assert.assertEquals("t.a+b-c", uri.scheme());
    }

}
