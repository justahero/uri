package com.uri;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
    private final static String Unreserved = "a-zA-Z0-9-._~";
    
    private final static String SubDelimiters  = "!$&'()*+,;=";
    private final static String PercentEncoded = "%[0-9a-fA-F]{2}"; 
    
    private final static String RegExScheme    = "^([a-zA-z]+[a-zA-z+-.]*):";
    private final static String RegExUserInfo  = "(["+Unreserved+SubDelimiters+":]|"+PercentEncoded+")+";
    private final static String RegExHost      = "(?:(["+Unreserved+SubDelimiters+"]|"+PercentEncoded+")*)?";
    private final static String RegExPort      = "([0-9]{1,5})";
    
    private final static String RegExAuthority =
          "//(?:(.*)@)?(?:([^:/#\\?]+))(?::(.*))?";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern HostPattern;
    private final static Pattern PortPattern;
    
    private final static Pattern PercentEncodingPattern;
    
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
        PortPattern      = Pattern.compile(RegExPort);
        PercentEncodingPattern = Pattern.compile("(?:%([a-fA-F0-9]{2})*)");
    }
    
    public URI(String url) throws URISyntaxException {
        System.out.println("Parsing url: " + url);
        remaining.append(removePercentEncodedCharacters(url.toLowerCase()));
        parseScheme(remaining);
        parseAuthority(remaining);
    }
    
    private String removePercentEncodedCharacters(String url) {
        StringBuilder result = new StringBuilder(url);
        Matcher matcher = PercentEncodingPattern.matcher(result);
        int index = 0;
        while (matcher.find(index++)) {
            System.out.println("  count: " + matcher.groupCount());
            String hexValue = matcher.group(1);
            String replaced = getPercentEncodedChar(hexValue);
            result.replace(matcher.start(), matcher.end(), replaced);
            matcher.reset();
        }
        System.out.println("  result: " + result);
        return result.toString();
    }
    
    private String getPercentEncodedChar(String hexValue) {
        int value = Integer.parseInt(hexValue, 16);
        int index = 0;
        if ((index = UnreservedChars.indexOf(value)) != -1) {
            return ("" + UnreservedChars.charAt(index)).toLowerCase();
        }
        return "%" + hexValue.toUpperCase();
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
        if (port == null)
            return;
        
        Matcher matcher = PortPattern.matcher(port);
        if (!matcher.matches()) {
            throw new URISyntaxException(port, "Invalid port");
        }
        int portNumber = Integer.valueOf(port);
        if (portNumber < 1 || portNumber > 65535) {
            throw new URISyntaxException(port, "Invalid port number");
        }
        this.port = port;
    }
}

