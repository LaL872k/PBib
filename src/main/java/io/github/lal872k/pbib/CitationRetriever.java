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

import io.github.lal872k.pbib.ui.MakeCitationFrame;
import java.awt.Component;

/**
 * Retriever for making a citation.
 * @author L. Arthur Lewis II
 */
public class CitationRetriever extends Retriever<Citation>{
    
    private static int index = 1;
    
    public CitationRetriever(SourceLibrary library){
        super(new MakeCitationFrame(library));
    }
    
    public CitationRetriever(SourceLibrary library, Component parentWindow){
        super(new MakeCitationFrame(library, "Citation "+index, parentWindow));
    }
    
    @Override
    public Retriever.ReturnStatus retrieve() {
        Retriever.ReturnStatus val = super.retrieve();
        index++;
        return val;
    }
    
}
