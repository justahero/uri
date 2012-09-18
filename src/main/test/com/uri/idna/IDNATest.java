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
    
    @Test
    public void shouldParseURIWithInternationalizedHostName() throws URISyntaxException {
        URI uri = URI.parse("http://www.Iñtërnâtiônàlizætiøn.com");
        URIAssert.equals("http://www.xn--itrntinliztin-vdb0a5exd8ewcye.com", uri.toASCII());
    }
    
    @Test
    public void shouldParseURIWithInternationalizedLongJapaneseHost() throws URISyntaxException {
        URI uri = URI.parse("http://www.ほんとうにながいわけのわからないどめいんめいのらべるまだながくしないとたりない.w3.mag.keio.ac.jp");
        String expected = "http://www.xn--n8jaaaaai5bhf7as8fsfk3jnknefdde3fg11amb5gzdb4wi9bya3kc6lra.w3.mag.keio.ac.jp";
        URIAssert.equals(expected, uri.toASCII());
    }
    
    @Test
    public void shouldParseURIExamples() throws URISyntaxException {
        URIAssert.equals("xn--0trv4xfvn8el34t.w3.mag.keio.ac.jp", new URI().withHost("点心和烤鸭.w3.mag.keio.ac.jp").host());
        URIAssert.equals("xn--o39acdefghijk5883jma.com", new URI().withHost("가각갂갃간갅갆갇갈갉힢힣.com").host());
        URIAssert.equals("xn--eek174hoxfpr4k.com", new URI().withHost("ﾘ宠퐱〹.com").host());
        URIAssert.equals("xn--4ud", new URI().withHost("ᆵ").host());
        URIAssert.equals("xn--4ud", new URI().withHost("ﾯ").host());
    }
    
    @Test
    public void shouldConvertURIFromASCIIToUnicode() throws URISyntaxException {
        URIAssert.equals("http://www.google.com", SimpleIDN.toUnicode(URI.parse("http://www.google.com").toASCII()));
    }
/*    
    @Test
    public void shouldConvertToUnicodeURISamples() throws URISyntaxException {
        URIAssert.equals(
            "www.iñtërnâtiônàlizætiøn.com",
            SimpleIDN.toUnicode("www.xn--itrntinliztin-vdb0a5exd8ewcye.com"));
    }
    
    @Test
    public void shouldConvertURIWithAsianHostname() throws URISyntaxException {
        URIAssert.equals("www.詹姆斯.com", SimpleIDN.toUnicode(new URI().withHost("www.xn--8ws00zhy3a.com").host()));
    }
    */
    @Test
    public void shouldParseUnicodeURIAndApplyToASCIITwice() throws URISyntaxException {
        URI uri = URI.parse("http://www.詹姆斯.com");
        URI other = URI.parse(uri.toASCII());
        URIAssert.equals("http://www.xn--8ws00zhy3a.com", other.toASCII());
    }
}


