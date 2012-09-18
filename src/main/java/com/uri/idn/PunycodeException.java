package com.uri.idn;

@SuppressWarnings("serial")
public class PunycodeException extends Exception {
    public static String OVERFLOW = "Overflow";
    public static String BAD_INPUT = "Bad Input";
    
    public PunycodeException(String message) {
        super(message);
    }
}
