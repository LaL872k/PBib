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
 * Used to organize <code>{@link lal.pbib.Source}</code> by holding an array of them.
 * @author L. Arthur Lewis II
 */
public class SourceCategory {
    
    private Source[] sources;
    private String title;
    
    public SourceCategory(String title, Source[] sources){
        this.title = title;
        this.sources = sources;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setSourceTypes(Source[] sources){
        this.sources = sources;
    }
    
    public String getTitle(){
        return title;
    }
    
    public Source[] getSourceTypes(){
        return sources;
    }
    
    public Source getSourceType(String title){
        for (Source sourceType : getSourceTypes()){
            if (sourceType.getTitle().equals(title)){
                return sourceType;
            }
        }
        return null;
    }
    
}
