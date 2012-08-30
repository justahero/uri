package com.uri.idna;

import java.net.IDN;
import java.net.URISyntaxException;

import org.junit.Test;

import com.uri.URI;
import com.uri.URIAssert;
import com.uri.idn.SimpleIDN;

public class IDNATest {
    
    @Test
    public void toASCIIShouldNotAlterEmptyString() throws URISyntaxException {
        URIAssert.equals("", SimpleIDN.toASCII(""));
    }
    
    @Test
    public void toASCIIShouldNotAlterASCIILabel() throws URISyntaxException {
        URIAssert.equals("test", SimpleIDN.toASCII("test"));
    }
    
    @Test
    public void toUnicodeShouldNotAlterEmptyString() {
        URIAssert.equals("", SimpleIDN.toUnicode(""));
    }
    
    @Test
    public void toASCIIShouldTransformExampleLabel() {
        URIAssert.equals("xn--mllerriis-l8a.com", IDN.toASCII("møllerriis.com"));
    }
    
    @Test
    public void toUnicodeShouldTransformExampleLabel() {
        URIAssert.equals("møllerriis.com", IDN.toUnicode("xn--mllerriis-l8a.com"));
    }
    
    @Test
    public void shouldParseURIWithLettersOutsideSubsetInHostname() throws URISyntaxException {
        URI uri = URI.parse("http://www.äöü.com");
        URIAssert.equals("http://www.xn--4ca0bs.com", uri.toASCII());
    }
    
    @Test
    public void shouldConvertAsianHostNameToUnicodeName() throws URISyntaxException {
        URI uri = URI.parse("http://www.詹姆斯.com");
        URIAssert.equals("http://www.xn--8ws00zhy3a.com", uri.toASCII());
    }
}

