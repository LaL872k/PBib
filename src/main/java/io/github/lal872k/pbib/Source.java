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

import io.github.lal872k.pbib.ui.comps.CompInput;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 * This is the lowest class for organization of sources, and contains the code for formating 
 * the bibliography and footnotes. This is organized by  
 * <code>{@link lal.pbib.SourceCategory}</code>.
 * @author L. Arthur Lewis II
 */
public class Source {
    
    private final String title, bibCode, footCode;
    
    private final ArrayList<CompInput> inputs;
    
    public Source(String title, String bibCode, String footCode){
        this.title = title;
        this.bibCode = bibCode;
        this.footCode = footCode;
        inputs = new ArrayList();
    }
    
    public void addInput(CompInput in){
        inputs.add(in);
    }
    
    public void removeInput(CompInput in){
        inputs.remove(in);
    }
    
    public int getInputsLength(){
        return inputs.size();
    }
    
    /**
     * clears all the inputs by calling <code>{@link lal.pbib.ui.comps.CompInput#clear()}</code>
     * , this usually involves clearing the text or just reseting to default.
     */
    public void clearForm(){
        for (CompInput in : inputs){
            in.clear();
        }
    }
    
    /**
     * adds all of the inputs to a jpanel.
     * @param panel panel to add the inputs to.
     */
    public void fillForm(JPanel panel){
        for (CompInput in : inputs){
            panel.add(in.getComponent());
        }
    }
    
    /**
     * adds a <code>DocumentListener</code> to all the inputs
     * @param dl <code>DocumentListener</code> to add to the inputs
     */
    public void addDocumentListenerToInputs(DocumentListener dl){
        for (CompInput in : inputs){
            in.addDocListener(dl);
        }
    }
    
    /**
     * title of the source ex.: Online Article
     * @return title of the source.
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * the code for the bibliography that contains references to inputs.
     * @return the code for the bibliography
     */
    public String getBibCode(){
        return bibCode;
    }
    
    /**
     * the code for the footnotes that contains references to inputs.
     * @return the code for the footnote
     */
    public String getFootCode(){
        return footCode;
    }
    
    /**
     * gets the paragraph for the bibliography by using the bib code and the input fields.
     * @return word paragraph.
     */
    public CTP getBibCitation(){
        return ReferenceCode.getCitation(bibCode, inputs.toArray(new CompInput[0]));
    }
    
    /**
     * gets the paragraph for the footnote by using the foot code and the input fields.
     * @return word paragraph.
     */
    public CTP getFootnoteCitation(){
        return ReferenceCode.getCitation(footCode, inputs.toArray(new CompInput[0]));
    }
    
    /**
     * @return <code>String</code> version of 
     * <code>{@link lal.pbib.Source#getBibCitation()}</code>.
     */
    public String getBibCitationPreview(){
        return getCitationPreview(getBibCitation());
    }
    
    /**
     * @return <code>String</code> version of 
     * <code>{@link lal.pbib.Source#getFootnoteCitation()}</code>
     */
    public String getFootnoteCitationPreview(){
        return getCitationPreview(getFootnoteCitation());
    }
    
    private String getCitationPreview(CTP ctp){
        String preview = "";
        for (CTR ctr : ctp.getRArray()){
            for (CTText t : ctr.getTArray()){
                preview += t.getStringValue();
            }
        }
        return preview;
    }
    
    
    
    /**
     * @return a deep copy of the instance.
     */
    public Source getDeepCopy(){
        String title = getTitle();
        String bibCode = getBibCode();
        String footCode = getFootCode();
        Source newSource = new Source(title, bibCode, footCode);
        for (CompInput input : inputs){
            newSource.addInput(input);
        }
        return newSource;
    }
    
}
