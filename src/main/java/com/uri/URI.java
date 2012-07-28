package com.uri;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String Unreserved     = "a-zA-Z0-9-._~";
    private final static String SubDelimiters  = "!$&'()*+,;=";
    private final static String PercentEncoded = "%[0-9A-F]{2}"; 
    
    private final static String RegExScheme    = "^([a-zA-z]+[a-zA-z+-.]*):";
    private final static String RegExUserInfo  = "(["+Unreserved+SubDelimiters+":]|"+PercentEncoded+")+";
    private final static String RegExHost      = "(?:(["+Unreserved+SubDelimiters+"]|"+PercentEncoded+")*)?";
    private final static String RegExPort      = "([0-9]{1,5})";
    
    private final static String RegExAuthority =
          //"//(?:(.*)@)?(?:(.*))(?::([0-9]+))?";
          "//(?:(.*)@)?(?:([a-zA-Z0-9-._~]*))(?::([0-9]+))?";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern HostPattern;
    
    private String scheme    = "";
    private String username  = "";
    private String userpass  = "";
    private String host      = "";
    private String port      = "";
    
    private StringBuilder remaining = new StringBuilder();
    
    static {
        SchemePattern    = Pattern.compile(RegExScheme);
        AuthorityPattern = Pattern.compile(RegExAuthority);
        UserInfoPattern  = Pattern.compile(RegExUserInfo);
        HostPattern      = Pattern.compile(RegExHost);
    }
    
    public URI(String url) throws URISyntaxException {
        System.out.println("Parsing url: " + url);
        remaining.append(url);
        parseScheme(remaining);
        parseAuthority(remaining);
    }
    
    public String scheme() {
        return scheme;
    }
    
    public String username() {
        return username;
    }
    
    public String userpass() {
        return userpass;
    }
    
    public String host() {
        return host;
    }
    
    public String port() {
        return port;
    }
    
    private void parseScheme(StringBuilder url) throws URISyntaxException {
        Matcher matcher = SchemePattern.matcher(url);
        if (matcher.find()) {
            scheme = matcher.group(1);
            url.delete(matcher.start(0), matcher.end(0));
        } else {
            throw new URISyntaxException(url.toString(), "No valid scheme");
        }
    }
    
    private void parseAuthority(StringBuilder url) throws URISyntaxException {
        Matcher matcher = AuthorityPattern.matcher(url);
        System.out.println("parse authority: " + url);
        if (matcher.find()) {
            System.out.println("  userinfo: " + matcher.group(1));
            System.out.println("  host: " + matcher.group(2));
            System.out.println("  port: " + matcher.group(3));
            
            parseUserInfo(matcher.group(1));
            parseHost(matcher.group(2));
            parsePort(matcher.group(3));
        }
    }
    
    private void parseUserInfo(String userInfo) throws URISyntaxException {
        if (userInfo != null) {
            if (userInfo.isEmpty()) {
                throw new URISyntaxException(userInfo, "User info must be specified if '@' is present");
            }
            String[] parts = userInfo.split(":");
            if (parts.length > 2 || parts[0].isEmpty() || !isUserInfoValid(userInfo)) {
                throw new URISyntaxException(userInfo, "User info is not valid");
            }
            username = parts[0];
            userpass = (parts.length > 1) ? parts[1] : "";
        }
    }
    
    private boolean isUserInfoValid(String userInfo) {
        Matcher matcher = UserInfoPattern.matcher(userInfo);
        return matcher.matches();
    }
    
    private void parseHost(String hostPart) throws URISyntaxException {
        if (hostPart == null) {
            throw new URISyntaxException(hostPart, "Host must be given!");
        }
        Matcher matcher = HostPattern.matcher(hostPart);
        if (!matcher.matches()) {
            throw new URISyntaxException(hostPart, "Host is not valid");
        }
        host = hostPart;
    }
    
    private void parsePort(String port) throws URISyntaxException {
        if (port != null) {
            this.port = port;
        }
    }
}

