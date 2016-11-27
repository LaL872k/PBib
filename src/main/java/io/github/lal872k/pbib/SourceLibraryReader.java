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
import io.github.lal872k.pbib.ui.comps.DateInput;
import io.github.lal872k.pbib.ui.comps.TextInput;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * Used to read from sources.xml to load the <code>{@link lal.pbib.SourceLibrary}</code>.
 * @author L. Arthur Lewis II
 */
public class SourceLibraryReader {
    
    /**
     * loads the sources from the default location: project location /sources.xml
     * @return source library loaded from file
     * @throws ParsingException
     * @throws IOException 
     */
    public static SourceLibrary loadSourceLibrary() throws ParsingException, IOException{
        String decodedPath;
        
        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            decodedPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
        File file = new File(new File(decodedPath).getParent()+"/sources.xml");
        return loadSourceLibrary(file);
    }
    
    /**
     * loads the <code>{@link lal.pbib.SourceLibrary}</code> from the file indicated below.
     * @param file file to load sources from.
     * @return new source library loaded from file.
     * @throws ParsingException
     * @throws IOException 
     */
    public static SourceLibrary loadSourceLibrary(File file) throws ParsingException, IOException{
        BibConsole.println("Reading Source Library: "+file.getName());
        SourceLibrary lib = new SourceLibrary();
        
        Builder build = new Builder();
        Document doc = build.build(file);
        
        Element root = doc.getRootElement();
        
        // source categories
        BibConsole.debugln("Reading all Source Categories under the <cat> tag...");
        Elements sources = root.getChildElements("cat");
        for (int q = 0; q < sources.size(); q++){
            Element current = sources.get(q);
            // no title attribute
            if (current.getAttribute("title") == null){
                BibConsole.getConsole().printlnError("Source #"+(q+1)+" is missing a title attribute.");
                continue;
            }
            String title = current.getAttributeValue("title");
            
            // unique source types
            Elements types = current.getChildElements("source");
            // no types
            if (types.size() == 0){
                BibConsole.getConsole().printlnError("Source #"+(q+1)+" doesn't contain any source types.");
                continue;
            }
            
            BibConsole.debugln("Reading all the sources under the source category of "+title);
            ArrayList<Source> sourceTypes = new ArrayList();
            for (int w = 0; w < types.size(); w++){
                Element currentType = types.get(w);
                
                // title attribute
                if (currentType.getAttribute("title") == null){
                    BibConsole.getConsole().printlnError("Source Type #"+(w+1)+" in "+title+" is missing a title attribute.");
                    continue;
                }
                String typeTitle = currentType.getAttributeValue("title");
                
                // bib
                if (currentType.getChildElements("bib").size()==0){
                    BibConsole.getConsole().printlnError("Source Type "+typeTitle+" is missing a <bib> node.");
                    continue;
                }
                String bib = currentType.getFirstChildElement("bib").getAttributeValue("val");
                
                // make sure the code is valid
                if (ReferenceCode.validateCode(bib)!=null){
                    BibConsole.getConsole().printlnError("Source Type "+typeTitle+" has an invalid bibliography code: "+ReferenceCode.validateCode(bib));
                    continue;
                }
                
                // foot
                if (currentType.getChildElements("foot").size()==0){
                    BibConsole.getConsole().printlnError("Source Type "+typeTitle+" is missing a <foot> node.");
                    continue;
                }
                String foot = currentType.getFirstChildElement("foot").getAttributeValue("val");
                
                // make sure the code is valid
                if (ReferenceCode.validateCode(foot)!=null){
                    BibConsole.getConsole().printlnError("Source Type "+typeTitle+" has an invalid footnote code: "+ReferenceCode.validateCode(foot));
                    continue;
                }
                
                // source type instance
                Source sourceType = new Source(typeTitle, bib, foot);
                
                // input
                if (currentType.getChildElements("input").size()==0){
                    BibConsole.getConsole().printlnError("Source Type "+typeTitle+" is missing a <input> node.");
                    continue;
                }
                Element input = currentType.getFirstChildElement("input");
                
                // all the inputs
                Elements inputs = input.getChildElements();
                for (int e = 0; e < inputs.size(); e++){
                    Element inputNode = inputs.get(e);
                    String textTitle = inputNode.getAttributeValue("title");
                    String name = inputNode.getAttributeValue("name");
                    String italic = inputNode.getAttributeValue("italic");
                    String bold = inputNode.getAttributeValue("bold");
                    String desc = inputNode.getAttributeValue("desc");
                    String example = inputNode.getAttributeValue("ex");
                    
                    if (textTitle==null){
                        BibConsole.getConsole().printlnError("Input in "+typeTitle+" is missing a title value.");
                        continue;
                    }
                    
                    if (name==null){
                        BibConsole.getConsole().printlnError("Input "+textTitle+" is missing a name value.");
                        continue;
                    }
                    
                    if (!CompInput.validateName(name)){
                        BibConsole.getConsole().printlnError("Input "+textTitle+" is has an invalid name value.");
                        continue;
                    }
                    
                    CompInput in = null;
                    // when both the description and example are filled in
                    if (desc != null && example != null){
                        // text or link
                        if (inputNode.getLocalName().equals("text")){
                            in = new TextInput(textTitle, name, desc, example);
                        } else if (inputNode.getLocalName().equals("link")){
                            in = new TextInput(textTitle, name, desc, example);
                        }
                    } 
                    // when the description is used and it's a date
                    else if (desc != null && inputNode.getLocalName().equals("date")){
                        in = new DateInput(textTitle, name, desc);
                    }
                    // no description or example
                    else {
                        if (inputNode.getLocalName().equals("text")){
                            in = new TextInput(textTitle, name);
                        } else if (inputNode.getLocalName().equals("link")){
                            in = new TextInput(textTitle, name);
                        } else if (inputNode.getLocalName().equals("date")){
                            in = new DateInput(textTitle, name);
                        }
                    }
                    
                    if (bold!=null){
                        in.setBold(bold.toLowerCase().equals("true"));
                    }
                    if (italic!=null){
                        in.setItalic(italic.toLowerCase().equals("true"));
                    }
                    
                    sourceType.addInput(in);
                }
                
                BibConsole.println("Finished loading the contents of source: " + typeTitle);
                sourceTypes.add(sourceType);
            }
            lib.addSource(new SourceCategory(title, sourceTypes.toArray(new Source[0])));
        }
        lib.setInfo(getSourceLibraryInfo(root.getFirstChildElement("libinfo")));
        return lib;
    }
    
    /**
     * load the <code>{@link lal.pbib.SourceLibraryInfo}</code> from the root element.
     * @param root the root element of the source file.
     * @return info from file.
     */
    public static SourceLibraryInfo getSourceLibraryInfo(Element root){
        SourceLibraryInfo info = new SourceLibraryInfo();
        
        // title
        Element titleTag = root.getFirstChildElement("title");
        info.setTitle(titleTag.getAttributeValue("val"));
        
        // contributions
        Elements conts = root.getChildElements("contribution");
        for (int q = 0; q < conts.size(); q++){
            Element contTag = conts.get(q);
            Contribution cont = new Contribution();
            
            String title = contTag.getAttributeValue("title");
            cont.setTitle(title);
            
            Elements contrs = contTag.getChildElements("contributer");
            for (int w = 0; w < contrs.size(); w++){
                Element contrTag = contrs.get(w);
                
                cont.addContributer(contrTag.getAttributeValue("name"));
            }
            
            info.addContribution(cont);
        }
        
        // update history
        Element hisTag = root.getFirstChildElement("updatehistory");
        Elements updates = hisTag.getChildElements("update");
        for (int q = 0; q < updates.size(); q++){
            Element updateTag = updates.get(q);
            Update update = new Update();
            
            update.setRelease(updateTag.getAttributeValue("rel"));
            
            String title = updateTag.getAttributeValue("title");
            if (title==null){
                update.setTitle("New Update");
            } else {
                update.setTitle(title);
            
            }
            update.setVersion(updateTag.getAttributeValue("ver"));
            
            Elements bullets = updateTag.getChildElements("bullet");
            for (int w = 0; w < bullets.size(); w++){
                Element bulletTag = bullets.get(w);
                update.addBulletPoint(bulletTag.getAttributeValue("val"));
            }
            info.addUpdate(update);
        }
        
        // instructions
        Element instructions = root.getFirstChildElement("instructions");
        Elements paragraphs = instructions.getChildElements("para");
        for (int q = 0; q < paragraphs.size(); q++){
            Element paraTag = paragraphs.get(q);
            
            Paragraph para = new Paragraph();
            
            String alignment = paraTag.getAttributeValue("alignment");
            String font = paraTag.getAttributeValue("font");
            String text = paraTag.getAttributeValue("val");
            String indent = paraTag.getAttributeValue("indent");
            
            if (alignment!=null){
                para.setTextAlignment(alignment);
            }
            if (font!=null){
                para.setFontType(font);
            }
            if (text!=null){
                para.setText(text);
            }
            if (indent!=null){
                para.setTextIndent(Integer.parseInt(indent));
            }
            
            info.addParagraph(para);
        }
        
        return info;
    }
    
}
