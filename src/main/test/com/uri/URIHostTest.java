package com.uri;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class URIHostTest {
    
    @Test
    public void namedHostWithoutUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com");
        URIAssert.equals("www.example.com", uri.host());
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseNamedHostWithSquareBrackets() throws URISyntaxException {
        URI.parse("http://www.foo[bar].com");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseNamedHostWithComparisonOperators() throws URISyntaxException {
        URI.parse("http://www.<test>.com");
    }
    
    @Test
    public void namedHostWithInvalidFormat() throws URISyntaxException {
        URIAssert.exception("http://<test>");
    }
    
    @Test
    public void namedHostWithUpperCaseLetters() throws URISyntaxException {
        URIAssert.equals("www.example.com", URI.parse("http://www.EXAMPLE.cOm").host());
    }
    
    @Test
    public void namedHostWithPortNumber() throws URISyntaxException {
        Assert.assertEquals(80, URI.parse("http://www.test.com:80").port());
    }
    
    @Test
    public void ipV4HostWithoutUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://127.0.0.1");
        Assert.assertEquals("127.0.0.1", uri.host());
    }
    
    @Test
    public void ipV4HostWithUserInfo() throws URISyntaxException {
        URI uri = URI.parse("http://foo:bar@33.33.33.10");
        URIAssert.equals("33.33.33.10", uri.host());
    }
    
    @Test
    public void ipV4HostWithPathSeparator() throws URISyntaxException {
        URIAssert.equals("127.0.0.1", URI.parse("http://127.0.0.1/").host());
        URIAssert.equals("http://127.0.0.1", URI.parse("http://127.0.0.1/").toASCII());
    }

    @Test
    public void portSeparatorOnlyIsOmitted() throws URISyntaxException {
        URI uri = URI.parse("http://www.example.com:");
        URIAssert.equals(URI.parse("http://www.example.com").toASCII(), uri.toASCII());
    }

    @Test(expected=URISyntaxException.class)
    public void portContainsInvalidCharacter() throws URISyntaxException {
        URI.parse("http://www.example.com:9a");
    }
    
    @Test
    public void portNumberIsTooLong() throws URISyntaxException {
        URIAssert.exception("http://www.test.de:123456");
        URIAssert.exception("http://www.test.de:0");
        URIAssert.exception("http://www.test.de:-10");
    }
    
    @Test
    public void ipV6HostUnicast() throws URISyntaxException {
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", URI.parse("http://[1080:0:0:0:8:800:200C:417A]").host());
    }
    
    @Test
    public void ipV6HostWithIpV4Part() throws URISyntaxException {
        URIAssert.equals("[0:0:0:0:0:0:13.1.68.3]", URI.parse("http://[0:0:0:0:0:0:13.1.68.3]/").host());
        URIAssert.equals("[0:0:0:0:0:FFFF:129.144.52.38]", URI.parse("http://[0:0:0:0:0:FFFF:129.144.52.38]").host());
    }
    
    @Test
    public void ipV6HostWithPort() throws URISyntaxException {
        URI uri = URI.parse("http://[1080:0:0:0:8:800:200C:417A]:4040");
        Assert.assertEquals(4040, uri.port());
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", uri.host());
    }
    
    @Test
    public void ipV6HostWithPortAndPathSeparator() throws URISyntaxException {
        URI uri = URI.parse("http://[1080:0:0:0:8:800:200C:417A]:4040/test");
        Assert.assertEquals(4040, uri.port());
        URIAssert.equals("[1080:0:0:0:8:800:200C:417A]", uri.host());
    }
    
    @Test
    public void shouldParseCompleteIPV6URI() throws URISyntaxException {
        URI uri = URI.parse("http://[3ffe:1900:4545:3:200:f8ff:fe21:67cf]/");
        URIAssert.equals("http://[3ffe:1900:4545:3:200:f8ff:fe21:67cf]", uri.toASCII());
        URIAssert.equals("[3ffe:1900:4545:3:200:f8ff:fe21:67cf]", uri.host());
    }
    
    @Test
    public void shouldParseSeveralDifferentIPV6URIs() throws URISyntaxException {
        URI.parse("http://[fe80:0:0:0:200:f8ff:fe21:67cf]/");
        URI.parse("http://[fe80::200:f8ff:fe21:67cf]/");
        URI.parse("http://[::1]/");
        URI.parse("http://[fe80::1]/");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseInvalidIPV6URI() throws URISyntaxException {
        URI.parse("http://[<invalid>]");
    }
    
    @Test
    public void shouldParseCompleteIPFutureURIs() throws URISyntaxException {
        URI uri = URI.parse("http://[v9.3ffe:1900:4545:3:200:f8ff:fe21:67cf]/");
        URIAssert.equals("http://[v9.3ffe:1900:4545:3:200:f8ff:fe21:67cf]", uri.toASCII());
        URIAssert.equals("[v9.3ffe:1900:4545:3:200:f8ff:fe21:67cf]", uri.host());
    }
    
    @Test
    public void shouldParseSeveralIPV6FutureURIs() throws URISyntaxException {
        URI.parse("http://[vff.fe80:0:0:0:200:f8ff:fe21:67cf]/");
        URI.parse("http://[v12.fe80::200:f8ff:fe21:67cf]/");
        URI.parse("http://[va0.::1]/");
        URI.parse("http://[v255.fe80::1]/");
    }
    
    @Test(expected=URISyntaxException.class)
    public void shouldNotParseInvalidIPFutureURI() throws URISyntaxException {
        URI.parse("http://[v0.<invalid>]/");
    }
}