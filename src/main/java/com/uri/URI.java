package com.uri;

import java.net.IDN;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uri.idn.SimpleIDN;

public class URI {
    
    private final static Map<String, Integer> DefaultPortMap = new HashMap<String, Integer>();
    
    private final static char   DEFAULT_DELIMITER = '&';
    
    private final static String ALPHA          = "a-zA-Z";
    private final static String DIGIT          = "0-9";
    private final static String HEX            = "a-fA-F0-9";
    private final static String UNRESERVED     = ALPHA + DIGIT + "-._~";
    private final static String SUBDELIM       = "!$&'()*+,;=";
    private final static String COMMON         = UNRESERVED + SUBDELIM;
    private final static String PERCENT        = "%["+HEX+"]{2}";
    
    private final static String RegExUserInfo  = "(?:["+COMMON+":]|"+PERCENT+")*";
    private final static String RegExScheme    = "^(["+ALPHA+"]+["+ALPHA+DIGIT+"+-.]*)";
    
    private final static String RegExNamedHost = "(?:[^\\[\\]:/?#]*|"+PERCENT+")*";
    private final static String RegExIPV6Host  = "\\[["+HEX+":.]+\\]";
    private final static String RegExIPFuture  = "(?:\\[v["+HEX+".]+["+COMMON+":]+\\])";
    private final static String RegExHost      = "("+RegExNamedHost+"|"+RegExIPV6Host+"|"+RegExIPFuture+")?";
    
    private final static String RegExQuery     = "(?:\\?(["+COMMON+":@/?\\[\\]%]*))";
    private final static String RegExRequestURI = "(/?["+COMMON+":@]+(?:/["+COMMON+":@]+)*/?)?"+RegExQuery+"?";
    
    private final static String RegExAuthority =
          "\\A^" +
          "(?:([^\\[\\]]*)@)?" +   // user info
          RegExHost+"?" +          // host
          "(?::([^:@\\[\\]]*))?" + // port
          "$\\Z";
    private final static String RegExURI =
          "\\A" +
          "(?:([^:/?#]+):)?" +       // scheme
          "(?:\\/\\/([^\\/?#]*))?" + // authority
          "(?:([^?#]*))?" +          // path
          "(?:\\?([^#]*))?" +        // query string
          "(?:#(.*))?" +             // fragment
          "\\Z";
    
    private final static Pattern URIPattern;
    private final static Pattern AuthorityPattern;
    
    private final static Pattern UserInfoPattern;
    private final static Pattern SchemePattern;
    private final static Pattern RequestURIPattern;
    
    private final static Pattern NamedHostPattern;
    private final static Pattern IPV6HostPattern;
    private final static Pattern IPFuturePattern;
    
    private final Vector<NameValuePair> queries = new Vector<NameValuePair>();
    private String scheme    = null;
    private String username  = null;
    private String userpass  = null;
    private String host      = null;
    private int    port      = -1;
    private String path      = null;
    private String fragment  = null;
    
    private char   delimiter = DEFAULT_DELIMITER;
    
    static {
        URIPattern        = Pattern.compile(RegExURI);
        AuthorityPattern  = Pattern.compile(RegExAuthority);
        UserInfoPattern   = Pattern.compile(RegExUserInfo);
        SchemePattern     = Pattern.compile(RegExScheme);
        RequestURIPattern = Pattern.compile(RegExRequestURI);
        
        NamedHostPattern = Pattern.compile(RegExNamedHost);
        IPV6HostPattern  = Pattern.compile(RegExIPV6Host);
        IPFuturePattern  = Pattern.compile(RegExIPFuture);
        
        DefaultPortMap.put("ftp",   21);
        DefaultPortMap.put("http",  80);
        DefaultPortMap.put("https", 443);
        DefaultPortMap.put("ldap",  389);
    }
    
    public URI() {
    }
    
    public URI withAuthority(String authority) throws URISyntaxException {
        parseAuthority(authority);
        return this;
    }
    
    public URI withHost(String host) throws URISyntaxException {
        this.host = null;
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
        this.scheme = null;
        parseScheme(scheme);
        return this;
    }
    
    public URI withPort(int port) throws URISyntaxException {
        parsePort(Integer.toString(port));
        return this;
    }
    
    private URI withPort(String port) throws URISyntaxException {
        this.port = -1;
        parsePort(port);
        return this;
    }
    
    public URI withPath(String path) throws URISyntaxException {
        this.path = null;
        parsePath(path);
        return this;
    }
    
    public URI withQuery(String query) {
        parseQuery(query);
        return this;
    }
    
    public URI addParam(String key, String value) {
        parseQuery(key, value);
        return this;
    }
    
    public void queryDelimiter(char delimiter) {
        if (this.delimiter != delimiter) {
            String query = query();
            this.delimiter = delimiter;
            parseQuery(query);
        } 
    }
    
    public URI sortQuery() {
        Collections.sort(this.queries);
        return this;
    }
    
    public URI withFragment(String fragment) {
        parseFragment(fragment);
        return this;
    }
    
    public static URI parse(String url) throws URISyntaxException {
        Matcher matcher = URIPattern.matcher(url);
        if (matcher.find()) {
            String scheme    = matcher.group(1);
            String authority = matcher.group(2);
            String path      = matcher.group(3);
            String query     = matcher.group(4);
            String fragment  = matcher.group(5);
            URI uri = new URI()
                .withScheme(scheme)
                .withAuthority(authority)
                .withPath(path)
                .withQuery(query)
                .withFragment(fragment);
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
        Vector<String> result = new Vector<String>();
        for (NameValuePair pair : queries) {
            result.add(pair.toString());
        }
        return queries.isEmpty() ? "" : URIUtils.join(result, Character.toString(delimiter));
    }
    
    public List<NameValuePair> queries() {
        return queries;
    }
    
    public String fragment() {
        return fragment;
    }
    
    /**
     * Returns the HTTP request URI, consisting of path and the query components of the URI.
     * 
     * @return
     */
    public String requestURI() {
        String query = query();
        StringBuilder builder = new StringBuilder();
        builder.append(path != null ? path : "");
        builder.append(!query.isEmpty() ? "?" + query : "");
        return builder.toString();
    }
    
    /**
     * Returns the HTTP authority part. The authority consists of user info (if available) the
     * host (named, ipv4, ipv6 or ipfuture) and the port (if available and different from default).
     * 
     * @return The authority part of the URI.
     */
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
    
    /**
     * Returns the composite site component, consisting of scheme and authority, e.g.
     * The site component of the URI 'http://user:pass@www.example.com:1234/path?query=true#fragment'
     * is 'http://user:pass@www.example.com:1234'
     * 
     * @return
     */
    public String site() {
        String scheme = scheme();
        String authority = authority();
        
        StringBuilder builder = new StringBuilder();
        builder.append(scheme != null ? scheme + ":" : "");
        if (scheme != null && !authority.isEmpty()) {
            builder.append("//");
        }
        builder.append(authority);
        
        return (builder.length() > 0) ? builder.toString() : null;
    }
    
    /**
     * Sets the request URI component consisting of path and query parameters, e.g.
     * 'path/to/resource?q=all&search=foo'
     * 
     * @param request
     * @return
     * @throws URISyntaxException
     */
    public URI withRequestURI(String request) throws URISyntaxException {
        path = null;
        queries.clear();
        parseRequestURI(request);
        return this;
    }
    
    public int inferredPort() {
        if (scheme != null && DefaultPortMap.containsKey(scheme)) {
            return DefaultPortMap.get(scheme).intValue();
        }
        return -1;
    }
    
    public boolean isAbsolute() {
        return !isRelative();
    }
    
    public boolean isRelative() {
        return (scheme == null);
    }
    
    /**
     * 
     * 
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public URI join(String uri) throws URISyntaxException {
        return join(URI.parse(uri));
    }

    /**
     * Joins the URI with another one, useful for Reference resolution, e.g. specifying a relative URI
     * to a different one, acting as Base URI.
     * 
     * A relative path can only be joined when a base URI is known. A base URI must conform to the
     * absolute URI syntax (see section 4.3 of RFC 3986). A base URI is an absolute URI with scheme,
     * authority. How a base URI can be obtained of a reference is described in section 5 of RFC 3986.
     * 
     * The algorithm used is taken from section 5.2.2
     * 
     * @param relativePath
     * @return
     * @throws URISyntaxException
     */
    public URI join(URI uri) throws URISyntaxException {
        String targetScheme    = null;
        String targetAuthority = null;
        String targetPath      = null;
        String targetQuery     = null;
        String targetFragment  = null;
        
        if (uri.scheme != null) {
            targetScheme    = uri.scheme;
            targetAuthority = uri.authority();
            targetPath      = uri.path;
            targetQuery     = uri.query();
        } else {
            if (!uri.authority().isEmpty()) {
                targetAuthority = uri.authority();
                targetPath      = uri.path;
                targetQuery     = uri.query();
            } else {
                if (uri.path == null || uri.path.isEmpty()) {
                    targetPath = this.path;
                    if (!uri.query().isEmpty()) {
                        targetQuery = uri.query();
                    } else {
                        targetQuery = this.query();
                    }
                } else {
                    if (uri.path.startsWith("/")) {
                        targetPath = uri.path;
                    } else {
                        targetPath = mergePath(this, uri);
                    }
                    targetQuery = uri.query();
                }
                targetAuthority = this.authority();
            }
            targetScheme = this.scheme;
        }
        targetFragment = uri.fragment;
        
        URI targetURI = new URI()
            .withScheme(targetScheme)
            .withAuthority(targetAuthority)
            .withPath(targetPath)
            .withQuery(targetQuery)
            .withFragment(targetFragment);
        return targetURI;
    }
    
    /**
     * See section 5.2.3 of RFC 3986 for more details
     * 
     * @param basePath
     * @param relativePath
     * @return
     */
    private static String mergePath(URI baseURI, URI referenceURI) {
        String basePath = (baseURI.path == null || baseURI.path.isEmpty()) ? "" : baseURI.path;
        
        if (!baseURI.authority().isEmpty() && basePath.isEmpty()) {
            basePath = "/";
        } else {
            // remove the rightmost path segment from the base path (if available)
            int index = basePath.lastIndexOf('/');
            if (index != -1) {
                basePath = basePath.substring(0, index + 1);
            } else {
                basePath = "";
            }
        }
        
        if (referenceURI.path != null) {
            basePath += referenceURI.path;
        }
        
        return basePath;
    }
    
    public String toASCII() throws URISyntaxException {
        String authority = authority();
        String path = path();
        String site = site();
        String query = query();
        
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
        builder.append(!query.isEmpty() ? "?" + query : "");
        builder.append(fragment != null ? "#" + fragment : "");
        
        String uri = SimpleIDN.toASCII(builder.toString());
        return uri;
    }
    
    private void parseAuthority(String authority) throws URISyntaxException {
        if (authority != null) {
            Matcher matcher = AuthorityPattern.matcher(authority);
            if (!matcher.matches()) {
                throw new URISyntaxException(authority, "No valid authority given");
            }
            String userinfo = matcher.group(1);
            String host     = matcher.group(2);
            String port     = matcher.group(3);
            withUserInfo(userinfo);
            withHost(host);
            withPort(port);
        }
    }
    
    private void parseScheme(String scheme) throws URISyntaxException {
        if (scheme != null) {
            Matcher matcher = SchemePattern.matcher(scheme);
            if (!matcher.matches()) {
                throw new URISyntaxException(scheme, "No valid scheme");
            }
            this.scheme = scheme.toLowerCase();
        }
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
            String ascii = IDN.toASCII(host);
            this.host = URIUtils.normalize(URIUtils.normalizeString(ascii, false), URIUtils.REGNAME);
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
            try {
                int portNumber = Integer.valueOf(port);
                if (portNumber < 1 || portNumber > 65535) {
                    throw new URISyntaxException(port, "Invalid port number");
                }
                this.port = portNumber;
            } catch (Exception e) {
                throw new URISyntaxException(port, "Failed to parse port correctly");
            }
        }
    }
    
    private void parsePath(String path) throws URISyntaxException {
        if (path != null && !path.isEmpty()) {
            this.path = (host != null && !path.startsWith("/")) ? "/" + path : path;
            this.path = URIUtils.normalizeString(this.path, true);
            this.path = URIUtils.removeDotSegments(this.path);
        }
    }
    
    private void parseQuery(String key, String value) {
        key   = (key != null && !key.isEmpty()) ? key : null;
        value = (value != null && !value.isEmpty()) ? value : null;
        if (key != null || value != null) {
            queries.add(new NameValuePair(key, value));
        }
    }
    
    private void parseQuery(String query) {
        queries.clear();
        if (query != null) {
            String[] parts = query.split(Character.toString(delimiter));
            for (String part : parts) {
                int index = part.indexOf('=');
                String key = part;
                String value = null;
                if (index != -1) {
                    key = part.substring(0, index);
                    value = part.substring(index + 1, part.length());
                }
                parseQuery(key, value);
            }
        }
    }
    
    private void parseFragment(String fragment) {
        if (fragment != null) {
            this.fragment = fragment;
        }
    }
    
    private void parseRequestURI(String request) throws URISyntaxException {
        boolean found = (scheme != null) ? scheme.matches("^https?$") : false;
        if (isAbsolute() && !found) {
            throw new URISyntaxException(request, "Cannot set an HTTP request URI for non-HTTP URI.");
        }
        
        Matcher matcher = RequestURIPattern.matcher(request);
        if (matcher.matches()) {
            String path = matcher.group(1);
            String query = matcher.group(2);
            parsePath(path);
            parseQuery(query);
        }
    }

}

