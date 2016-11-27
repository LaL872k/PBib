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

import io.github.lal872k.pbib.Citation;
import io.github.lal872k.pbib.Footnote;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author L. Arthur Lewis II
 */
public final class MakeFootnoteFrame extends MakeFrame<Footnote[]>{
    
    public static final String WINDOW_TITLE = "Selecting Footnotes";
    
    private final String document;
    private final Citation[] cites;
    
    private final ArrayList<Footnote> footnotes;
    
    // swing stuff
    private JPanel doc;
    private JTextArea doc_text;
    private JScrollPane doc_scroll;
    private JPanel doc_bottom;
    private JLabel doc_pos;
    
    private JPanel doc_fn;
    
    private JPanel fn; // footnote
    private JList fn_list, fn_cites;
    private JPanel fn_bottom, fn_lists;
    private JButton fn_remove, fn_add;
    
    private JPanel bot;
    private JButton bot_finish;
    private JButton bot_cancel;
    
    public MakeFootnoteFrame(Component parentFrame, String document, Citation[] cites){
        super(parentFrame, WINDOW_TITLE);
        this.document = document;
        this.cites = cites;
        footnotes = new ArrayList();
    }
    
    @Override
    public void initializeComponents() {
        JPanel addpanel; // use for adding borders to panel before adding to parent
        GridBagConstraints bag = new GridBagConstraints();
        bag.insets = new Insets(5, 5, 5, 5);
        
        // main doc
        doc = new JPanel();
        doc.setLayout(new GridBagLayout());
        doc.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Document"));
        
        doc_pos = new JLabel();
        doc_pos.setName("0");
        doc_pos.setText("Position: 0");
        
        doc_text = new JTextArea();
        doc_text.setEditable(false);
        doc_text.setWrapStyleWord(true);
        doc_text.setLineWrap(true);
        doc_text.addCaretListener(l -> {
            doc_pos.setName(String.valueOf(l.getDot()));
            doc_pos.setText("Position: " + l.getDot());
        });
        doc_text.setText(document);
        doc_text.setCaret(new DefaultCaret(){
            @Override
            public void setSelectionVisible(boolean hasFocus) {
                super.setSelectionVisible(true);
            }
            
            @Override
            public void setVisible(boolean visible){
                super.setVisible(true);
            }
        });
        doc_text.getCaret().setVisible(true);
        
        doc_scroll = new JScrollPane(doc_text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        doc_bottom = new JPanel();
        doc_bottom.setLayout(new BorderLayout());
        
        doc_bottom.add(doc_pos, BorderLayout.EAST);
        
        addpanel = addComponent(doc_scroll, 0, 0, 1, 1, 1, 1, bag);
        doc.add(addpanel, bag);
        addpanel = addComponent(doc_bottom, 0, 1, 1, 1, 1, 0, bag);
        doc.add(addpanel, bag);
        
        // all current footnotes
        fn = new JPanel();
        fn.setLayout(new GridBagLayout());
        fn.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Footnotes / Citations"));
        
        fn_list = new JList();
        fn_list.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "All Footnotes"));
        
        fn_cites = new JList();
        fn_cites.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "All Citations"));
        String[] citeLists = new String[cites.length];
        for (int q = 0; q < cites.length; q++){
            Citation cite = cites[q];
            citeLists[q] = cite.getName();
        }
        fn_cites.setListData(citeLists);
        
        fn_lists = new JPanel();
        fn_lists.setLayout(new GridLayout(1, 2));
        fn_lists.add(fn_cites);
        fn_lists.add(fn_list);
        
        fn_bottom = new JPanel();
        
        fn_remove = new JButton();
        fn_remove.setText("Remove Footnote");
        fn_remove.addActionListener(l -> {
            if (fn_list.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "No footnote selected.", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            footnotes.remove(fn_list.getSelectedIndex());
            updateFootnoteList();
        });
        
        fn_add = new JButton();
        fn_add.setText("Add Footnote");
        fn_add.addActionListener(l -> {
            if (fn_cites.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "No citation selected.", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Footnote foot = new Footnote(cites[fn_cites.getSelectedIndex()], Integer.parseInt(doc_pos.getName()));
            footnotes.add(foot);
            updateFootnoteList();
        });
        
        fn_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        fn_bottom.setLayout(new BoxLayout(fn_bottom, BoxLayout.X_AXIS));
        fn_bottom.add(Box.createHorizontalGlue());
        fn_bottom.add(fn_add, BorderLayout.LINE_END);
        fn_bottom.add(Box.createRigidArea(new Dimension(5,0)));
        fn_bottom.add(fn_remove, BorderLayout.EAST);
        
        addpanel = addComponent(fn_lists, 0, 0, 1, 1, 1, 1, bag);
        fn.add(addpanel, bag);
        addpanel = addComponent(fn_bottom, 0, 1, 1, 1, 1, 0, bag);
        fn.add(addpanel, bag);
        
        doc_fn = new JPanel();
        doc_fn.setLayout(new GridLayout(2, 1));
        doc_fn.add(doc);
        doc_fn.add(fn);
        
        // bottom
        bot = new JPanel();
        
        bot_finish = new JButton();
        bot_finish.setText("Add Footnotes");
        bot_finish.addActionListener(l -> {
            if (footnotes.isEmpty()){
                JOptionPane.showMessageDialog(null, "You must have at least one footnote.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            setContents(footnotes.toArray(new Footnote[0]));
        });
        
        bot_cancel = new JButton();
        bot_cancel.setText("Cancel");
        bot_cancel.addActionListener(l -> {
            setContents(null);
        });
        
        bot.setBorder(new EmptyBorder(5, 5, 5, 5));
        bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
        bot.add(Box.createHorizontalGlue());
        bot.add(bot_finish, BorderLayout.LINE_END);
        bot.add(Box.createRigidArea(new Dimension(5,0)));
        bot.add(bot_cancel, BorderLayout.EAST);
        
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        addpanel = addComponent(doc_fn, 0, 0, 1, 1, 1, 1, bag);
        container.add(addpanel, bag);
        addpanel = addComponent(bot, 0, 1, 1, 1, 1, 0, bag);
        container.add(addpanel, bag);
    }
    
    @Override
    public void styleComponents() {
        doc_text.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        doc_pos.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        fn_cites.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        fn_add.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        fn_list.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        fn_remove.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        bot_finish.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        bot_cancel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        ((TitledBorder)doc.getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)fn.getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)fn_list.getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)fn_cites.getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
    }
    
    public void updateFootnoteList(){
        String[] listData = new String[footnotes.size()];
        for (int q = 0; q < footnotes.size(); q++){
            Footnote cite = footnotes.get(q);
            listData[q] = cite.getName()+" @ position:"+cite.getPosition();
        }
        fn_list.setListData(listData);
    }
    
}
