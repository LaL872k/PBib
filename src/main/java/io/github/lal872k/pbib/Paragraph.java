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

import java.awt.Font;

/**
 *
 * @author L. Arthur Lewis II
 */
public final class Paragraph {
    
    private String text = "";
    private TextAlignment alignment = TextAlignment.LEFT;
    private FontType font = FontType.REG;
    private int textIndent = 0;
    
    public enum TextAlignment {
        /**
         * This specifies that the text is aligned to the left indent and extra 
         * whitespace should be placed on the right.
         */
        LEFT("left"),
        /**
         * This specifies that the text is aligned to the center and extra 
         * whitespace should be placed equally on the left and right.
         */
        CENTER("center"), 
        /**
         * This specifies that the text is aligned to the right indent and 
         * extra whitespace should be placed on the left.
         */
        RIGHT("right"), 
        /**
         * This specifies that extra whitespace should be spread out through 
         * the rows of the paragraph with the text lined up with the left and 
         * right indent except on the last line which should be aligned to the left.
         */
        JUSTIFIED("justify");
        
        private final String val;
        
        TextAlignment(String val){
            this.val = val;
        }
        
        public String getStyleValue(){
            return val;
        }
        
    };
    
    public enum FontType {
        REG(new Font(Font.DIALOG, Font.PLAIN, 14)), 
        HEADER1(new Font(Font.DIALOG, Font.BOLD, 25)), 
        HEADER2(new Font(Font.DIALOG, Font.BOLD+Font.ITALIC, 20)), 
        HEADER3(new Font(Font.DIALOG, Font.PLAIN, 16));
        
        private final Font font;
        
        FontType(Font font){
            this.font = font;
        }
        
        public Font getFont(){
            return font;
        }
        
        public String getFontCSSText(){
            String styles = "";
            if (font.isBold()){
                styles += "bold ";
            }
            if (font.isItalic()){
                styles += "italic ";
            }
            return "font:"+styles+font.getSize()+"px "+font.getFontName()+";";
        }
        
    };
    
    public Paragraph() {}
    
    public Paragraph(String text, TextAlignment alignment, FontType font, int textIndent) {
        setText(text);
        setTextAlignment(alignment);
        setFontType(font);
        setTextIndent(textIndent);
    }
    
    public Paragraph(String text, TextAlignment alignment, FontType font) {
        setText(text);
        setTextAlignment(alignment);
        setFontType(font);
    }
    
    public Paragraph(String text) {
        setText(text);
    }
    
    public Paragraph(String text, TextAlignment alignment) {
        setText(text);
        setTextAlignment(alignment);
    }
    
    public Paragraph(String text, FontType font) {
        setText(text);
        setFontType(font);
    }
    
    public Paragraph(String text, int textIndent) {
        setText(text);
        setTextIndent(textIndent);
    }
    
    public void setTextAlignment(TextAlignment alignment){
        this.alignment = alignment;
    }
    
    public boolean setTextAlignment(String alignment){
        try {
            this.alignment = TextAlignment.valueOf(alignment);
            return true;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void setFontType(FontType font){
        this.font = font;
    }
    
    public boolean setFontType(String font){
        try {
            this.font = FontType.valueOf(font);
            return true;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setTextIndent(int textIndent){
        this.textIndent = textIndent;
    }
    
    public String getText(){
        return text;
    }
    
    public TextAlignment getTextAlignment(){
        return alignment;
    }
    
    public FontType getFontType(){
        return font;
    }
    
    public int getTextIndent(){
        return textIndent;
    }
    
}
