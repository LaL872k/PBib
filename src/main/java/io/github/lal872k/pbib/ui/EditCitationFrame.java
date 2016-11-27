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
import io.github.lal872k.pbib.ui.comps.TextInput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author L. Arthur Lewis II
 */
public class EditCitationFrame extends MakeFrame<Citation> implements DocumentListener{
    
    public static final String WINDOW_TITLE = "Edit Citation";
    
    // main content
    private JPanel contents;
    private JPanel contentsParent;
    private JPanel formSheet;
    private JTextField name;
    private JLabel name_title;
    private JComboBox hand;
    private JLabel hand_title;
    
    // preview for bib and footnote
    private JLabel bibLabel, bibText;
    private JLabel footLabel, footText;
    private JButton copyFoot, copyBib;
    
    // save and cancel
    private JPanel options;
    private JButton save, cancel;
    
    private final Citation cite;
    
    public EditCitationFrame(Component parentFrame, Citation cite) {
        super(parentFrame, WINDOW_TITLE);
        this.cite = cite;
    }
    
    @Override
    public void initializeComponents() {
        GridBagConstraints bag = new GridBagConstraints();
        bag.insets = new Insets(5, 5, 5, 5);
        
        contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        
        contentsParent = new JPanel();
        contentsParent.setLayout(new GridBagLayout());
        
        formSheet = new JPanel();
        formSheet.setLayout(new BoxLayout(formSheet, BoxLayout.Y_AXIS));
        
        name_title = new JLabel();
        name_title.setText("Name: ");
        
        name = new JTextField();
        name.setText(cite.getName());
        name.setColumns(15);
        
        hand_title = new JLabel();
        hand_title.setText("Primary/Secondary Source: ");
        
        hand = new JComboBox();
        hand.addItem("Primary");
        hand.addItem("Secondary");
        
        hand.setSelectedItem(cite.getTypeOfSource().toString().substring(0, 1)+cite.getTypeOfSource().toString().substring(1).toLowerCase());
        
        ImageIcon copyImg = new ImageIcon(SourceManagerFrame.getCopyTOClipboard().getScaledInstance(
                TextInput.IMAGE_SIZE, TextInput.IMAGE_SIZE, 0));
        
        bibLabel = new JLabel("Bibliography: ");
        bibText = new JLabel();
        bibText.setText(cite.getSource().getBibCitationPreview());
        copyBib = new JButton(copyImg);
        copyBib.setBorderPainted(false);
        copyBib.setFocusPainted(false);
        copyBib.setContentAreaFilled(false);
        copyBib.setFocusable(false);
        copyBib.setToolTipText("Copy bibliography to clipboard");
        copyBib.addActionListener(l -> {
            StringSelection text = new StringSelection(bibText.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, text);
            JOptionPane.showMessageDialog(copyBib, "Copied bibliography to clipboard.");
        });
        
        JPanel bibPreview = new JPanel();
        bibPreview.add(bibLabel);
        bibPreview.add(bibText);
        bibPreview.add(copyBib);
        
        footLabel = new JLabel("Footnote: ");
        footText = new JLabel();
        footText.setText(cite.getSource().getFootnoteCitationPreview());
        copyFoot = new JButton(copyImg);
        copyFoot.setBorderPainted(false);
        copyFoot.setFocusPainted(false);
        copyFoot.setContentAreaFilled(false);
        copyFoot.setFocusable(false);
        copyFoot.setToolTipText("Copy footnote to clipboard");
        copyFoot.addActionListener(l -> {
            StringSelection text = new StringSelection(footText.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, text);
            JOptionPane.showMessageDialog(copyFoot, "Copied footnote to clipboard.");
        });
        
        JPanel footPreview = new JPanel();
        footPreview.add(footLabel);
        footPreview.add(footText);
        footPreview.add(copyFoot);
        
        JPanel citationPreview = new JPanel();
        citationPreview.setLayout(new BoxLayout(citationPreview, BoxLayout.Y_AXIS));
        citationPreview.add(bibPreview);
        citationPreview.add(footPreview);
        
        // bottom menu
        
        options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
        
        save = new JButton();
        save.setText("Save");
        save.addActionListener(l -> {
            setContents(new Citation(name.getText(), cite.getSource(),
                    Citation.TypeOfSource.valueOf(
                        hand.getSelectedItem().toString().toUpperCase())));
        });
        
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.addActionListener(l -> {
            setContents(null);
        });
        
        options.add(Box.createHorizontalGlue());
        options.add(save, BorderLayout.EAST);
        options.add(Box.createRigidArea(new Dimension(5, 0)));
        options.add(cancel, BorderLayout.LINE_END);
        
        // adding components to parents
        
        JPanel section = getNewSection();
        
        section.add(name_title);
        section.add(name);
        
        contents.add(section);
        
        contents.add(formSheet);
        
        section = getNewSection();
        
        section.add(hand_title);
        section.add(hand);
        
        contents.add(section);
        
        contents.add(citationPreview);
        
        contentsParent.add(contents);
        
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        
        JPanel sourcePanel = MakeFrame.addComponent(contentsParent, 0, 0, 1, 1, 1, 1, bag);
        sourcePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Source Data"));
        container.add(sourcePanel, bag);
        
        JPanel optionsPanel = MakeFrame.addComponent(options, 0, 1, 1, 1, 1, 0, bag);
        container.add(optionsPanel, bag);
        
        // update the form
        formSheet.removeAll();
        cite.getSource().fillForm(formSheet);
        cite.getSource().addDocumentListenerToInputs(this);
        if (frame!=null){
            frame.revalidate();
            Dimension size = frame.getSize();
            frame.pack();
            if (frame.getSize().height<size.height){
                frame.setSize(size);
            }
        }
        
        frame.getRootPane().setDefaultButton(save);
    }
    
    private JPanel getNewSection(){
        JPanel section = new JPanel();
        section.setLayout(new FlowLayout());
        //section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

    @Override
    public void styleComponents() {
        ((TitledBorder)((JPanel)contentsParent.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        name.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        name_title.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        hand.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        hand_title.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        save.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        cancel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        bibLabel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        bibText.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        footLabel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        footText.setFont(SourceManagerFrame.UNIVERSAL_FONT);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        bibText.setText(cite.getSource().getBibCitationPreview());
        footText.setText(cite.getSource().getFootnoteCitationPreview());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        bibText.setText(cite.getSource().getBibCitationPreview());
        footText.setText(cite.getSource().getFootnoteCitationPreview());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        bibText.setText(cite.getSource().getBibCitationPreview());
        footText.setText(cite.getSource().getFootnoteCitationPreview());
    }
    
}
