package com.uri;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String RegExScheme    = "^([a-zA-z]+[a-zA-z+-.]*):";
    private final static String RegExUserInfo  = "([a-zA-Z0-9-._~!$&'()*+,;=:]|%[0-9a-fA-F]{2})+";
    private final static String RegExNamedHost = "(?:([a-zA-Z0-9-._~!$&'()*+,;=]|%[0-9a-fA-F]{2})*)?";
    private final static String RegExPort      = "([0-9]{1,5})";
    
    private final static String RegExAuthority =
          "\\/\\/" +
          "(?:(.*)@)?" +
          "(?:([a-zA-Z0-9-._~%]+)|(?:\\[(.+)\\])|(?:\\[v(.+)\\]))" +
          "(?::([0-9]+))?" +
          "(?:(\\/[a-zA-Z0-9-._~%!$&'()*+,;=:@]*))?";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern HostPattern;
    private final static Pattern PortPattern;
    
    private String scheme    = "";
    private String username  = "";
    private String userpass  = "";
    private String host      = "";
    private String port      = "";
    private String path      = "";
    
    private StringBuilder remaining = new StringBuilder();
    
    static {
        SchemePattern    = Pattern.compile(RegExScheme);
        AuthorityPattern = Pattern.compile(RegExAuthority);
        UserInfoPattern  = Pattern.compile(RegExUserInfo);
        HostPattern      = Pattern.compile(RegExNamedHost);
        PortPattern      = Pattern.compile(RegExPort);
    }
    
    public URI(String url) throws URISyntaxException {
        remaining.append(URIUtils.removePercentEncodedCharacters(url.toLowerCase()));
        parseScheme(remaining);
        parseAuthority(remaining);
        if (remaining.length() > 0) {
            throw new URISyntaxException(url, "Some components could not be parsed!");
        }
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
    
    public String path() {
        return path;
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
        System.out.println("Parsing authorinty: " + url);
        Matcher matcher = AuthorityPattern.matcher(url);
        if (matcher.find()) {
            for (int i = 1; i < matcher.groupCount(); i++) {
                System.out.println("  " + i + ": " + matcher.group(i));
            }
            
            String userInfo  = matcher.group(1);
            String namedHost = matcher.group(2);
            String ipv6Host  = matcher.group(3);
            String ipFuture  = matcher.group(4);
            String port      = matcher.group(5);
            String path      = matcher.group(6);
            
            parseUserInfo(userInfo);
            parseHost(namedHost, ipv6Host, ipFuture);
            parsePort(port);
            parsePath(path);
            
            url.delete(matcher.start(), matcher.end());
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
    
    private void parseHost(String namedHost, String ipV6Host, String ipFuture) throws URISyntaxException {
        if (namedHost != null) {
            host = parseNamedHost(namedHost);
        } else if (ipV6Host != null) {
            host = parseIpV6Host(ipV6Host);
        } else {
            throw new URISyntaxException("", "No valid host found");
        }
    }
    
    private String parseNamedHost(String namedHost) throws URISyntaxException {
        Matcher matcher = HostPattern.matcher(namedHost);
        if (!matcher.matches()) {
            throw new URISyntaxException(namedHost, "Host is not valid");
        }
        return namedHost;
    }
    
    private String parseIpV6Host(String ipV6Host) {
        return ipV6Host.toUpperCase();
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
    
    private void parsePath(String path) {
        if (path != null) {
            this.path = path;
        }
    }
    
}

