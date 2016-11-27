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

import java.util.ArrayList;

/**
 * Highest class for organizing sources. Contains a list of 
 * <code>{@link lal.pbib.SourceCategory}</code>.
 * @author L. Arthur Lewis II
 */
public final class SourceLibrary {
    
    private final ArrayList<SourceCategory> sources;
    
    private SourceLibraryInfo info;
    
    public SourceLibrary(){
        sources = new ArrayList();
    }
    
    public void setInfo(SourceLibraryInfo info){
        this.info = info;
    }
    
    public void addSource(SourceCategory source){
        sources.add(source);
    }
    
    public void removeSource(SourceCategory source){
        sources.add(source);
    }
    
    public SourceLibraryInfo getInfo(){
        return info;
    }
    
    public SourceCategory[] getSources(){
        return sources.toArray(new SourceCategory[0]);
    }
    
    public SourceCategory getSource(String title){
        for (SourceCategory source : getSources()){
            if (source.getTitle().equals(title)){
                return source;
            }
        }
        return null;
    }
    
}
