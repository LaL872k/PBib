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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFootnote;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalAlignRun;

/**
 * Used for writing the footnotes and bibliography to the word document.
 * @author L. Arthur Lewis II
 */
public final class DocumentIO {
    
    private File file;
    
    private XWPFDocument doc;
    
    public DocumentIO(File file){
        this.file = file;
    }
    
    /**
     * loads the contents of the file into memory overriding any changes made before calling 
     * <code>{@link lal.pbib.DocumentIO#saveDocument()}</code>.
     * <p>
     * this should be run after the constructor.
     * @throws IOException reading issue.
     */
    public void loadDocument() throws IOException {
        doc = new XWPFDocument(new FileInputStream(file));
    }
    
    /**
     * saves the changes made to the file.
     * @throws IOException writing issue.
     */
    public void saveDocument() throws IOException {
        doc.write(new FileOutputStream(file));
    }
    
    /**
     * adds the bibliography to the document. 
     * @param cites citations to be added.
     */
    public void addBibliography(Citation[] cites) {
        ArrayList<Citation> firstHand = new ArrayList();
        ArrayList<Citation> secondHand = new ArrayList();
        for (Citation cite : cites){
            if (cite.getTypeOfSource().equals(Citation.TypeOfSource.PRIMARY)){
                firstHand.add(cite);
            } else {
                secondHand.add(cite);
            }
        }
        
        XWPFParagraph bibTitle = doc.createParagraph();
        bibTitle.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun bibTitle_text = bibTitle.createRun();
        bibTitle_text.setBold(true);
        bibTitle_text.setText("Bibliography");
        
        // spacing
        doc.createParagraph();
        
        if (firstHand.size()>0){
            XWPFParagraph fhTitle = doc.createParagraph();
            XWPFRun fhTitle_text = fhTitle.createRun();
            fhTitle_text.setBold(true);
            fhTitle_text.setUnderline(UnderlinePatterns.SINGLE);
            fhTitle_text.setText("Primary Sources");
            // spacing
            doc.createParagraph();
        }
        
        // organize by alphabetical
        Comparator<Citation> abc = (o1, o2) -> {
            String regex = "The\\s|A\\s|An\\s";
            String o1Text = o1.getSource().getBibCitationPreview().replaceAll(regex, "");
            String o2Text = o2.getSource().getBibCitationPreview().replaceAll(regex, "");
            int o1Index = 0, o2Index = 0;
            while (true){
                if (!Character.isLetter(o1Text.charAt(o1Index))){
                    o1Index++;
                    continue;
                }
                if (!Character.isLetter(o2Text.charAt(o2Index))){
                    o2Index++;
                    continue;
                }
                
                char o1Char = o1Text.charAt(o1Index);
                char o2Char = o2Text.charAt(o2Index);
                
                if (o1Char == o2Char){
                    return 0;
                }
                
                if (Character.toLowerCase(o1Char) < Character.toLowerCase(o2Char)){
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        
        Collections.sort(firstHand, abc);
        Collections.sort(secondHand, abc);
        
        for (int q = 0; q < firstHand.size(); q++){
            XWPFParagraph para = doc.createParagraph();
            para.setIndentationHanging(720);
            // setIndentationLeft() is more like addIndentationLeft()
            para.setIndentationLeft(720);
            insertRinParagraphInOrder(para.getCTP(), 
                    firstHand.get(q).getSource().getBibCitation().getRArray(), 0);
            if (q+1<firstHand.size()){
                // spacing
                doc.createParagraph();
            }
        }
        
        if (secondHand.size()>0){
            XWPFParagraph shTitle = doc.createParagraph();
            XWPFRun shTitle_text = shTitle.createRun();
            shTitle_text.setBold(true);
            shTitle_text.setUnderline(UnderlinePatterns.SINGLE);
            shTitle_text.setText("Secondary Sources");
            // spacing
            doc.createParagraph();
        }
        
        for (int q = 0; q < secondHand.size(); q++){
            XWPFParagraph para = doc.createParagraph();
            para.setIndentationHanging(720);
            para.setIndentationLeft(720);
            insertRinParagraphInOrder(para.getCTP(), 
                    secondHand.get(q).getSource().getBibCitation().getRArray(), 0);
            if (q+1<secondHand.size()){
                // spacing
                doc.createParagraph();
            }
        }
    }
    
    /**
     * calls the <code>{@link lal.pbib.DocumentIO#addFootnote(lal.BibMaker.Footnote)}</code>
     * on all of the footnotes to add them to the document.
     * @param footnotes footnotes to be added to the document.
     */
    public void addFootnote(Footnote[] footnotes) {
        checkFootnoteStatus();
        for (Footnote foot : footnotes){
            addFootnote(foot);
        }
    }
    
    /**
     * adds footnotes if they are not present. Adds three styles for the footnotes if they 
     * haven't already been added. Makes a footnote object. Add a footnote reference to that 
     * object.
     * @param footnote footnote object to be added to document.
     */
    public void addFootnote(Footnote footnote) {
        checkFootnoteStatus();
        BigInteger id = addFootnoteObject(footnote.getSource().getFootnoteCitation());
        addFootnoteReference(id, footnote.getPosition());
    }
    
    /**
     * adds the footnotes if they are not present. Adds the styles if they haven't been added
     * already.
     */
    public void checkFootnoteStatus(){
        addFootnotesIfAbsent();
        addFootnoteStylesIfAbsent();
    }
    
    /**
     * creates footnotes if they don't already exist.
     */
    public void addFootnotesIfAbsent(){
        if (doc.getFootnotes().isEmpty()){
            doc.createFootnotes();
        }
    }
    
    private BigInteger addFootnoteObject(CTP text) {
        CTFtnEdn main = CTFtnEdn.Factory.newInstance();
        
        BigInteger id = new BigInteger(String.valueOf(getOpenFootnoteID()));
        
        BibConsole.debugln("Adding footnote object with id:"+id);
        
        main.setId(id);
        CTP ctp = main.addNewP();
        
        ctp.addNewPPr().addNewPStyle().setVal("FootnoteText");
        
        CTR ctr = ctp.addNewR();
        ctr.addNewRPr().addNewRStyle().setVal("FootnoteReference");
        ctr.addNewFootnoteRef();
        
        for (CTR textCTR : text.getRArray()){
            for (CTText t : textCTR.getTArray()){
                t.setSpace(SpaceAttribute.Space.PRESERVE);
            }
        }
        
        insertRinParagraphInOrder(ctp, text.getRArray(), ctp.getRArray().length);
        
        doc.addFootnote(main);
        
        return id;
    }
    
    /*
    thought: is the length++ suposed to go after the para.
    */
    
    private void addFootnoteReference(BigInteger id, int pos) {
        int length = 0;
        
        CTR ref = CTP.Factory.newInstance().addNewR();
        CTRPr rpr = ref.addNewRPr();
        rpr.addNewRStyle().setVal("FootnoteReference");
        ref.addNewFootnoteReference().setId(id);
        
        for (int t = 0; t < doc.getParagraphs().size(); t++){
            XWPFParagraph current = doc.getParagraphs().get(t);
            // all of the r's
            for (int q = 0; q < current.getCTP().getRArray().length; q++){
                CTR currentCTR = current.getCTP().getRArray()[q];
                String rText = "";
                // the text inside the r (cttext)
                for (CTText ctText : currentCTR.getTArray()){
                    rText += ctText.getStringValue();
                }
                if (pos<=rText.length()+length){
                    // the length into the paragraph where we want to split it
                    int lengthIntoPara = pos-length;
                    
                    // the first half of the split
                    CTR half1 = CTR.Factory.newInstance();
                    CTText half1Text = half1.addNewT();
                    half1Text.setStringValue(rText.substring(0, lengthIntoPara));
                    half1Text.setSpace(SpaceAttribute.Space.PRESERVE);
                    
                    // the second half of the split
                    CTR half2 = CTR.Factory.newInstance();
                    CTText half2Text = half2.addNewT();
                    half2Text.setStringValue(rText.substring(lengthIntoPara, rText.length()));
                    half2Text.setSpace(SpaceAttribute.Space.PRESERVE);
                    
                    // get the current paragraph
                    CTP ctp = current.getCTP();

                    // remove the old r that the split is in
                    ctp.removeR(q);
                    
                    insertRinParagraph(ctp, new CTR[]{half2, ref, half1}, q);
                    return;
                } 
                // doesn't have any text
                else if (rText.length()==0 && pos<=length){
                    // get the current paragraph
                    CTP ctp = current.getCTP();

                    // remove the old r that the split is in
                    ctp.removeR(q);

                    insertRinParagraph(ctp, new CTR[]{ref}, q);
                    return;
                }
                // add length of text from the run
                length += rText.length();
            }
            // doesn't contain any r's
            if (current.getCTP().getRArray().length==0 && pos<=length){
                insertRinParagraph(current.getCTP(), new CTR[]{ref}, 0);
                return;
            }
            // add one because going to next paragraph
            length++;
        }
    }
    
    /**
     * inserts runs into a paragraph at the position indicated.
     * <p>
     * Note: the runs will be put in backwards, so if you need them added in the position 
     * they are given, then call 
     * <code>{@link lal.pbib.DocumentIO#insertRinParagraphInOrder(CTP, CTR[], int)}</code>
     * instead.
     * @param ctp paragraph that the runs are being added to.
     * @param newCTR runs to be added.
     * @param position position runs are to be added at.
     */
    public static void insertRinParagraph(CTP ctp, CTR[] newCTR, int position){
        // make a list with all the old r's
        ArrayList<CTR> ctrList = new ArrayList();
        for (CTR ctr : ctp.getRArray()){
            ctrList.add(ctr);
        }
        
        // add the new content
        for (CTR ctr : newCTR){
            ctrList.add(position, ctr);
        }

        // add r array to current paragraph
        ctp.setRArray(ctrList.toArray(new CTR[0]));
    }
    
    /**
     * inserts runs into a paragraph at the position indicated. Similar to 
     * <code>{@link lal.pbib.DocumentIO#insertRinParagraph(CTP, CTR[], int) }</code>, but
     * adds them in the order that they are given.
     * @param ctp paragraph that the runs are being added to.
     * @param newCTR runs to be added.
     * @param position position runs are to be added at.
     */
    public static void insertRinParagraphInOrder(CTP ctp, CTR[] newCTR, int position){
        // make a list with all the old r's
        ArrayList<CTR> ctrList = new ArrayList();
        for (CTR ctr : ctp.getRArray()){
            ctrList.add(ctr);
        }

        // add the new content
        for (int q = 0; q < newCTR.length; q++){
            ctrList.add(position+q, newCTR[q]);
        }

        // add r array to current paragraph
        ctp.setRArray(ctrList.toArray(new CTR[0]));
    }
    
    private long getOpenFootnoteID(){
        long minValue = 1;
        for (XWPFFootnote current : doc.getFootnotes()){
            long id = current.getCTFtnEdn().getId().longValue();
            if (id>=minValue){
                minValue=id+1l;
            }
        }
        return minValue;
    }
    
    public void addFootnoteStylesIfAbsent() {
        if (!footnoteStyleExists()){
            addFootnoteStyles();
        }
    }
    
    /**
     * @return true if all the styles have been added, false otherwise.
     */
    public boolean footnoteStyleExists() {
        return (doc.getStyles().styleExist("FootnoteReference") && 
                doc.getStyles().styleExist("FootnoteText") && 
                doc.getStyles().styleExist("FootnoteTextChar"));
    }
    
    private void addFootnoteStyles() {
        BibConsole.debugln("Adding word styles...");
        addFootnoteReferenceStyle();
        addFootnoteTextStyle();
        addFootnoteTextCharStyle();
        BibConsole.debugln("Completed adding word styles.");
    }
    
    private void addFootnoteReferenceStyle() {
        BibConsole.debugln("Adding word style \"FootnoteReference\"...");
        CTStyle style = CTStyle.Factory.newInstance();
        style.setStyleId("FootnoteReference");
        style.setType(STStyleType.CHARACTER);
        style.addNewName().setVal("footnote reference");
        style.addNewBasedOn().setVal("DefaultParagraphFont");
        style.addNewUiPriority().setVal(new BigInteger("99"));
        style.addNewSemiHidden();
        style.addNewUnhideWhenUsed();
        style.addNewRPr().addNewVertAlign().setVal(STVerticalAlignRun.SUPERSCRIPT);
        doc.getStyles().addStyle(new XWPFStyle(style));
        BibConsole.debugln("Completed adding word style \"FootnoteReference\".");
    }
    
    private void addFootnoteTextStyle(){
        BibConsole.debugln("Adding word style \"FootnoteText\"...");
        CTStyle style = CTStyle.Factory.newInstance();
        style.setType(STStyleType.PARAGRAPH);
        style.setStyleId("FootnoteText");
        style.addNewName().setVal("footnote text");
        style.addNewBasedOn().setVal("Normal");
        style.addNewLink().setVal("FootnoteTextChar");
        style.addNewUiPriority().setVal(new BigInteger("99"));
        style.addNewSemiHidden();
        style.addNewUnhideWhenUsed();
        CTRPr rpr = style.addNewRPr();
        rpr.addNewSz().setVal(new BigInteger("20"));
        rpr.addNewSzCs().setVal(new BigInteger("20"));
        doc.getStyles().addStyle(new XWPFStyle(style));
        BibConsole.debugln("Completed adding word style \"FootnoteText\".");
    }
    
    private void addFootnoteTextCharStyle() {
        BibConsole.debugln("Adding word style \"FootnoteTextChar\"...");
        CTStyle style  = CTStyle.Factory.newInstance();
        style.setCustomStyle(STOnOff.X_1);
        style.setStyleId("FootnoteTextChar");
        style.setType(STStyleType.CHARACTER);
        style.addNewName().setVal("Footnote Text Char");
        style.addNewBasedOn().setVal("DefaultParagraphFont");
        style.addNewLink().setVal("FootnoteText");
        style.addNewUiPriority().setVal(new BigInteger("99"));
        style.addNewSemiHidden();
        CTRPr rpr = style.addNewRPr();
        rpr.addNewSz().setVal(new BigInteger("20"));
        rpr.addNewSzCs().setVal(new BigInteger("20"));
        doc.getStyles().addStyle(new XWPFStyle(style));
        BibConsole.debugln("Completed adding word style \"FootnoteTextChar\".");
    }
    
    /**
     * @return the text of the document.
     */
    public String getText(){
        StringBuilder text = new StringBuilder();
        for (XWPFParagraph current : doc.getParagraphs()){
            for (CTR currentCTR : current.getCTP().getRArray()){
                for (CTText ctText : currentCTR.getTArray()){
                    text.append(ctText.getStringValue());
                }
            }
            text.append("\n");
        }
        // remove 1 because we will have one extra \n
        return text.substring(0, text.toString().length()-1);
    }
    
    /**
     * set the file that is used to save and load from.
     * @param file new file location.
     */
    public void setFile(File file){
        this.file = file;
    }
    
    /**
     * @return the file that is used for saving and loading.
     */
    public File getFile(){
        return file;
    }
    
}
