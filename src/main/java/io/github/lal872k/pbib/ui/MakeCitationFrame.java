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
import io.github.lal872k.pbib.Source;
import io.github.lal872k.pbib.SourceCategory;
import io.github.lal872k.pbib.SourceLibrary;
import io.github.lal872k.pbib.ui.comps.TextInput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author L. Arthur Lewis II
 */
public final class MakeCitationFrame extends MakeFrame<Citation> implements DocumentListener{
    
    public static final String FRAME_TITLE = "Create Citation";
    
    private final SourceLibrary library;
    private final String citeName;
    
    // source
    private JPanel source;
    private JComboBox sourceInput;
    
    // source type
    private JPanel sourceType;
    private JComboBox sourceTypeInput;
    
    // form
    private JPanel formSheet;
    private JPanel form;
    private JComboBox handSource; // primary or secondary
    private JLabel handDesc;
    
    // preview for bib and footnote
    private JLabel bibLabel, bibText;
    private JLabel footLabel, footText;
    private JButton copyFoot, copyBib;
    
    // enter
    private JPanel enter;
    private JButton addSource;
    private JButton cancel;
    
    public MakeCitationFrame(SourceLibrary library){
        super(null, FRAME_TITLE);
        this.library = library;
        this.citeName = "New Citation";
    }
    
    public MakeCitationFrame(SourceLibrary library, String citeName, Component parentWindow){
        super(parentWindow, FRAME_TITLE);
        this.library = library;
        this.citeName = citeName;
    }
    
    @Override
    public void initializeComponents(){
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // source
        sourceInput = new JComboBox();
        
        for (SourceCategory currentSource : library.getSources()){
            sourceInput.addItem(currentSource.getTitle());
        }
        sourceInput.addItemListener((e) -> {
            if (e.getStateChange()==ItemEvent.SELECTED){
                updateSourceTypes();
                updateWidthRestriction();
                updatePreview();
            }
        });
        source = new JPanel();
        source.add(sourceInput);
        
        JPanel sourcePanel = addComponent(source, 0, 0, 1, 1, .5d, 0, gbc);
        sourcePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Source Category"));
        container.add(sourcePanel, gbc);
        
        // source type
        sourceTypeInput = new JComboBox();
        updateSourceTypes();
        sourceTypeInput.addItemListener((e) -> {
            if (e.getStateChange()==ItemEvent.SELECTED){
                updateForm();
                updatePreview();
            }
        });
        sourceType = new JPanel();
        sourceType.add(sourceTypeInput);
        
        JPanel sourceTypePanel = addComponent(sourceType, 1, 0, 1, 1, .5d, 0, gbc);
        sourceTypePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Type of Source"));
        container.add(sourceTypePanel, gbc);
        
        // form
        form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        
        formSheet = new JPanel();
        formSheet.setLayout(new BoxLayout(formSheet, BoxLayout.Y_AXIS));
        
        handSource = new JComboBox(new String[]{"Primary", "Secondary"});
        handDesc = new JLabel("Primary/Seconday Source: ");
        JPanel handSourcePanel = new JPanel();
        handSourcePanel.add(handDesc);
        handSourcePanel.add(handSource);
        
        ImageIcon copyImg = new ImageIcon(SourceManagerFrame.getCopyTOClipboard().getScaledInstance(
                TextInput.IMAGE_SIZE, TextInput.IMAGE_SIZE, 0));
        
        bibLabel = new JLabel("Bibliography: ");
        bibText = new JLabel();
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
        
        form.add(formSheet);
        form.add(handSourcePanel);
        form.add(citationPreview);
        updateForm();
        
        JPanel formPanel = addComponent(form, 0, 1, 2, 1, 1, 1, gbc);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Form"));
        container.add(formPanel, gbc);
        
        // enter
        addSource = new JButton("Add Citation");
        addSource.addActionListener(l -> {
            Source current = getCurrentSource();
            Citation.TypeOfSource hand = Citation.TypeOfSource.valueOf(handSource.getSelectedItem().toString().toUpperCase());
            setContents(new Citation(citeName, current, hand));
        });
        cancel = new JButton("Cancel");
        cancel.addActionListener(l -> {
            setContents(null);
        });
        enter = new JPanel();
        
        enter.setBorder(new EmptyBorder(0, 0, 5, 5));
        enter.setLayout(new BoxLayout(enter, BoxLayout.X_AXIS));
        enter.add(Box.createHorizontalGlue());
        enter.add(addSource, BorderLayout.LINE_END);
        enter.add(Box.createRigidArea(new Dimension(5,0)));
        enter.add(cancel, BorderLayout.EAST);
        
        JPanel enterPanel = addComponent(enter, 0, 2, 2, 1, 1, 0, gbc);
        container.add(enterPanel, gbc);
        
        frame.getRootPane().setDefaultButton(addSource);
    }
    
    @Override
    public void showFrame(){
        super.showFrame();
        updateWidthRestriction();
    }
    
    @Override
    public void styleComponents() {
        // get the parent of the jpanel which is a jpanel also. Get the border which we know is a 
        // titled border. Set the font
        ((TitledBorder)((JPanel)source.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)((JPanel)sourceType.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)((JPanel)form.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        sourceInput.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        sourceTypeInput.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        addSource.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        cancel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        handDesc.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        handSource.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        bibLabel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        bibText.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        footLabel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        footText.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        updateForm();
    }
    
    public void updateSourceTypes(){
        sourceTypeInput.removeAllItems();
        if (sourceInput.getItemCount()>0){
            for (Source st : library.getSource(
                    sourceInput.getSelectedItem().toString()).getSourceTypes()){
                sourceTypeInput.addItem(st.getTitle());
            }
        }
    }
    
    /**
     * updates to form panel with the currently selected source.
     */
    public void updateForm(){
        formSheet.removeAll();
        if (sourceTypeInput.getItemCount()>0){
            Source currentSource = getCurrentSource();
            currentSource.clearForm();
            currentSource.fillForm(formSheet);
            currentSource.addDocumentListenerToInputs(this);
        }
        if (frame!=null){
            frame.revalidate();
            Dimension size = frame.getSize();
            frame.pack();
            if (frame.getSize().height<size.height){
                frame.setSize(size);
            }
        }
    }
    
    private Source getCurrentSource(){
        return library.getSource(sourceInput.getSelectedItem().toString())
                    .getSourceType(sourceTypeInput.getSelectedItem().toString());
    }
    
    private void updateWidthRestriction(){
        frame.setMinimumSize(new Dimension(frame.getPreferredSize().width, 0));
    }
    
    public void updatePreview(){
        bibText.setText(getCurrentSource().getBibCitationPreview());
        footText.setText(getCurrentSource().getFootnoteCitationPreview());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updatePreview();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updatePreview();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updatePreview();
    }
    
}
