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
package io.github.lal872k.pbib.ui.comps;

import io.github.lal872k.pbib.ReferenceCode;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;

/**
 * These are the fields inside the form used to collect data about a citation.
 * @author L. Arthur Lewis II
 */
public abstract class CompInput {
    
    private String name;
    
    private boolean italic, bold;
    
    public CompInput(String name){
        this.name = name;
    }
    
    /**
     * @return component that should be added to the form.
     */
    public abstract JPanel getComponent();
    
    /**
     * @return the data from the input.
     */
    public abstract String getData();
    
    /**
     * this is temporarily here because the input fields need to be listened to so the bibliography
     * & footnote preview can be updated whenever the input is updated.
     * @param dl Document Listener
     */
    public abstract void addDocListener(DocumentListener dl);
    
    /**
     * clears the input reseting its values to the default
     */
    public abstract void clear();
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setItalic(boolean italic){
        this.italic = italic;
    }
    
    public void setBold(boolean bold){
        this.bold = bold;
    }
    
    public String getName(){
        return name;
    }
    
    public boolean isItalic(){
        return italic;
    }
    
    public boolean isBold(){
        return bold;
    }
    
    public static boolean validateName(String name){
        return (name.indexOf(ReferenceCode.CONDITION_AND_OUTPUT_SEPARATOR)==-1 &&
                name.indexOf(ReferenceCode.MULTIPLE_CONDITIONS_OR)==-1 &&
                name.indexOf(ReferenceCode.MULTIPLE_CONDITIONS_AND)==-1 &&
                !name.contentEquals(ReferenceCode.TRUE_PLACE_HOLDER) &&
                !name.contentEquals(ReferenceCode.FALSE_PLACE_HOLDER) &&
                !name.contains("{") && !name.contains("}") &&
                !name.contains("(") && !name.contains(")") &&
                name.indexOf(ReferenceCode.IF)==-1 && 
                name.indexOf(ReferenceCode.NAME_REFERENCE)==-1);
    }
    
}
