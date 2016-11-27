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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author L. Arthur Lewis II
 */
public class MakeBibFrame extends MakeFrame<Citation[]>{
    
    public static final String WINDOW_TITLE = "Add Bibliography";
    
    private final Citation[] cites;
    
    // ui
    private JPanel contents;
    private JLabel instructions;
    private JPanel instructionsPanel;
    private JCheckBox[] boxes;
    
    private JPanel options;
    private JButton save, cancel;
    
    public MakeBibFrame(Citation[] cites, Component parentFrame) {
        super(parentFrame, WINDOW_TITLE);
        this.cites = cites;
    }
    
    public MakeBibFrame(Citation[] cites) {
        super(null, WINDOW_TITLE);
        this.cites = cites;
    }

    @Override
    public void initializeComponents() {
        GridBagConstraints bag = new GridBagConstraints();
        bag.insets = new Insets(5, 5, 5, 5);
        
        // main content
        contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        
        instructions = new JLabel();
        instructions.setText("Select the citations to include in your bibliography...");
        
        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        instructionsPanel.add(instructions);
        
        contents.add(instructionsPanel);
        
        boxes = new JCheckBox[cites.length];
        for (int q = 0; q < boxes.length; q++){
            boxes[q] = new JCheckBox();
            boxes[q].setText(cites[q].getName());
            boxes[q].setSelected(true);
            boxes[q].setToolTipText(cites[q].getSource().getBibCitationPreview());
            
            JPanel boxPanel = new JPanel();
            boxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            boxPanel.add(boxes[q]);
            contents.add(boxPanel);
        }
        
        // options - save / cancel
        options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
        
        save = new JButton();
        save.setText("Add Bibliography");
        
        save.addActionListener(l -> {
            LinkedList<Citation> checkedCitations = new LinkedList();
            for (int q = 0; q < boxes.length; q++){
                if (boxes[q].isSelected()){
                    checkedCitations.add(cites[q]);
                }
            }
            if (checkedCitations.isEmpty()){
                JOptionPane.showMessageDialog(null, "You must have at least one citation selected", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            setContents(checkedCitations.toArray(new Citation[0]));
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
        
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        
        container.add(addComponent(contents, 0, 0, 1, 1, 1, 1, bag), bag);
        container.add(addComponent(options, 0, 1, 1, 1, 1, 0, bag), bag);
        
        frame.getRootPane().setDefaultButton(save);
    }

    @Override
    public void styleComponents() {
        save.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        cancel.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        for (JCheckBox box : boxes){
            box.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        }
        instructions.setFont(SourceManagerFrame.UNIVERSAL_FONT);
    }
    
}
