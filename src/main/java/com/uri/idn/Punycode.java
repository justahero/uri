package com.uri.idn;


public class Punycode {
    public static final int BASE = 36;
    public static final int TMIN = 1;
    public static final int TMAX = 26;
    public static final int SKEW = 38;
    public static final int DAMP = 700;
    public static final int INITIAL_BIAS = 72;
    public static final int INITIAL_N = 0x80;
    
    public static final int DELIMITER = 0x2D; // Hyphen
    public static final int MAXINT = Integer.MAX_VALUE;
    
    public static final int UNICODE_MAX_LENGTH = 256;
    public static final int ACE_MAX_LENGTH = 256;
    
    /**
     * Applies the Punycode encode algorithm on a given Unicode string and converts it to ASCII string.
     * 
     * @param label
     * @return
     * @throws PunycodeException 
     */
    public static String encode(String label) throws PunycodeException {
        // set up input and output variables
        StringBuffer output = new StringBuffer();
        
        // initialize states
        int n = INITIAL_N;
        int delta = 0;
        int bias = INITIAL_BIAS;
        
        // handle basic code points, they all appear upfront
        encode_basic_chars(label, output);
        // b is the number if handled basic code points
        int b = output.length();
        if (b > 0) {
            output.appendCodePoint(DELIMITER);
        }
        
        // h is the number of handled code points
        int h = b;
        
        // main encoding loop for all non-ASCII characters
        while (h < label.length()) {
            // all non-basic code points have been handled already, find the next larger one
            int m = MAXINT;
            for (int i = 0; i < label.length(); i++) {
                char codepoint = label.charAt(i);
                if (codepoint >= n && codepoint < m) {
                    m = codepoint;
                }
            }
            // increase delta enough to advance encoder's <n,i> state to <m,0> (section 3.2)
            // but guard against overflow (section 6.4)
            if (m - n > (Integer.MAX_VALUE - delta) / (h + 1)) {
                throw new PunycodeException("Input needs a wider integer range (overflow)");
            }
            delta += (m - n) * (h + 1);
            n = m;
            
            for (int i = 0; i < label.length(); i++) {
                char codepoint = label.charAt(i);
                if (codepoint < n) {
                    delta ++;
                    if (delta == 0) {
                        throw new PunycodeException("Input needs a wider integer range (overflow)");
                    }
                }
                
                if (codepoint == n) {
                    int q = delta;
                    
                    // represents delta as a generalized variable-length integer
                    for (int k = BASE;; k += BASE) {
                        int t = 0;
                        if (k <= bias) {
                            t = TMIN;
                        } else if (k >= bias + TMAX) {
                            t = TMAX;
                        } else {
                            t = k - bias;
                        }
                        
                        if (q < t) {
                            break;
                        }
                        
                        output.append((char)encode_digit(t + (q - t) % (BASE - t)));
                        q = (q - t) / (BASE - t);
                    }
                    
                    output.appendCodePoint(encode_digit(q));
                    bias = adapt(delta, h + 1, h == b);
                    delta = 0;
                    h++;
                }
            }
            delta++;
            n++;
        }
        
        String result = output.toString();
        CodepointIteratable resultIt = new CodepointIteratable(result);
        for (int codepoint : resultIt) {
            if (!(codepoint >= 0 && codepoint <= 0x7F)) {
                throw new PunycodeException("Invalid output char");
            }
        }
        
        return result;
    }
    
    private static void encode_basic_chars(String label, StringBuffer output) throws PunycodeException {
        CodepointIteratable sequence = new CodepointIteratable(label);
        for (int codepoint : sequence) {
            if (isBasic(codepoint)) {
                if (ACE_MAX_LENGTH - output.length() < 2) {
                    throw new PunycodeException("Output would exceed the provided space!");
                }
                output.appendCodePoint(codepoint);
            }
        }
    }
    
    private static boolean isBasic(int codepoint) {
        return (codepoint < 0x80);
    }
    
    private static boolean isDelim(int codepoint) {
        return (codepoint == DELIMITER);
    }
    
    private static int encode_digit(int digit) {
        return digit + 22 + 75 * ((digit < 26) ? 1 : 0);
    }
    
    /**
     * Returns the numeric value of a basic codepoint (for use in representing integers) in the range
     * 0 - 127 or base if codepoint does not represent a value
     * 
     * @param codepoint A value in range of 0-127 or a base value
     * @return
     */
    private static int decode_digit(int codepoint) {
        if (codepoint - 48 < 10) return codepoint - 22;
        if (codepoint - 65 < 26) return codepoint - 65;
        if (codepoint - 97 < 26) return codepoint - 97;
        return BASE;
    }
    
    /**
     * Bi)as adaption function
     * 
     * @param delta
     * @param numpoints
     * @param firsttime
     * @return
     */
    private static int adapt(int delta, int numpoints, boolean firsttime) {
        delta = firsttime ? delta / DAMP : (delta >> 1);
        delta += delta / numpoints;
        
        int difference = BASE - TMIN;
        int k = 0;
        while (delta > ((difference * TMAX) >> 1)) {
            delta /= difference;
            k += BASE;
        }
        return k + (difference + 1) * delta / (delta + SKEW);
    }
    
}

