package com.uri.idn;

import java.util.Iterator;

public class CodepointIterator implements Iterator<Integer> {
    private final String sequence;
    private int index = 0;
    
    public CodepointIterator(String sequence) {
        this.sequence = sequence;
    }
    
    public boolean hasNext() {
        return index < sequence.length();
    }
    
    public Integer next() {
        int codepoint = sequence.codePointAt(index);
        index += Character.charCount(codepoint);
        return codepoint;
    }
    
    public void reset() {
        index = 0;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    public int size() {
        return sequence.codePointCount(0, sequence.length() - 1);
    }
}

