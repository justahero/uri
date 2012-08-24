package com.uri;

public class NameValuePair implements Comparable<NameValuePair> {
    public final String key;
    public final String value;
    
    public NameValuePair(String key, String value) {
        this.key   = key;
        this.value = value;
    }
    
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append((key != null) ? key : "");
        result.append((key != null && value != null) ? '=' : "");
        result.append((value != null) ? value : "");
        return (result.length() > 0) ? result.toString() : "";
    }
    
    @Override
    public int compareTo(NameValuePair rhs) {
        int compare = key.compareTo(rhs.key);
        if (compare != 0) {
            return compare;
        }
        return value.compareTo(rhs.value);
    }
}
