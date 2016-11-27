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
 * Contains the info about <code>{@link lal.pbib.SourceLibrary}</code> and displayed on startup.
 * @author L. Arthur Lewis II
 */
public class SourceLibraryInfo {
    
    private String title;
    
    private final ArrayList<Contribution> contributions;
    
    private final ArrayList<Paragraph> instructions;
    
    private final ArrayList<Update> updates;
    
    public SourceLibraryInfo(){
        contributions = new ArrayList();
        instructions = new ArrayList();
        updates = new ArrayList();
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void addContribution(Contribution contribution){
        contributions.add(contribution);
    }
    
    public void removeContribution(Contribution contribution){
        contributions.remove(contribution);
    }
    
    public void addParagraph(Paragraph para){
        instructions.add(para);
    }
    
    public void removeParagraph(Paragraph para){
        instructions.remove(para);
    }
    
    public void addUpdate(Update update){
        updates.add(update);
    }
    
    public void removeUpdate(Update update){
        updates.remove(update);
    }
    
    public String getTitle(){
        return title;
    }
    
    public List<Contribution> getContributions(){
        return contributions;
    }
    
    public Contribution[] getContributionsArray(){
        return contributions.toArray(new Contribution[0]);
    }
    
    public List<Paragraph> getParagraphs(){
        return instructions;
    }
    
    public Paragraph[] getParagraphsArray(){
        return instructions.toArray(new Paragraph[0]);
    }
    
    public List<Update> getUpdates(){
        return updates;
    }
    
    public Update[] getUpdatesArray(){
        return updates.toArray(new Update[0]);
    }
    
}
