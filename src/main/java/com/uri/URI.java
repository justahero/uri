package com.uri;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String  RegExScheme = "^([a-zA-z]+[a-zA-z+-.]*):";
    private final static String  RegExAuthority = "";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    
    private final static char[] RESERVED;
    private final static char[] GEN_DELIMITERS = { ':', '/', '?', '#', '[', ']', '@' };
    private final static char[] SUB_DELIMITERS = { '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=' };
    
    private String scheme    = "";
    
    // authority component
    private String authority = "";
    private String host      = "";
    private String user      = "";
    private String password  = "";
    private int port         = -1;
    
    private String path      = "";
    private String fragment  = "";
    
    static {
        RESERVED = new char[GEN_DELIMITERS.length + SUB_DELIMITERS.length];
        int i = 0;
        for (char c : GEN_DELIMITERS) {
            RESERVED[i++] = c;
        }
        for (char c : SUB_DELIMITERS) {
            RESERVED[i++] = c;
        }
        
        SchemePattern = Pattern.compile(RegExScheme);
        AuthorityPattern = Pattern.compile(RegExAuthority);
    }
    
    public URI(String url) throws URISyntaxException {
        scheme = parseScheme(url);
        parseAuthority(url);
    }
    
    public String host() {
        return this.authority;
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    public String path() {
        return this.path;
    }
    
    public String fragment() {
        return this.fragment;
    }
    
    public int port() {
        return this.port;
    }
    
    private String parseScheme(String url) throws URISyntaxException {
        Matcher matcher = SchemePattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new URISyntaxException(url, "No valid scheme");
    }
    
    public static String parseAuthority(String url) {
        return "";
    }
}


