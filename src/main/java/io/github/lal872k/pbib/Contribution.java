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
import java.util.List;

/**
 * POJO used as part of the library info that is shown at start up.
 * @author L. Arthur Lewis II
 */
public class Contribution {
    
    private String title;
    
    private final ArrayList<String> contributers;
    
    public Contribution(){
        contributers = new ArrayList();
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void addContributer(String contributer){
        contributers.add(contributer);
    }
    
    public void removeContributer(String contributer){
        contributers.remove(contributer);
    }
    
    public String getTitle(){
        return title;
    }
    
    public List<String> getContributers(){
        return contributers;
    }
    
    public String[] getContributersArray(){
        return contributers.toArray(new String[0]);
    }
    
}
