/* 
 * The MIT License
 *
 * Copyright 2016 L. Arthur Lewis II.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.lal872k.pbib;

/**
 * This class represents a filled out source that can be used to get a bibliography and footnote
 * from.
 * @author L. Arthur Lewis II
 */
public class Citation {
    
    private String name;
    private TypeOfSource hand;
    private final Source source;
    
    /**
     * enum used to indicate whether the source was primary/first hand or secondary/second hand.
     */
    public enum TypeOfSource {
        /**
         * when the source is primary/first hand point of view. It comes directly from the source.
         */
        PRIMARY, 
        /**
         * when the source is secondary/ second hand point of view. Someone else has written about
         * it the event that actually was present.
         */
        SECONDARY;
    }
    
    public Citation(String name, Source source, TypeOfSource hand){
        this.name = name;
        this.source = source.getDeepCopy();
        this.hand = hand;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setSourceType(TypeOfSource hand){
        this.hand = hand;
    }
    
    public String getName(){
        return name;
    }
    
    public Source getSource(){
        return source;
    }
    
    public TypeOfSource getTypeOfSource(){
        return hand;
    }
    
}
