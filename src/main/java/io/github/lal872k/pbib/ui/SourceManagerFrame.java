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

import io.github.lal872k.console.extensions.command.Command;
import io.github.lal872k.pbib.BibCitationPicker;
import io.github.lal872k.pbib.BibConsole;
import io.github.lal872k.pbib.Citation;
import io.github.lal872k.pbib.CitationEditor;
import io.github.lal872k.pbib.CitationRetriever;
import io.github.lal872k.pbib.DocumentIO;
import io.github.lal872k.pbib.FootnoteRetriever;
import io.github.lal872k.pbib.Retriever;
import io.github.lal872k.pbib.SourceLibrary;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author L. Arthur Lewis II
 */
public final class SourceManagerFrame extends MakeFrame{
    
    public static final Font UNIVERSAL_FONT = new Font(Font.DIALOG, Font.PLAIN, 15);
    public static final String TITLE = "PBib";
    private static Image FIELD_HINT, COPY_TO_CLIPBOARD;
    
    // ui
    private JPanel fileSelection;
    private JLabel fileName;
    private JButton chooseFile;
    private File file;
    
    private JPanel allCitations;
    private JPanel citationUtil;
    private JButton addCitation, removeCitation, editCitation;
    private JList citations;
    private JScrollPane citationScroll;
    
    private JPanel addCitations;
    private JButton addBibliography, addFootnotes;
    
    private JPanel devDetails;
    private JLabel devName;
    private static final String DEV_NAME = "L. Arthur Lewis II 2016";
    
    // other stuff
    private final ArrayList<Citation> cites;
    private final SourceLibrary library;
    
    public SourceManagerFrame(SourceLibrary library){
        super(null, TITLE);
        cites = new ArrayList();
        this.library = library;
        openFrame();
        BibConsole.getCommandHandler().addCommand(new Command("libinfo", (console, arguments) -> {
            console.println("Showing the library info screen...");
            showLibraryInfo();
        }));
    }
    
    @Override
    public void initializeComponents() {
        // main content pane of frame
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        bag.insets = new Insets(5, 5, 5, 5);
        
        // first panel
        fileSelection = new JPanel();
        fileSelection.setLayout(new BoxLayout(fileSelection, BoxLayout.X_AXIS));
        
        fileName = new JLabel("No File Selected");
        chooseFile = new JButton("Select File");
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent l) {
                JFileChooser chooser = new JFileChooser();
                // make it word only
                chooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (!pathname.isFile() || (pathname.getName().endsWith(".doc") || 
                                pathname.getName().endsWith(".docx"))){
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "Word Documents (*.doc and *.docx)";
                    }
                });
                chooser.setAcceptAllFileFilterUsed(false);
                int returnval = chooser.showDialog(null, "Select");
                if (returnval == JFileChooser.APPROVE_OPTION){
                    file = chooser.getSelectedFile();
                    fileName.setText(file.getName());
                }
            }
        });
        fileSelection.setBorder(new EmptyBorder(0, 5, 5, 5));
        fileSelection.add(Box.createHorizontalGlue());
        fileSelection.add(fileName, BorderLayout.EAST);
        fileSelection.add(Box.createRigidArea(new Dimension(5, 0)));
        fileSelection.add(chooseFile, BorderLayout.LINE_END);
        
        // second panel
        allCitations = new JPanel();
        allCitations.setLayout(new GridBagLayout());
        
        // add, remove, and edit buttons
        citationUtil = new JPanel();
        citationUtil.setLayout(new BoxLayout(citationUtil, BoxLayout.X_AXIS));
        citationUtil.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        addCitation = new JButton("Add Citation");
        addCitation.addActionListener(l -> {
            // without thread the components inside citeframe are not visible
            new Thread(() -> {
                CitationRetriever citeGetter = new CitationRetriever(library, frame);
                frame.setVisible(false);
                if (citeGetter.retrieve() == Retriever.ReturnStatus.SELECTED){
                    cites.add(citeGetter.getContents());
                    updateSourcesList();
                }
                frame.setVisible(true);
            }).start();
        });
        
        editCitation = new JButton("Edit Citation");
        editCitation.addActionListener(l -> {
            new Thread(() -> {
                CitationEditor citeEditor = new CitationEditor(frame, cites.get(citations.getSelectedIndex()));
                frame.setVisible(false);
                if (citeEditor.retrieve() == Retriever.ReturnStatus.SELECTED){
                    cites.set(citations.getSelectedIndex(), citeEditor.getContents());
                    updateSourcesList();
                }
                frame.setVisible(true);
            }).start();
        });
        
        removeCitation = new JButton("Remove Citation");
        
        removeCitation.addActionListener(l -> {
            cites.remove(citations.getSelectedIndex());
            updateSourcesList();
        });
        
        
        citationUtil.add(Box.createHorizontalGlue());
        citationUtil.add(editCitation, BorderLayout.EAST);
        citationUtil.add(Box.createRigidArea(new Dimension(5, 0)));
        citationUtil.add(addCitation, BorderLayout.EAST);
        citationUtil.add(Box.createRigidArea(new Dimension(5, 0)));
        citationUtil.add(removeCitation, BorderLayout.EAST);
        
        // the main list of all sources
        citations = new JList();
        updateSourcesList();
        citationScroll = new JScrollPane(citations, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        allCitations.add(MakeFrame.addComponent(citationScroll, 0, 0, 1, 1, 1, 1, bag), bag);
        allCitations.add(MakeFrame.addComponent(citationUtil, 0, 1, 1, 1, 1, 0, bag), bag);
        
        // third panel
        addCitations = new JPanel();
        addCitations.setLayout(new BoxLayout(addCitations, BoxLayout.X_AXIS));
        addCitations.setBorder(new EmptyBorder(0, 5, 5, 5));
        
        addBibliography = new JButton("Add Bibliography");
        addBibliography.addActionListener(l -> {
            new Thread(() -> {
                // check for a selected file
                if (file==null){
                    JOptionPane.showMessageDialog(null, "No file selected", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // make sure there is a citaion
                if (cites.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No citations added yet (click add citation to do so).", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // get the citation they want
                frame.setVisible(false);
                BibCitationPicker bcp = new BibCitationPicker(cites.toArray(new Citation[0]), frame);
                Citation[] bibCites = null;
                bcp.retrieve();
                if (bcp.retrieve()==Retriever.ReturnStatus.SELECTED){
                    bibCites = bcp.getContents();
                } else {
                    frame.setVisible(true);
                    return;
                }
                frame.setVisible(true);

                // load the document
                DocumentIO dio = new DocumentIO(file);
                try {
                    dio.loadDocument();
                } catch (IOException ex) {
                    BibConsole.getConsole().printlnError("Failed to load file. Error Message: "+ex.getMessage());
                    BibConsole.getConsole().show();
                    ex.printStackTrace();
                    return;
                }

                // add the bibliography
                dio.addBibliography(bibCites);

                // save to the document
                try {
                    dio.saveDocument();
                } catch (IOException ex) {
                    BibConsole.getConsole().printlnError("Failed to save file. Error Message: "+ex.getMessage());
                    BibConsole.getConsole().show();
                    ex.printStackTrace();
                    return;
                }
            }).start();
        });
        
        addFootnotes = new JButton("Add Footnotes");
        addFootnotes.addActionListener(l -> {
            new Thread(() -> {
                // check for file
                if (file==null){
                    JOptionPane.showMessageDialog(null, "No file selected", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // make sure there is a citaion
                if (cites.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No citations added yet (click add citation to do so).", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                DocumentIO dio = new DocumentIO(file);
                try {
                    dio.loadDocument();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    BibConsole.getConsole().printlnError("Failed to load file. Error Message: "+ex.getMessage());
                    JOptionPane.showMessageDialog(null, "There was an io issue with the file selected", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                frame.setVisible(false);
                FootnoteRetriever fr = new FootnoteRetriever(dio.getText(), cites.toArray(new Citation[0]), frame);
                if (fr.retrieve()==Retriever.ReturnStatus.SELECTED){
                    dio.addFootnote(fr.getContents());
                    try {
                        dio.saveDocument();
                    } catch (IOException ex) {
                        BibConsole.getConsole().printlnError("Failed to save file. Error Message: "+ex.getMessage());
                        JOptionPane.showMessageDialog(null, "There was an io issue with the file selected", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
                frame.setVisible(true);
            }).start();
        });
        
        addCitations.add(Box.createHorizontalGlue());
        addCitations.add(addBibliography, BorderLayout.EAST);
        addCitations.add(Box.createRigidArea(new Dimension(5, 0)));
        addCitations.add(addFootnotes, BorderLayout.LINE_END);
        
        devDetails = new JPanel();
        devDetails.setLayout(new BoxLayout(devDetails, BoxLayout.X_AXIS));
        
        devName = new JLabel();
        devName.setText(DEV_NAME);
        
        devDetails.add(Box.createHorizontalGlue());
        devDetails.add(devName, BorderLayout.EAST);
        
        JPanel addPanel;
        
        addPanel = MakeFrame.addComponent(fileSelection, 0, 0, 1, 1, 1, 0, bag);
        addPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "File Selection"));
        container.add(addPanel, bag);
        
        addPanel = MakeFrame.addComponent(allCitations, 0, 1, 1, 1, 1, 1, bag);
        addPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Citations"));
        container.add(addPanel, bag);
        
        addPanel = MakeFrame.addComponent(addCitations, 0, 2, 1, 1, 1, 0, bag);
        addPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Add Citations to File"));
        container.add(addPanel, bag);
        
        container.add(MakeFrame.addComponent(devDetails, 0, 3, 2, 1, 1, 0, bag), bag);
    }
    
    @Override
    public void showFrame() {
        super.showFrame();
        frame.setMinimumSize(new Dimension(frame.getPreferredSize().width, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void styleComponents(){
        ((TitledBorder)((JPanel)fileSelection.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)((JPanel)allCitations.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        ((TitledBorder)((JPanel)addCitations.getParent()).getBorder()).setTitleFont(SourceManagerFrame.UNIVERSAL_FONT);
        
        fileName.setFont(UNIVERSAL_FONT);
        chooseFile.setFont(UNIVERSAL_FONT);
        
        addCitation.setFont(UNIVERSAL_FONT);
        removeCitation.setFont(UNIVERSAL_FONT);
        editCitation.setFont(UNIVERSAL_FONT);
        citations.setFont(UNIVERSAL_FONT);
        
        addBibliography.setFont(UNIVERSAL_FONT);
        addFootnotes.setFont(UNIVERSAL_FONT);
        
        devName.setFont(UNIVERSAL_FONT);
        devName.setFont(devName.getFont().deriveFont(Font.ITALIC));
    }
    
    public void updateSourcesList(){
        String[] sourcesList = new String[cites.size()];
        for (int q = 0; q < cites.size(); q++){
            sourcesList[q] = cites.get(q).getName();
        }
        citations.setListData(sourcesList);
    }
    
    public static void addOpenConsole(JFrame window){
        window.getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "open console");
        window.getRootPane().getActionMap().put("open console", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BibConsole.getConsole().show();
            }
        });
    }
    
    static {
        try {
            FIELD_HINT = ImageIO.read(SourceManagerFrame.class.getResource("/fieldhint.png"));
            COPY_TO_CLIPBOARD = ImageIO.read(SourceManagerFrame.class.getResource("/copytoclipboard.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    
    public static Image getFieldHint(){
        return FIELD_HINT;
    }
    
    public static Image getCopyTOClipboard(){
        return COPY_TO_CLIPBOARD;
    }
    
    public void showLibraryInfo(){
        LibraryInfoFrame infoFrame = new LibraryInfoFrame(library.getInfo());
        infoFrame.openFrame();
    }
    
}
