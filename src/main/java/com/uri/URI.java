package com.uri;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI {
    
    private final static Map<String, Integer> DefaultPortMap = new HashMap<String, Integer>();
    
    private final static String ALPHA          = "a-zA-Z";
    private final static String DIGIT          = "0-9";
    private final static String HEX            = "a-fA-F0-9";
    private final static String UNRESERVED     = ALPHA + DIGIT + "-._~";
    private final static String SUBDELIM       = "!$&'()*+,;=";
    private final static String COMMON         = UNRESERVED + SUBDELIM;
    private final static String PERCENT        = "%["+HEX+"]{2}";
    
    private final static String RegExUserInfo  = "(["+COMMON+":]|"+PERCENT+")+";
    private final static String RegExNamedHost = "(?:(["+COMMON+"]|"+PERCENT+")*)";
    private final static String RegExScheme    = "^(["+ALPHA+"]+["+ALPHA+DIGIT+"+-.]*)"; 
    
    private final static String RegExURI =
          "\\A" +
          RegExScheme+":" + // scheme
          "(?:" + // authority
              "\\/\\/" +
              "(?:((?:["+COMMON+":]|"+PERCENT+")+)@)?" + // user info
              "(?:" + // host
                  "((?:["+UNRESERVED+"]|"+PERCENT+")*)" + // named or ip4 host 
                  "|" +
                  "(?:(\\[["+HEX+":.]+\\]))" + // ipv6 host
                  "|" +
                  "(?:\\[v(.+)\\])" + // ip future host
              ")" +
              "(?::([0-9]*))?" + // port
              "(/(?:["+COMMON+":@/]|"+PERCENT+")*)?" + // path
          "|" + // no authority
              "(?:" +
                  "(["+COMMON+"@]+["+COMMON+":@]*)" +
                  "|" +
                  "(?:(/["+COMMON+":@]+))?" +
              ")" +
          ")" +
          "(?:\\?(["+COMMON+":@/?]*))?" + // query string
          "(?:\\#(["+COMMON+":@/?]*))?" + // fragment
          "\\Z";
    
    private final static Pattern URIPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern HostPattern;
    private final static Pattern SchemePattern;
    
    private String scheme    = null;
    private String username  = null;
    private String userpass  = null;
    private String host      = null;
    private int    port      = -1;
    private String path      = null;
    private String query     = null;
    private String fragment  = null;
    
    static {
        URIPattern      = Pattern.compile(RegExURI);
        UserInfoPattern = Pattern.compile(RegExUserInfo);
        HostPattern     = Pattern.compile(RegExNamedHost);
        SchemePattern   = Pattern.compile(RegExScheme);
        
        DefaultPortMap.put("ftp",  21);
        DefaultPortMap.put("http", 80);
        DefaultPortMap.put("ldap", 389);
    }
    
    public URI() {
    }
    
    public URI withHost(String host) throws URISyntaxException {
        parseNamedHost(host);
        return this;
    }
    
    public URI withIPV6Host(String ipv6Host) {
        parseIpV6Host(ipv6Host);
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
    
    private URI withPort(String port) throws URISyntaxException {
        parsePort(port);
        return this;
    }
    
    public URI withPath(String path) throws URISyntaxException {
        parsePath(path);
        return this;
    }
    
    public URI withQuery(String query) {
        parseQuery(query);
        return this;
    }
    
    public URI withFragment(String fragment) {
        parseFragment(fragment);
        return this;
    }
    
    public static URI parse(String url) throws URISyntaxException {
        //url = URIUtils.removePercentEncodedCharacters(url);
        System.out.println("Parsing: " + url);
        Matcher matcher = URIPattern.matcher(url);
        if (matcher.find()) {
            if (matcher.end() != url.length()) {
                throw new URISyntaxException(url, "Some components could not be parsed!");
            }
            for (int i = 1; i < matcher.groupCount(); i++) {
                System.out.println("  " + i + ": " + matcher.group(i));
            }
            
            String scheme    = matcher.group(1);
            String userInfo  = matcher.group(2);
            String namedHost = matcher.group(3);
            String ipv6Host  = matcher.group(4);
            //String ipFuture  = matcher.group(5);
            String port      = matcher.group(6);
            String path      = matcher.group(7);
            if (path == null) {
                path = matcher.group(8);
            }
            if (path == null) {
                path = matcher.group(9);
            }
            String query = matcher.group(10);
            
            URI uri = new URI()
                .withScheme(scheme)
                .withUserInfo(userInfo)
                .withPort(port)
                .withPath(path)
                .withQuery(query);
            if (namedHost != null)
                uri.withHost(namedHost);
            if (ipv6Host != null)
                uri.withIPV6Host(ipv6Host);
            
            return uri;
        }
        throw new URISyntaxException(url, "Some components could not be parsed!");
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
            return String.format("%s:%s", username, userpass);
        }
        return null;
    }
    
    public String host() {
        return host;
    }
    
    public int port() {
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
        String authority = authority();
        String scheme = scheme();
        
        if (scheme != null && authority != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(scheme != null ? scheme + ":" : "");
            if (!authority.isEmpty()) {
                builder.append("//").append(authority);
            }
            return builder.toString();
        }
        
        return null;
    }
    
    public String authority() {
        StringBuilder result = new StringBuilder();
        String userinfo = userinfo();
        result.append(userinfo != null ? userinfo + "@" : "");
        result.append(host != null ? host + "" : "");
        
        int defaultPort = inferredPort();
        if (port != -1 && port != defaultPort) {
            result.append(":" + port);
        }
        return result.toString();
    }
    
    public int inferredPort() {
        if (scheme != null && DefaultPortMap.containsKey(scheme)) {
            return DefaultPortMap.get(scheme).intValue();
        }
        return -1;
    }
    
    public String toASCII() throws URISyntaxException {
        String authority = authority();
        String path = path();
        if (path != null && path.compareTo("/") == 0) {
            path = "";
        }
        
        if (authority.isEmpty() && path == null) {
            throw new URISyntaxException("", "URI is missing authority or path!");
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(site());
        builder.append(path != null ? path : "");
        builder.append(query != null ? "?" + query : "");
        builder.append(fragment != null ? "#" + fragment : "");
        
        String uri = builder.toString();
        Matcher matcher = URIPattern.matcher(uri);
        if (!matcher.find()) {
            throw new URISyntaxException(uri, "URI representation is not valid!"); 
        }
        return uri;
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
    
    private void parseNamedHost(String namedHost) throws URISyntaxException {
        namedHost = URIUtils.normalizeString(namedHost);
        Matcher matcher = HostPattern.matcher(namedHost);
        if (!matcher.matches()) {
            throw new URISyntaxException(namedHost, "Host is not valid");
        }
        host = namedHost;
    }
    
    private void parseIpV6Host(String ipV6Host) {
        host = ipV6Host;
    }
    
    private void parsePort(String port) throws URISyntaxException {
        if (port == null)
            return;
        
        if (!port.isEmpty()) {
            int portNumber = Integer.valueOf(port);
            if (portNumber < 1 || portNumber > 65535) {
                throw new URISyntaxException(port, "Invalid port number");
            }
            this.port = portNumber;
        }
    }
    
    private void parsePath(String path) {
        if (path != null) {
            this.path = (host != null && !path.startsWith("/")) ? "/" + path : path;
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

