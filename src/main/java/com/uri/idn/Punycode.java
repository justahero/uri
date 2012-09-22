package com.uri.idn;

/**
 * This class offers encode and decode functions to transform a Unicode string to an ASCII string
 * and vice versa. See appendix C of RFC 3492 (http://www.ietf.org/rfc/rfc3492.txt) for more details
 *
 */
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
     * Applies the Punycode encode algorithm on a given Unicode string and converts it to a ASCII string.
     * 
     * @param label
     * @return
     * @throws PunycodeException 
     */
    public static String encode(String label) throws PunycodeException {
        // set up input and output variables
        StringBuffer output = new StringBuffer();
        CodepointIteratable sequence = new CodepointIteratable(label);
        
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
            for (int codepoint : sequence) {
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
            
            for (int codepoint : sequence) {
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
                        int t = (k <= bias) ? TMIN : (k >= bias + TMAX) ? TMAX : k - bias;
                        if (q < t) break;
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
        check_for_nonbasic_chars(result);
        
        return result;
    }
    
    /**
     * Applies the Punycode decode algorithm on a given ASCII string and converts it to a Unicode string
     * 
     * @param input
     * @return
     * @throws PunycodeException
     */
    public static String decode(String input) throws PunycodeException {
        StringBuffer output = new StringBuffer();
        
        int n = INITIAL_N;
        int i = 0;
        int bias = INITIAL_BIAS;
        
        // handle the basic code points at the start of the label
        // let b the number of input code points before the last delimiter or 0 if there is none
        // then copy the first b code pints to the output
        if (ACE_MAX_LENGTH * 2 < input.length()) {
            throw new PunycodeException("Output would exceed space");
        }
        
        // b marks the delimiter character position
        int b = Math.max(0, input.lastIndexOf(DELIMITER));
        if (b > ACE_MAX_LENGTH) {
            throw new PunycodeException("Output would exceed space");
        }
        
        // copy the basic code points until delimiter character
        for (int j = 0; j < b; j++) {
            char c = input.charAt(j);
            if (!isBasic(c)) {
                throw new PunycodeException("Invalid input character");
            }
            output.append(c);
        }
        
        // Main decoding loop: Start just after the last delimiter if any basic code points were copied.
        // or if not start at the beginning otherwise
        int index = (b > 0) ? b + 1 : 0;
        while (index < input.length()){
            // Decode a generalize variable-length integer into delta, which gets added to i.
            // the overflow checking is easier if we increase i as we go, then subtract off its
            // startig value at the end to obtain delta
            int w = 1;
            int oldi = i;
            for (int k = BASE; ; k += BASE) {
                if (index >= input.length()) {
                    throw new PunycodeException("Input is invalid!");
                }
                int codepoint = input.charAt(index++);
                int digit = decode_digit(codepoint);
                if (digit >= BASE || digit > (Integer.MAX_VALUE - i) / w) {
                    throw new PunycodeException("Overflow");
                }
                
                i += digit * w;
                int t = (k <= bias) ? TMIN : k >= bias + TMAX ? TMAX : k - bias;
                
                if (digit < t) break;
                if (w > Integer.MAX_VALUE / (BASE - t)) {
                    throw new PunycodeException("Input needs wider integers");
                }
                w *= (BASE - t);
            }
            
            int out = output.length();
            bias = adapt(i - oldi, out + 1, oldi == 0);
            
            // 'i' was supposed to wrap from output.length + 1 to 0,
            // incrementing n each time, so we'll fix that now
            if (i / (out + 1) > Integer.MAX_VALUE - n) {
                throw new PunycodeException("Input needs wider integers");
            }
            n = n + i / (out + 1);
            i = i % (out + 1);
            
            if (out >= ACE_MAX_LENGTH) {
                throw new PunycodeException("Output would exceed space");
            }
            
            output.insert(i, (char)n);
            i++;
        }
        
        return output.toString();
    }
    
    private static void check_for_nonbasic_chars(String label) throws PunycodeException {
        CodepointIteratable sequence = new CodepointIteratable(label);
        for (int codepoint : sequence) {
            if (!(codepoint >= 0 && codepoint <= 0x7F)) {
                throw new PunycodeException("Invalid output char");
            }
        }
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
     * Bias adaption function
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

