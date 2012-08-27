package com.uri;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Pattern;

public class URIUtils {
    
    final static String  ALPHA      = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String  DIGIT      = "0123456789";
    final static String  HEXDIGIT   = "abcdefABCDEF" + DIGIT;
    final static String  GENDELIMS  = ":/?#[]@";
    final static String  SUBDELIMS  = "!$&'()*+;=";
    
    final static String  UNRESERVED = ALPHA + DIGIT + "-._~";
    final static String  RESERVED   = GENDELIMS + SUBDELIMS;
    final static String  PERCENT    = "%";
    final static String  PCHAR      = UNRESERVED + SUBDELIMS + PERCENT;
    
    final static String  QUERY      = PCHAR + "/" + "?";
    final static String  REGNAME    = UNRESERVED + PERCENT + SUBDELIMS;
    
    final static Pattern PercentEncodingPattern = Pattern.compile("(?:%([0-9a-fA-F]{2}))");
    
    public static String normalize(String query, String charList) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < query.length(); i++) {
            result.append(getPercentEncodedChar(query.charAt(i), charList));
        }
        return result.toString();
    }

    public static String normalizeString(String text, boolean ignoreCase) throws URISyntaxException {
        StringBuffer result = new StringBuffer(ignoreCase ? text : text.toLowerCase());
        try {
            int index = 0;
            int found = 0;
            while ((found = result.indexOf("%", index)) != -1) {
                if (found < result.length() - 2) {
                    String hexString = result.substring(found + 1, found + 3);
                    String replaced = URIUtils.getPercentEncodedChar(hexString, ignoreCase);
                    result.replace(found, found + 3, replaced);
                }
                index++;
            }
        } catch (Exception e) {
            throw new URISyntaxException(text, "Failed to normalize string");
        }
        return result.toString();
    }
    
    private static String getPercentEncodedChar(String hexValue, boolean ignoreCase) {
        int value = Integer.parseInt(hexValue, 16);
        int index = 0;
        if ((index = UNRESERVED.indexOf(value)) != -1) {
            String c = "" + UNRESERVED.charAt(index);
            return (ignoreCase) ? c : c.toLowerCase();
        }
        return "%" + hexValue.toUpperCase();
    }
    
    public static String getPercentEncodedChar(char c, final String charList) {
        int index = charList.indexOf(c);
        if (index == -1) {
            int value = (int)c;
            return ("%" + (value < 16 ? "0" : "") + Integer.toHexString(value)).toUpperCase();
        }
        return String.valueOf(c);
    }
    
    /**
     * TODO refactor this algorithm, it's a bit chunky and not so readable
     * 
     * @param path
     * @return
     */
    public static String removeDotSegments(String path) {
        Stack<String> output = new Stack<String>();
        String input = new String(path);
        
        while (!input.isEmpty()) {
            if (input.startsWith("../") || input.startsWith("./")) {
                input = input.substring(input.indexOf("/") + 1);
            } else if (input.startsWith("/..")) {
                input = input.substring(3);
                if (!output.isEmpty()) {
                    output.pop();
                }
            } else if (input.equals("/.")) {
                input = "/";
            } else if (input.startsWith("/.")) {
                input = input.substring(2);
            } else if (input.equals(".") || input.equals("..")) {
                input = "";
            } else {
                int segmentIndex = input.indexOf("/", 1);
                if (segmentIndex == -1) {
                    segmentIndex = input.length();
                }
                
                String pathSegment = input.substring(0, segmentIndex);
                input = input.substring(segmentIndex);
                if (!pathSegment.isEmpty()) {
                    output.push(pathSegment);
                }
            }
        }
        
        return join(output);
    }
    
    
    public static String join(final Iterable<String> container) {
        return join(container, "");
    }
    
    /**
     * Joins a collection of strings to a single string, all parts are merged with a delimiter string.
     * 
     * @param container
     * @param delimiter
     * @return
     */
    public static String join(final Iterable<String> container, final String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = container.iterator();
        while (it.hasNext()) {
            builder.append(it.next());
            if (it.hasNext())
                builder.append(delimiter);
        }
        return builder.toString();
    }
}

