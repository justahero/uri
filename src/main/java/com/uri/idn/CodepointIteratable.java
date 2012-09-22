package com.uri.idn;

import java.util.Iterator;

/**
 * This Iterable class can be used to iterate more conveniently over all code points of a string sequence, e.g.
 * 
 * <code>
 *   CodepointIteratable sequence = new CodepointIterable(label)
 *   for (int codepoint : sequence) {
 *      // ...
 *   }
 * </code>
 *
 */
public class CodepointIteratable implements Iterable<Integer>{
    private final String sequence;
    
    public CodepointIteratable(String sequence) {
        this.sequence = sequence;
    }
    
    public Iterator<Integer> iterator() {
        return new CodepointIterator(this.sequence);
    }
}
