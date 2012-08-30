package com.uri.idn;

import java.net.IDN;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uri.URIUtils;

public class SimpleIDN {
    
    public static final String ACE_PREFIX = "xn--";
    
    private static final String UTF8_REGEX =
            "\\A(?:" +
            "[\\x09\\x0A\\x0D\\x20-\\x7E]]" +            // ASCII characters
            "|[\\xC2-\\xDF][\\x80-\\xBF]" +              // non-overlong 2-byte
            "|\\xE0[\\xA0-\\xBF][\\x80-\\xBF]" +         // excluding overlongs
            "|[\\xE1-\\xEC\\xEE\\xEF][\\x80-\\xBF]{2}" + // straight 3-byte
            "|\\xED[\\x80-\\x9F][\\x80-\\xBF]" +         // excluding surrogates
            "|\\xF0[\\x90-\\xBF][\\x80-\\xBF]{2}" +      // planes 1-3
            "|[\\xF1-\\xF3][\\x80-\\xBF]{3}" +           // planes 4-5
            "|\\xF4[\\x80-\\x8F][\\x80-\\xBF]{2}" +      // plane 16
            ")*\\Z/mnx";
    
    private static final String UTF8_REGEX_MULTIBYTE =
            "\\A(?:" +
            "[\\xC2-\\xDF][\\x80-\\xBF]" +               // non-overlong 2-byte
            "|\\xE0[\\xA0-\\xBF][\\x80-\\xBF]" +         // excluding overlongs
            "|[\\xE1-\\xEC\\xEE\\xEF][\\x80-\\xBF]{2}" + // straight 3-byte
            "|\\xED[\\x80-\\x9F][\\x80-\\xBF]" +         // excluding surrogates
            "|\\xF0[\\x90-\\xBF][\\x80-\\xBF]{2}" +      // planes 1-3
            "|[\\xF1-\\xF3][\\x80-\\xBF]{3}" +           // planes 4-5
            "|\\xF4[\\x80-\\x8F][\\x80-\\xBF]{2}" +      // plane 16
            ")\\Z/mnx";
    
    private static final String DOTS_REGEX = "(?:[\\x2E]|[\\x3002]|[\\xFF0E]|[\\xFF61])";
    
    private static final Pattern UTF8_PATTERN       = Pattern.compile(UTF8_REGEX);
    private static final Pattern UTF8_MULTI_PATTERN = Pattern.compile(UTF8_REGEX_MULTIBYTE);
    
    public static String toASCII(final String label) {
        if (isUTF8Label(label)) {
            String[] parts = splitParts(downcase(label));
            Vector<String> result = new Vector<String>();
            for (String part : parts) {
                if (isUTF8Label(part)) {
                    result.add(IDN.toASCII(part));
                } else {
                    result.add(part);
                }
            }
            return URIUtils.join(result, ".");
        }
        return label;
    }
    
    public static String toUnicode(final String label) {
        try {
            String[] parts = splitParts(label);
            Vector<String> result = new Vector<String>();
            for (String part : parts) {
                if (part.startsWith(ACE_PREFIX)) {
                    result.add(IDN.toUnicode(part));
                } else {
                    result.add(part);
                }
            }
            return URIUtils.join(result, ".");
        } catch (Exception e) {
            return label;
        }
    }
    
    public static String[] splitParts(String input) {
        return input.split(DOTS_REGEX);
    }
    
    private static boolean isUTF8Label(String label) {
        Matcher utf8Matcher = UTF8_PATTERN.matcher(label);
        Matcher utf8MultiMatcher = UTF8_MULTI_PATTERN.matcher(label);
        return (utf8Matcher.find() && utf8MultiMatcher.find());
    }
    
    private static String downcase(String input) {
        return input.toLowerCase();
    }
}


