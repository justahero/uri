package com.uri;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String Unreserved     = "a-zA-Z0-9-._~";
    private final static String SubDelimiters  = "!$&'()*+,;=";
    private final static String PercentEncoded = "%[0-9A-F]{2}"; 
    
    private final static String RegExScheme = "^([a-zA-z]+[a-zA-z+-.]*):";
    
    private final static String RegExUserInfo = "(?:((?:["+Unreserved+"!$&'()*+,;=:]|"+PercentEncoded+")*)@)?";
    private final static String RegExHost     = "((?:["+Unreserved+SubDelimiters+"]|"+PercentEncoded+")*)";
    
    // ((?:[a-z0-9-._~!$&'()*+,;=]|%[0-9A-F]{2})*)
    private final static String RegExAuthority = 
            "//" +
            RegExUserInfo +
            RegExHost +
            "";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    
    private String  scheme    = "";
    private String  userinfo  = "";
    private String  hostname  = "";
    private boolean isAuthory = true;
    
    private StringBuilder remaining = new StringBuilder();
    
    static {
        SchemePattern = Pattern.compile(RegExScheme);
        AuthorityPattern = Pattern.compile(RegExAuthority);
    }
    
    public URI(String url) throws URISyntaxException {
        remaining.append(url);
        parseScheme(remaining);
        parseAuthority(remaining);
    }
    
    public String authority() {
        StringBuilder sb = new StringBuilder();
        sb.append(isAuthory ? "//" : "");
        sb.append(userinfo);
        return sb.toString();
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    public String userinfo() {
        return this.userinfo;
    }
    
    public String username() {
        String[] parts = userinfo.split(":");
        return parts[0];
    }
    
    public String userpass() {
        String[] parts = userinfo.split(":");
        return (parts.length > 1) ? parts[1] : "";
    }
    
    private void parseScheme(StringBuilder url) throws URISyntaxException {
        Matcher matcher = SchemePattern.matcher(url);
        if (matcher.find()) {
            this.scheme = matcher.group(1);
            url.delete(matcher.start(0), matcher.end(0));
        } else {
            throw new URISyntaxException(url.toString(), "No valid scheme");
        }
    }
    
    private void parseAuthority(StringBuilder url) throws URISyntaxException {
        System.out.println("parseAuthority: " + url);
        Matcher matcher = AuthorityPattern.matcher(url);
        if (matcher.find()) {
            this.userinfo = matcher.group(1);
            this.hostname = matcher.group(2);
            System.out.println("username: " + matcher.group(1));
            System.out.println("hostname: " + matcher.group(2));
            url.delete(matcher.start(0), matcher.end(0) - 1);
        } else {
            throw new URISyntaxException(url.toString(), "No valid authority");
        }
    }
}


