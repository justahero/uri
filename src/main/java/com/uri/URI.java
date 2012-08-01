package com.uri;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static String RegExUserInfo  = "([a-zA-Z0-9-._~!$&'()*+,;=:]|%[0-9a-fA-F]{2})+";
    private final static String RegExNamedHost = "(?:([a-zA-Z0-9-._~!$&'()*+,;=]|%[0-9a-fA-F]{2})*)?";
    private final static String RegExPort      = "([0-9]{1,5})";
    
    private final static String RegExScheme    = "^([a-zA-Z]+[a-zA-Z+-.]*)"; 
    
    private final static String RegExURI =
          "\\A" +
          RegExScheme + ":" +
          "\\/\\/" +
          "(?:([a-zA-Z0-9-._%!$&'()*+,;=:]+)@)?" +
          "(?:([a-zA-Z0-9-._~%]+)|(?:\\[(.+)\\])|(?:\\[v(.+)\\]))" +
          "(?::([0-9]+))?" +
          "(?:(\\/[a-zA-Z0-9-._~%!$&'()*+,;=:@]*))?" +
          "\\Z";
    
    private final static Pattern URIPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern HostPattern;
    private final static Pattern PortPattern;
    private final static Pattern SchemePattern;
    
    private String scheme    = null;
    private String username  = null;
    private String userpass  = null;
    private String host      = null;
    private String port      = null;
    private String path      = null;
    private String query     = null;
    private String fragment  = null;
    
    static {
        URIPattern      = Pattern.compile(RegExURI);
        UserInfoPattern = Pattern.compile(RegExUserInfo);
        HostPattern     = Pattern.compile(RegExNamedHost);
        PortPattern     = Pattern.compile(RegExPort);
        SchemePattern   = Pattern.compile(RegExScheme);
    }
    
    public URI() {
    }
    
    public URI withHost(String host) throws URISyntaxException {
        parseHost(host, null, null);
        return this;
    }
    
    private URI withUserInfo(String userInfo) throws URISyntaxException {
        parseUserInfo(userInfo);
        return this;
    }
    
    public URI withUserInfo(String username, String userpass) throws URISyntaxException {
        String userinfo = "";
        userinfo += (username != null) ? username : "";
        userinfo += (userpass != null) ? ":" + userpass : "";
        return withUserInfo(userinfo);
    }
    
    public URI withScheme(String scheme) throws URISyntaxException {
        parseScheme(scheme);
        return this;
    }
    
    public URI withPort(int port) throws URISyntaxException {
        parsePort("" + port); // TODO check port before!
        return this;
    }
    
    public URI withPath(String path) throws URISyntaxException {
        parsePath(path.startsWith("/") ? path : "/" + path);
        return this;
    }
    
    private URI withQuery(String query) {
        parseQuery(query);
        return this;
    }
    
    public URI withFragment(String fragment) {
        parseFragment(fragment);
        return this;
    }
    
    public static URI parse(String url) throws URISyntaxException {
        return new URI(url);
    }
    
    public static URI parseURL(URL url) throws URISyntaxException {
        URI uri = new URI()
                .withScheme(url.getProtocol())
                .withUserInfo(url.getUserInfo())
                .withHost(url.getHost())
                .withPort(url.getPort())
                .withPath(url.getPath())
                .withQuery(url.getQuery());
        return uri;
    }
    
    private URI(String url) throws URISyntaxException {
        parseURI(URIUtils.removePercentEncodedCharacters(url.toLowerCase()));
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
    
    public String userinfo() {
        if (username != null && userpass != null && !username.isEmpty()) {
            String.format("%s:%s", username, userpass);
        }
        return null;
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
    
    public String query() {
        return query;
    }
    
    public String fragment() {
        return fragment;
    }
    
    public String site() {
        return "";
    }
    
    // TODO create a valid representation of the URI as ASCII!
    public String toASCII() throws URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(scheme != null ? scheme + "://" : "");
        String userinfo = userinfo();
        builder.append(userinfo != null ? userinfo : "");
        builder.append(host != null ? host : "");
        builder.append(path != null ? path : "");
        builder.append(query != null ? query : "");
        builder.append(fragment != null ? fragment : "");
        
        String uri = builder.toString();
        Matcher matcher = URIPattern.matcher(uri);
        if (!matcher.find()) {
            throw new URISyntaxException(uri, "URI representation is not valid!"); 
        }
        return uri;
    }
    
    private void parseURI(String string) throws URISyntaxException {
        System.out.println("Parsing uri: " + string);
        Matcher matcher = URIPattern.matcher(string);
        if (matcher.find()) {
            for (int i = 1; i < matcher.groupCount(); i++) {
                System.out.println("  " + i + ": " + matcher.group(i));
            }
            
            String scheme    = matcher.group(1);
            String userInfo  = matcher.group(2);
            String namedHost = matcher.group(3);
            String ipv6Host  = matcher.group(4);
            String ipFuture  = matcher.group(5);
            String port      = matcher.group(6);
            String path      = matcher.group(7);
            
            parseScheme(scheme);
            parseUserInfo(userInfo);
            parseHost(namedHost, ipv6Host, ipFuture);
            parsePort(port);
            parsePath(path);
            
            if (matcher.end() != string.length()) {
                throw new URISyntaxException(string, "Some components could not be parsed!");
            }
        }
        else {
            throw new URISyntaxException(string, "Some components could not be parsed!");
        }
    }
    
    private void parseScheme(String scheme) throws URISyntaxException {
        if (scheme == null || scheme.isEmpty()) {
            throw new URISyntaxException(scheme, "No scheme given");
        }
        Matcher matcher = SchemePattern.matcher(scheme);
        if (!matcher.matches()) {
            throw new URISyntaxException(scheme, "No valid scheme");
        }
        this.scheme = scheme;
    }
    
    private void parseUserInfo(String userInfo) throws URISyntaxException {
        if (userInfo != null) {
            if (userInfo.isEmpty()) {
                throw new URISyntaxException(userInfo, "User info must be specified if '@' is present");
            }
            String[] parts = userInfo.split(":", -1);
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
    
    private void parseQuery(String query) {
        if (query != null) {
            this.query = query;
        }
    }
    
    private void parseFragment(String fragment) {
        if (fragment != null) {
            this.fragment = fragment;
        }
    }
}

