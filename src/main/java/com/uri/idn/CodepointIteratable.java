package com.uri.idn;

import java.util.Iterator;

public class CodepointIteratable implements Iterable<Integer>{
    private final String sequence;
    
    public CodepointIteratable(String sequence) {
        this.sequence = sequence;
    }
    
    public Iterator<Integer> iterator() {
        return new CodepointIterator(this.sequence);
    }
}
