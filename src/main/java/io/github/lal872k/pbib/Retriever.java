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

import io.github.lal872k.pbib.ui.MakeFrame;


/**
 * This class is used to retrieve data through the means of a window that functions as a pop 
 * up menu. Be careful about running into an issue with swing threads because this will stop
 * the current thread in <code>{@link lal.pbib.Retriever#retrieve()}</code>.
 * @author L. Arthur Lewis II
 * @param <T> Type of data to retrieve
 */
public class Retriever<T> {
    
    private final MakeFrame frame;
    
    private T contents;
    
    private boolean waiting = false;
    
    /**
     * enum used to indicate the return type of a make frame
     */
    public enum ReturnStatus {
        /**
         * variable returned from <code>{@link lal.pbib.Retriever#retrieve()}</code> 
         * to indicate that an error occurred while retrieving the data.
        */
        ERROR,
        /**
         * variable returned from <code>{@link lal.pbib.Retriever#retrieve()}</code> 
         * to indicate that the data has been successfully sent through.
         */
        SELECTED,
        /**
         * variable returned from <code>{@link lal.pbib.Retriever#retrieve()}</code> 
         * to indicate that the user selected to cancel instead of sending through the data.
         */
        CANCELED;
    }
    
    public Retriever(MakeFrame frame){
        this.frame = frame;
    }
    
    /**
     * opens the window to retrieve the data from the user. The return is either 
     * <code>{@link lal.pbib.Retriever.ReturnStatus#SELECTED}</code>. Be careful about running 
     * into an issue with swing threads because this will stop
     * the current thread.
     * @return 
     */
    public ReturnStatus retrieve(){
        // make sure that this isn't called twice while it is running
        if (!waiting){
            waiting = true;
            
            // open the window
            frame.openFrame();
            
            // retrieve the contents
            try {
                contents = (T) frame.getContents();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            // remove the window
            frame.dispose();
            
            // return the result
            if (contents==null){
                return ReturnStatus.CANCELED;
            }
            return ReturnStatus.SELECTED;
        }
        return ReturnStatus.ERROR;
    }
    
    /**
     * gets the contents from the window after calling 
     * <code>{@link lal.pbib.Retriever#retrieve()}</code>.
     * @return 
     */
    public T getContents(){
        return contents;
    }
    
}
