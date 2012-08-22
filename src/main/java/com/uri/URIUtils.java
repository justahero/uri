package com.uri;

import java.util.Iterator;
import java.util.Stack;
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
            if (input.startsWith("../")) {
                input = input.substring(3);
            } else if (input.startsWith("./")) {
                input = input.substring(2);
            } else if (input.startsWith("/./")) {
                input = input.substring(2);
            } else if (input.equals("/.")) {
                input = "/";
            } else if (input.equals(".") || input.equals("..")) {
                input = "";
            } else if (input.startsWith("/..")) {
                input = input.substring(3);
                if (!output.isEmpty()) {
                    output.pop();
                }
            } else {
                int index = (input.startsWith("/")) ? 1 : 0;
                int segmentIndex = input.indexOf("/", index);
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

