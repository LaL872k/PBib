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
package io.github.lal872k.pbib.ui;

import io.github.lal872k.pbib.Contribution;
import io.github.lal872k.pbib.Paragraph;
import io.github.lal872k.pbib.SourceLibraryInfo;
import io.github.lal872k.pbib.Update;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.*;

/**
 *
 * @author L. Arthur Lewis II
 */
public class LibraryInfoFrame extends MakeFrame{
    
    private JPanel main;
    private JScrollPane main_scroll;
    private JEditorPane main_text;
    
    private JPanel bot;
    private JButton bot_close;
    
    private final SourceLibraryInfo info;
    
    public LibraryInfoFrame(SourceLibraryInfo info){
        super(null);
        this.info = info;
    }
    
    @Override
    public void initializeComponents() {
        GridBagConstraints bag = new GridBagConstraints();
        bag.insets = new Insets(5, 5, 5, 5);
        
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        
        main = new JPanel();
        main.setLayout(new GridLayout());
        
        main_text = new JEditorPane();
        main_text.setContentType("text/html");
        main_text.setEditable(false);
        
        main_scroll = new JScrollPane(main_text, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        main.add(main_scroll);
        
        addInfo();
        
        bot = new JPanel();
        bot.setLayout(new BorderLayout());
        
        bot_close = new JButton();
        bot_close.setText("Close");
        bot_close.addActionListener(l -> {
            frame.dispose();
        });
        
        bot.add(bot_close, BorderLayout.EAST);
        
        container.add(MakeFrame.addComponent(main_scroll, 0, 0, 1, 1, 1, 1, bag), bag);
        container.add(MakeFrame.addComponent(bot, 0, 1, 1, 1, 0, 0, bag), bag);
        
        frame.getRootPane().setDefaultButton(bot_close);
        frame.setPreferredSize(new Dimension(600, 700));
    }
    
    private void addInfo(){
        
        StringBuilder html = new StringBuilder();
        
        html.append(addParagraph(new Paragraph(info.getTitle(), 
                Paragraph.TextAlignment.CENTER, Paragraph.FontType.HEADER1)));
        
        html.append(addParagraph(new Paragraph("Contributions:", 
                Paragraph.TextAlignment.LEFT, Paragraph.FontType.HEADER2)));
        
        for (Contribution cont : info.getContributionsArray()){
            html.append(addParagraph(new Paragraph(cont.getTitle()+":", 
                    Paragraph.TextAlignment.LEFT, Paragraph.FontType.HEADER2, 1)));
            for (String contr : cont.getContributersArray()){
                html.append(addParagraph(new Paragraph("- "+contr, 
                    Paragraph.TextAlignment.LEFT, Paragraph.FontType.REG, 4)));
            }
        }
        
        html.append(addParagraph(new Paragraph("Developer Notes:", 
                Paragraph.TextAlignment.LEFT, Paragraph.FontType.HEADER2)));
        
        for (Paragraph para : info.getParagraphsArray()){
            html.append(addParagraph(para));
        }
        
        html.append(addParagraph(new Paragraph("Update History", 
                    Paragraph.TextAlignment.LEFT, Paragraph.FontType.HEADER2)));
        for (Update update : info.getUpdatesArray()){
            html.append(addParagraph(new Paragraph(update.getTitle() 
                    + " ver:" + update.getVersion() + " release:" + update.getRelease(), 
                    Paragraph.TextAlignment.LEFT, Paragraph.FontType.HEADER3, 2)));
            for (String bullet : update.getBulletPointsArray()){
                html.append(addParagraph(new Paragraph("- " + bullet, 
                    Paragraph.TextAlignment.LEFT, Paragraph.FontType.REG, 4)));
            }
        }
        
        main_text.setText("<html>"+html.toString()+"</html>");
    }
    
    private String addParagraph(Paragraph para){
        return ("<p style=\"text-indent:"+
                (main_text.getFontMetrics(para.getFontType().getFont()).charWidth(' ')
                *para.getTextIndent())
                +"px;text-align:"+
                para.getTextAlignment().getStyleValue()
                +";"+
                para.getFontType().getFontCSSText()
                +"\">"+
                para.getText()
                +"</p>");
    }
    
    @Override
    public void styleComponents() {
        bot_close.setFont(SourceManagerFrame.UNIVERSAL_FONT);
    }
    
}
