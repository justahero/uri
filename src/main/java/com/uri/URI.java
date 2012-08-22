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
    
    private final static String RegExUserInfo  = "(?:["+COMMON+":]|"+PERCENT+")*";
    private final static String RegExScheme    = "^(["+ALPHA+"]+["+ALPHA+DIGIT+"+-.]*)";
    
    private final static String RegExNamedHost = "(?:["+UNRESERVED+"]|"+PERCENT+")*";
    private final static String RegExIPV6Host  = "\\[["+HEX+":.]+\\]";
    private final static String RegExIPFuture  = "(?:\\[v["+HEX+".]+["+COMMON+":]+\\])";
    private final static String RegExHost      = "("+RegExNamedHost+"|"+RegExIPV6Host+"|"+RegExIPFuture+")?";
    
    private final static String RegExQuery     = "(?:\\?(["+COMMON+":@/?]*))";
    private final static String RegExFragment  = "(?:\\#(["+COMMON+":@/?]*))";
    
    private final static String RegExRequestURI = "(/?["+COMMON+":@]+(?:/["+COMMON+":@]+)*/?)?"+RegExQuery+"?"+RegExFragment+"?";
    
    private final static String RegExURI =
          "\\A" +
          "(?:" +
          RegExScheme+":" + // scheme
          "(?:" + // authority
              "//" +
              "(?:(" + RegExUserInfo + ")@)?" +
              RegExHost +
              "(?::([0-9]*))?" + // port
              "(/(?:["+COMMON+":@/]|"+PERCENT+")*)?" + // path
              "|" + // no path with authority
              "(/?["+COMMON+":@]+(/["+COMMON+":@]+)*/?)?" +
          ")" +
          "|" +
          "(" +
              // path without scheme and authority
              "(?:["+COMMON+"@]|"+PERCENT+")+(?:/["+COMMON+":@]|"+PERCENT+")*/?" +
              "|" + // path leading with scheme and authority
              "(?:/["+COMMON+":@]+)+/?" +
          ")" +
          ")" +
          RegExQuery + "?" + // query string
          RegExFragment + "?" + // fragment
          "\\Z";
    
    private final static Pattern URIPattern;
    private final static Pattern UserInfoPattern;
    private final static Pattern SchemePattern;
    private final static Pattern RequestURIPattern;
    
    private final static Pattern NamedHostPattern;
    private final static Pattern IPV6HostPattern;
    private final static Pattern IPFuturePattern;
    
    private String scheme    = null;
    private String username  = null;
    private String userpass  = null;
    private String host      = null;
    private int    port      = -1;
    private String path      = null;
    private String query     = null;
    private String fragment  = null;
    
    static {
        URIPattern        = Pattern.compile(RegExURI);
        UserInfoPattern   = Pattern.compile(RegExUserInfo);
        SchemePattern     = Pattern.compile(RegExScheme);
        RequestURIPattern = Pattern.compile(RegExRequestURI);
        
        NamedHostPattern = Pattern.compile(RegExNamedHost);
        IPV6HostPattern  = Pattern.compile(RegExIPV6Host);
        IPFuturePattern  = Pattern.compile(RegExIPFuture);
        
        DefaultPortMap.put("ftp",  21);
        DefaultPortMap.put("http", 80);
        DefaultPortMap.put("ldap", 389);
    }
    
    public URI() {
    }
    
    public URI withHost(String host) throws URISyntaxException {
        parseHost(host);
        return this;
    }
    
    private URI withUserInfo(String userInfo) throws URISyntaxException {
        username = null;
        userpass = null;
        parseUserInfo(userInfo);
        return this;
    }
    
    public URI withUserInfo(String username, String userpass) throws URISyntaxException {
        String userinfo = "";
        userinfo += (username != null) ? username : "";
        userinfo += (userpass != null && !userpass.isEmpty()) ? ":" + userpass : "";
        return withUserInfo(userinfo.isEmpty() ? null : userinfo);
    }
    
    public URI withScheme(String scheme) throws URISyntaxException {
        parseScheme(scheme);
        return this;
    }
    
    public URI withPort(int port) throws URISyntaxException {
        parsePort("" + port);
        return this;
    }
    
    private URI withPort(String port) throws URISyntaxException {
        parsePort(port);
        return this;
    }
    
    public URI withPath(String path) {
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
        System.out.println("Parsing: " + url);
        Matcher matcher = URIPattern.matcher(url);
        if (matcher.find()) {
            for (int i = 1; i < matcher.groupCount(); i++) {
                System.out.println("  " + i + ": " + matcher.group(i));
            }
            System.out.println(" -> " + matcher.start() + ", " + matcher.end());
            
            String scheme   = matcher.group(1);
            String userInfo = matcher.group(2);
            String host     = matcher.group(3);
            String port     = matcher.group(4);
            String path     = matcher.group(5);
            if (path == null) {
                path = matcher.group(6);
            }
            if (path == null) {
                path = matcher.group(7);
            }
            if (path == null) {
                path = matcher.group(8);
            }
            String query = matcher.group(9);
            
            URI uri = new URI()
                .withScheme(scheme)
                .withUserInfo(userInfo)
                .withPort(port)
                .withPath(path)
                .withQuery(query)
                .withHost(host);
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
        String userinfo = "";
        userinfo += (username != null) ? username : "";
        userinfo += (userpass != null && !userpass.isEmpty()) ? ":" + userpass : "";
        return userinfo;
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
    
    public String requestURI() {
        StringBuilder builder = new StringBuilder();
        builder.append(path != null ? path : "");
        builder.append(query != null ? "?" + query : "");
        builder.append(fragment != null ? "#" + fragment : "");
        return builder.toString();
    }
    
    public String authority() {
        StringBuilder result = new StringBuilder();
        String userinfo = userinfo();
        
        result.append(userinfo != null && !userinfo.isEmpty() ? userinfo + "@" : "");
        result.append(host != null ? host : "");
        
        if (port != -1 && port != inferredPort()) {
            result.append(":" + port);
        }
        return result.toString();
    }

    public String site() {
        String authority = authority();
        String scheme = scheme();
        
        StringBuilder builder = new StringBuilder();
        builder.append(scheme != null ? scheme + ":" : "");
        if (scheme != null && !authority.isEmpty()) {
            builder.append("//");
        }
        builder.append(authority);
        
        return (builder.length() > 0) ? builder.toString() : null;
    }
    
    public URI withRequestURI(String request) {
        parseRequestURI(request);
        return this;
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
        String site = site();
        
        if (username == null && userpass != null) {
            throw new URISyntaxException(userinfo(), "Userpass given but no username");
        }
        
        if (path != null) {
            if (path.compareTo("/") == 0) {
                path = "";
            } else if (path.startsWith("//")) {
                throw new URISyntaxException(path, "Path component must not start with '//'");
            }
        }
        
        if (authority.isEmpty() && path == null) {
            throw new URISyntaxException("", "URI is missing authority or path!");
        }
        if (authority.length() > 0 && scheme == null) {
            throw new URISyntaxException("", "Authority given but no scheme found!");
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(site != null ? site : "");
        builder.append(path != null ? path : "");
        builder.append(query != null ? "?" + query : "");
        builder.append(fragment != null ? "#" + fragment : "");
        
        String uri = builder.toString();
        // TODO remove this check, it's redundant
        Matcher matcher = URIPattern.matcher(uri);
        if (!matcher.find() || uri.isEmpty()) {
            throw new URISyntaxException(uri, "URI representation is not valid!"); 
        }
        return uri;
    }
    
    private void parseScheme(String scheme) throws URISyntaxException {
        if (scheme != null) {
            Matcher matcher = SchemePattern.matcher(scheme);
            if (!matcher.matches()) {
                throw new URISyntaxException(scheme, "No valid scheme");
            }
        }
        this.scheme = scheme;
    }
    
    private void parseUserInfo(String userInfo) throws URISyntaxException {
        if (userInfo != null) {
            String[] parts = userInfo.split(":", -1);
            if (parts.length > 2 || !isUserInfoValid(userInfo)) {
                throw new URISyntaxException(userInfo, "User info is not valid");
            }
            username = (!parts[0].isEmpty()) ? parts[0] : null;
            userpass = (parts.length > 1 && !parts[1].isEmpty()) ? parts[1] : null;
        }
    }
    
    private boolean isUserInfoValid(String userInfo) {
        Matcher matcher = UserInfoPattern.matcher(userInfo);
        return matcher.matches();
    }
    
    private void parseHost(String host) throws URISyntaxException {
        if (host == null) {
            return;
        }
        if (NamedHostPattern.matcher(host).matches()) {
            this.host = URIUtils.normalizeString(host, false);
        } else if (IPV6HostPattern.matcher(host).matches()) {
            this.host = host;
        } else if (IPFuturePattern.matcher(host).matches()) {
            this.host = host;
        } else {
            throw new URISyntaxException(host, "Host is not valid");
        }
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
            this.path = URIUtils.normalizeString(this.path, true);
            this.path = URIUtils.removeDotSegments(this.path);
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
    
    private void parseRequestURI(String request) {
        this.path = null;
        this.query = null;
        this.fragment = null;
        Matcher matcher = RequestURIPattern.matcher(request);
        if (matcher.matches()) {
            String path = matcher.group(1);
            String query = matcher.group(2);
            String fragment = matcher.group(3);
            
            parsePath(path);
            parseQuery(query);
            parseFragment(fragment);
        }
    }
    
}

