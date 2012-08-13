package com.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIUtils {
    
    final static String  UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
    final static Pattern PercentEncodingPattern = Pattern.compile("(?:%([a-fA-F0-9]{2}))");
    
    public static String normalizeString(String text, boolean ignoreCase) {
        StringBuilder result = new StringBuilder(ignoreCase ? text : text.toLowerCase());
        Matcher matcher = PercentEncodingPattern.matcher(result);
        int index = 0;
        while (matcher.find(index++)) {
            String hexString = matcher.group(1);
            String replaced = URIUtils.getPercentEncodedChar(hexString, ignoreCase);
            result.replace(matcher.start(), matcher.end(), replaced);
            matcher.reset();
        }
        return result.toString();
    }
    
    public static String getPercentEncodedChar(String hexValue, boolean ignoreCase) {
        int value = Integer.parseInt(hexValue, 16);
        int index = 0;
        if ((index = UnreservedChars.indexOf(value)) != -1) {
            String c = "" + UnreservedChars.charAt(index);
            return (ignoreCase) ? c : c.toLowerCase();
        }
        return "%" + hexValue.toUpperCase();
    }
}

