package com.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIUtils {
    
    final static String  UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
    final static Pattern PercentEncodingPattern = Pattern.compile("(?:%([a-fA-F0-9]{2}))");
    
    public static String removePercentEncodedCharacters(String url) {
        StringBuilder result = new StringBuilder(url);
        Matcher matcher = PercentEncodingPattern.matcher(result);
        int index = 0;
        while (matcher.find(index++)) {
            String hexValue = matcher.group(1);
            String replaced = URIUtils.getPercentEncodedChar(hexValue);
            result.replace(matcher.start(), matcher.end(), replaced);
            matcher.reset();
        }
        return result.toString();
    }
    
    public static String getPercentEncodedChar(String hexValue) {
        int value = Integer.parseInt(hexValue, 16);
        int index = 0;
        if ((index = UnreservedChars.indexOf(value)) != -1) {
            return ("" + UnreservedChars.charAt(index)).toLowerCase();
        }
        return "%" + hexValue.toUpperCase();
    }
    
}

