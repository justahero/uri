package com.uri.idna;

import org.junit.Test;

import com.uri.URIAssert;
import com.uri.idn.Punycode;
import com.uri.idn.PunycodeException;

public class PunycodeTest {
    
    //
    // See section 7.1 of RFC 3942 (http://www.ietf.org/rfc/rfc3492.txt) for language specific examples
    //
    
    @Test
    public void shouldEncodeArabicEgyptianString() throws PunycodeException {
        String label =
            "\u0644\u064A\u0647\u0645\u0627\u0628\u062A\u0643\u0644" +
            "\u0645\u0648\u0634\u0639\u0631\u0628\u064A\u061F";
        
        String expected = "egbpdaj6bu4bxfgehfvwxn";
        URIAssert.equals(expected, Punycode.encode(label));
    }
    
    @Test
    public void shouldEncodeSimplifiedChineseString() throws PunycodeException {
        String label = "\u4ED6\u4EEC\u4E3A\u4EC0\u4E48\u4E0D\u8BF4\u4E2D\u6587";
        String expected = "ihqwcrb4cv8a8dqg056pqjye";
        URIAssert.equals(expected, Punycode.encode(label));
    }
    
    @Test
    public void shouldEncodeRussianCyrillicString() throws PunycodeException {
        String label =
            "\u043F\u043E\u0447\u0435\u043C\u0443\u0436\u0435\u043E" +
            "\u043D\u0438\u043D\u0435\u0433\u043E\u0432\u043E\u0440" +
            "\u044F\u0442\u043F\u043E\u0440\u0443\u0441\u0441\u043A" +
            "\u0438";
        String expected = "b1abfaaepdrnnbgefbadotcwatmq2g4l";
        URIAssert.equals(expected, Punycode.encode(label));
    }
    
    @Test
    public void shouldEncodeJapaneseSamples() throws PunycodeException {
        URIAssert.equals("d9juau41awczczp", Punycode.encode("\u305D\u306E\u30B9\u30D4\u30FC\u30C9\u3067"));
    }
    
    // TODO many more examples!
}
