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

import io.github.lal872k.pbib.ui.SourceManagerFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

/**
 * Basic <code>JTextField</code> input with a <code>JLabel</code> to describe what the input 
 * field is used for.
 * @author L. Arthur Lewis II
 */
public class TextInput extends CompInput{
    
    public static final int IMAGE_SIZE = 24;
    
    private final String title;
    private final String inputText;
    private final boolean showingHint;
    private final String description, example;
    
    private JTextField input;
    private JLabel inputTitle;
    private JButton hint;
    
    public TextInput(String title, String name){
        super(name);
        this.title = title;
        inputText = "";
        showingHint=false;
        description=null;
        example=null;
        init();
    }
    
    public TextInput(String title, String name, String inputText){
        super(name);
        this.title = title;
        this.inputText = inputText;
        showingHint=false;
        description=null;
        example=null;
        init();
    }
    
    public TextInput(String title, String name, String description, String example){
        super(name);
        this.title = title;
        inputText = "";
        showingHint=true;
        this.description=description;
        this.example=example;
        init();
    }
    
    public TextInput(String title, String name, String inputText, String description, String example){
        super(name);
        this.title = title;
        this.inputText = inputText;
        showingHint=true;
        this.description=description;
        this.example=example;
        init();
    }
    
    public final void init(){
        input = new JTextField(15);
        inputTitle = new JLabel(title+": ");
        input.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        inputTitle.setFont(SourceManagerFrame.UNIVERSAL_FONT);
        if (showingHint){
            ImageIcon hintImg = new ImageIcon(SourceManagerFrame.getFieldHint().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, 0)); 
            hint = new JButton(hintImg);
            hint.setBorderPainted(false);
            hint.setFocusPainted(false);
            hint.setContentAreaFilled(false);
            hint.setFocusable(false);
            hint.setToolTipText("Description & Example");
            hint.addActionListener(l -> {
                JOptionPane.showMessageDialog(hint, description+"\nExample: "+example, title, JOptionPane.INFORMATION_MESSAGE, hintImg);
            });
        }
    }

    @Override
    public final JPanel getComponent() {
        JPanel panel = new JPanel();
        panel.add(inputTitle);
        panel.add(input);
        if (showingHint){
            panel.add(hint);
        }
        return panel;
    }
    
    @Override
    public final void clear(){
        input.setText(inputText);
    }
    
    @Override
    public final String getData() {
        return input.getText();
    }
    
    @Override
    public final void addDocListener(DocumentListener dl){
        input.getDocument().removeDocumentListener(dl);
        input.getDocument().addDocumentListener(dl);
    }
    
}
