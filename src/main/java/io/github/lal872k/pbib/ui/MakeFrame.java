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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Functions as a Popup menu were its main objective is to obtain a piece of info, in this case
 * <code>T</code>, and return it after a button is clicked.
 * @author L. Arthur Lewis II
 * @param <T> the return type.
 */
public abstract class MakeFrame<T> {
    
    public static final String DEFAULT_FRAME_TITLE = "Retriever Window";
    private final String initialTitle;
    
    private T t;
    
    private final Component parentFrame;
    
    protected JFrame frame;
    protected JPanel container;
    
    public MakeFrame(Component parentFrame){
        this.parentFrame = parentFrame;
        initialTitle = DEFAULT_FRAME_TITLE;
    }
    
    public MakeFrame(Component parentFrame, String initialTitle){
        this.parentFrame = parentFrame;
        if (initialTitle!=null){
            this.initialTitle = initialTitle;
        } else {
            this.initialTitle = DEFAULT_FRAME_TITLE;
        }
    }
    
    public abstract void initializeComponents();
    public abstract void styleComponents();
    
    public void initializeFrame(){
        frame = new JFrame();
        frame.setTitle(initialTitle);
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                setContents(null);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                setContents(null);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }
    
    public void showFrame(){
        if (container==null){
            container = new JPanel();
        }
        frame.setContentPane(container);
        frame.pack();
        frame.setLocationRelativeTo(parentFrame);
        SourceManagerFrame.addOpenConsole(frame);
        frame.setVisible(true);
    }
    
    public final void openFrame(){
        initializeFrame();
        initializeComponents();
        styleComponents();
        showFrame();
    }
    
    public final void dispose(){
        frame.dispose();
    }
    
    public synchronized final T getContents() throws InterruptedException{
        this.wait();
        return t;
    }
    
    protected synchronized final void setContents(T t){
        this.t = t;
        this.notify();
    }
    
    protected final Component getParentFrame(){
        return parentFrame;
    }
    
    public final static JPanel addComponent(Component comp, int x, int y, int width, int height, 
            double weightx, double weighty, GridBagConstraints bag){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        bag.gridx = x;
        bag.gridy = y;
        bag.gridwidth = width;
        bag.gridheight = height;
        bag.fill = GridBagConstraints.BOTH;
        bag.weightx = weightx;
        bag.weighty = weighty;
        panel.add(comp);
        return panel;
    }
    
}
