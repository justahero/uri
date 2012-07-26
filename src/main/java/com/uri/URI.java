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
    private final static String RegExAuthority = "";
    
    private final static Pattern SchemePattern;
    private final static Pattern AuthorityPattern;
    
    private final static Pattern UserInfoPattern;
    
    private String  scheme    = "";
    private String  userinfo  = "";
    private String  username  = "";
    private String  userpass  = "";
    private boolean hasAuthority = true;
    
    private StringBuilder remaining = new StringBuilder();
    
    static {
        SchemePattern    = Pattern.compile(RegExScheme);
        AuthorityPattern = Pattern.compile(RegExAuthority);
        UserInfoPattern  = Pattern.compile(RegExUserInfo);
    }
    
    public URI(String url) throws URISyntaxException {
        remaining.append(url);
        parseScheme(remaining);
        parseAuthority(remaining);
    }
    
    public String scheme() {
        return scheme;
    }
    
    public String userinfo() {
        return userinfo;
    }
    
    public String username() {
        return username;
    }
    
    public String userpass() {
        return userpass;
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
        if (url.toString().startsWith("//")) {
            hasAuthority = true;
            int userInfoIndex = url.indexOf("@");
            if (userInfoIndex != -1) {
                String userInfo = url.substring(2, userInfoIndex);
                parseUserInfo(userInfo);
                url.delete(0, userInfoIndex);
            }
        } else {
            throw new URISyntaxException(url.toString(), "Currently this is not supported");
        }
    }
    
    private void parseUserInfo(String userInfo) throws URISyntaxException {
        if (userInfo.isEmpty()) {
            throw new URISyntaxException(userInfo, "User info must be specified if '@' is present");
        }
        
        String[] parts = userInfo.split(":");
        if (parts.length > 2 || parts[0].isEmpty() || !isUserInfoValid(userInfo)) {
            throw new URISyntaxException(userInfo, "User info is not valid");
        }
        
        username = parts[0];
        userpass = (parts.length > 1) ? parts[1] : "";
        this.userinfo = userInfo;
    }
    
    private boolean isUserInfoValid(String userInfo) {
        Matcher matcher = UserInfoPattern.matcher(userInfo);
        return matcher.matches();
    }
}


