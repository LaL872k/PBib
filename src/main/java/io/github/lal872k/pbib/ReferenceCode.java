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

import io.github.lal872k.pbib.ui.comps.CompInput;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 *
 * @author L. Arthur Lewis II
 */
public class ReferenceCode {
    
    /*
    terms:
    condition: like in the if statement what needs to be true for it to run
    output: the output if the condion is met
    
    */
    
    /**
     * used to get the inverse of the next value ie. true if a field wasn't filled out
     */
    public static final char INVERSE = '!';
    
    /**
     * used after the curly brackets to indicate a reference to a field
     */
    public static final char NAME_REFERENCE = '@';
    
    /**
     * used after the curly bracket to indicate a if statement
     */
    public static final char IF = '?';
    
    /**
     * regex used to find references to a fields input, check group 1 for the value.
     */
    public static final String NAME_REFERENCE_REGEX = "(\\{\\"+NAME_REFERENCE+"[^}]*\\})";
    
    /**
     * regex used to find if statements, check group 1 for the value.
     */
    public static final String IF_NAME_REGEX = "(\\{\\"+IF+"[^}]*\\})";
    
    /**
     * char used to split the condition from the output inside the if statement.
     */
    public static final char CONDITION_AND_OUTPUT_SEPARATOR = ':';
    
    /**
     * regex used to split the condition for the if and the output.
     */
    public static final String CONDITION_AND_OUTPUT_SEPERATOR_REGEX = 
            "\\"+CONDITION_AND_OUTPUT_SEPARATOR;
    
    /**
     * char used to separate the different conditions that must both be true.
     */
    public static final char MULTIPLE_CONDITIONS_AND = '+';
    
    /**
     * char used to separate the different conditions that must both be true.
     */
    public static final String MULTIPLE_CONDITIONS_AND_REGEX = "\\"+MULTIPLE_CONDITIONS_AND;
    
    /**
     * char used to separate the different conditions where at least one must be true.
     */
    public static final char MULTIPLE_CONDITIONS_OR = '|';
    
    /**
     * char used to separate the different conditions where at least one must be true.
     */
    public static final String MULTIPLE_CONDITIONS_OR_REGEX = "\\"+MULTIPLE_CONDITIONS_OR;
    
    /**
     * place holder for a true statement. Used when doing conditions that contain parenthesis.
     */
    public static final String TRUE_PLACE_HOLDER = "TRUESTATEMENT00000";
    
    /**
     * place holder for a true statement. Used when doing conditions that contain parenthesis.
     */
    public static final String FALSE_PLACE_HOLDER = "FALSESTATEMENT99999";
    
    /**
     * regex used to find a pair of parenthesis with no other parenthesis inside.
     */
    public static final String PARENTHESIS_REGEX = "(\\([^\\(\\)]+\\))";
    
    /**
     * validates a reference code, returns null if it is valid, an error message otherwise.
     * @param code reference code.
     * @return null if it is valid, an error message otherwise.
     */
    public static String validateCode(String code){
        // make sure all parenthesis are in order
        int parenthesis = 0;
        int brackets = 0;
        boolean insideBrackets = false;
        boolean insideCondition = false;
        for (char letter : code.toCharArray()){
            if (letter=='(' && insideBrackets && insideCondition){
                parenthesis++;
            }
            if (letter==')' && insideBrackets && insideCondition){
                parenthesis--;
            }
            if (parenthesis<0){
                return "used closing parenthesis without opening parenthesis.";
            }
            if (letter=='{'){
                brackets++;
                if (brackets==1){
                    insideBrackets=true;
                    insideCondition=true;
                }
            }
            if (letter=='}'){
                brackets--;
            }
            if (letter==CONDITION_AND_OUTPUT_SEPARATOR){
                insideCondition=false;
                if (parenthesis>0){
                    return "didn't complete parenthesis inside brackets.";
                }
            }
            // can't have curly brackets inside curly brackets
            if (brackets>1){
                return "curly brackets inside curly brackets.";
            }
            if (brackets<0){
                return "to many closing brackets.";
            }
            if (brackets==0){
                insideBrackets=false;
                insideCondition=false;
            }
        }
        return null;
    }
    
    /**
     * turn a reference code used for the bibliography and footnotes into a paragraph
     * @param code reference code
     * @param inputs all the inputs that will be referenced from.
     * @return paragraph containing the code with references.
     */
    public static CTP getCitation(String code, CompInput[] inputs){
        CTP output = CTP.Factory.newInstance();
        output.addNewR().addNewT().setStringValue(code);
        
        Pattern namePattern = Pattern.compile(NAME_REFERENCE_REGEX);
        Pattern ifPattern = Pattern.compile(IF_NAME_REGEX);
        Matcher m;
        
        // name referenes
        m = namePattern.matcher(code);
        while (m.find()){
            // get the full message / place holder ex.: {!name}
            String full = m.group(1);
            // get the actual name ex.: "{!name}" ==> "name"
            String name = full.substring(2, full.length()-1);
            // get the ctr for the replaced text
            CTR replace = CTR.Factory.newInstance();
            
            // find the input with that name
            for (CompInput in : inputs){
                if (name.equals(in.getName())){
                    // add the text inside the input field
                    replace.addNewT().setStringValue(in.getData());
                    
                    // add any styles (bold/italic)
                    if (in.isBold() || in.isItalic()){
                        CTRPr rpr = replace.addNewRPr();
                        if (in.isBold()){
                            rpr.addNewB();
                        }
                        if (in.isItalic()){
                            rpr.addNewI();
                        }
                    }
                }
            }
            // replace the place holder with the text inside the input
            replaceText(output, replace, full);
        }
        
        // ifs
        m = ifPattern.matcher(code);
        while (m.find()){
            // get the full message
            String full = m.group(1);
            // get just the contents of the message
            String contents = full.substring(2, full.length()-1);
            // make sure that the condition and output are seperated
            if (contents.contains(new String(new char[]{CONDITION_AND_OUTPUT_SEPARATOR}))){
                
                // separate the contents by condition and output char
                String[] coArray = contents.split(CONDITION_AND_OUTPUT_SEPERATOR_REGEX);
                
                // the conditions that need to be met (can't contain a separator inside the names)
                String condition = coArray[0];
                
                // the output if the conditions are met
                String out = arrayToStringWithDelimiter(Arrays.copyOfRange(coArray, 1, coArray.length), new String(new char[]{CONDITION_AND_OUTPUT_SEPARATOR}));
                
                // the ctr version of out
                CTR outCTR = CTR.Factory.newInstance();
                CTText t = outCTR.addNewT();
                t.setStringValue(out);
                t.setSpace(SpaceAttribute.Space.PRESERVE);
                
                // replace all fields with conditions
                condition = replaceFieldsWithPlaceHolders(condition, inputs);
                
                // determine whether it is true or false
                if (getConditionStatus(condition)){
                    replaceText(output, outCTR, full);
                } else {
                    replaceText(output, CTR.Factory.newInstance(), full);
                }
            } else {
                // invalid code no separator
            }
        }
        return output;
    }
    
    /**
     * finds the first match of a string inside a paragraph and removes and replaces it with 
     * the new run.
     * @param ctp paragraph that contains all contents.
     * @param replace the run to replace the text with.
     * @param find the text to find inside the paragraph.
     */
    private static void replaceText(CTP ctp, CTR replace, String find){
        // check all of the runs
        for (int q = 0; q < ctp.getRArray().length; q++){
            // get current ctr
            CTR ctr = ctp.getRArray(q);
            
            // get text of all the t's and put it inside the text variable
            String text = "";
            for (CTText t : ctr.getTArray()){
                text += t.getStringValue();
            }
            
            // get location of the text to be found inside the current run
            int index = text.indexOf(find);
            
            // run contains the text
            if (index!=-1){
                // remove text that needed to be found
                text = text.replace(find, "");

                // 1st half of the text cut from index
                CTR half1 = CTR.Factory.newInstance();
                CTText half1Text = half1.addNewT();
                half1Text.setStringValue(text.substring(0, index));
                half1Text.setSpace(SpaceAttribute.Space.PRESERVE);

                // 2nd half of the text cut from index
                CTR half2 = CTR.Factory.newInstance();
                CTText half2Text = half2.addNewT();
                half2Text.setStringValue(text.substring(index));
                half2Text.setSpace(SpaceAttribute.Space.PRESERVE);

                // remove the run working in to replace it with these three new ones
                ctp.removeR(q);

                // add new runs
                DocumentIO.insertRinParagraph(ctp, new CTR[]{half2, replace, half1}, q);
                break;
            }
        }
    }
    
    /**
     * replaces all references of a name with a place holder that represents whether it is 
     * true or false by making it <code>{@link lal.pbib.ReferenceCode#TRUE_PLACE_HOLDER}</code>
     * or <code>{@link lal.pbib.ReferenceCode#FALSE_PLACE_HOLDER}</code>.
     * @param conditions the string that contains the conditions.
     * @param inputs all of the inputs used with this line of conditions.
     * @return a new string with the names of the inputs replaced with place holders for true
     * and false based on whether or no the field was filled in.
     */
    public static String replaceFieldsWithPlaceHolders(String conditions, CompInput[] inputs){
        // use a comparator to make any names that contain another name go first to avoid
        // having any issues
        Comparator<CompInput> sort = new Comparator<CompInput>() {
            @Override
            public int compare(CompInput o1, CompInput o2) {
                if (o1.getName().contains(o2.getName())){
                    return -1;
                }
                if (o2.getName().contains(o1.getName())){
                    return 1;
                }
                return 0;
            }
        };
        Arrays.sort(inputs, sort);
        for (CompInput in : inputs){
            // set the place holder based on whether or nor the value has been filled in
            String placeHolder;
            String placeHolderInverse;
            if (in.getData().contentEquals("")){
                placeHolder = FALSE_PLACE_HOLDER;
                placeHolderInverse = TRUE_PLACE_HOLDER;
            } else {
                placeHolder = TRUE_PLACE_HOLDER;
                placeHolderInverse = FALSE_PLACE_HOLDER;
            }
            // change all inverse occurances with the inverse place holder
            conditions = conditions.replace(INVERSE+in.getName(), placeHolderInverse);
            // change all occurances of that input field with the place holder
            conditions = conditions.replace(in.getName(), placeHolder);
        }
        return conditions;
    }
    
    /**
     * determines whether an entire condition is true or false.
     * @param condition the condition that is being checked.
     * @return true if the entire condition is true, false otherwise.
     */
    public static boolean getConditionStatus(String condition){
        // this can't hurt the program and it needs parenthesis to work
        condition = "("+condition+")";
        
        // get the regex for the parenthesis
        Pattern pat = Pattern.compile(PARENTHESIS_REGEX);
        Matcher m;
        
        // keep going as long as there is parenthesis left
        while (condition.contains("(") || condition.contains(")")){
            m = pat.matcher(condition);
            while (m.find()){
                String parenText = m.group(1);
                
                // get the text inside the parentheis
                String parenCondition = parenText.substring(1, parenText.length()-1);
                
                // statement was true
                if (getConditionStatement(parenCondition)){
                    condition = condition.replace(parenText, TRUE_PLACE_HOLDER);
                } 
                // statement was false
                else {
                    condition = condition.replace(parenText, FALSE_PLACE_HOLDER);
                }
            }
        }
        
        return condition.equals(TRUE_PLACE_HOLDER);
    }
    
    /**
     * determines whether a line is OR or AND and then determines whether the whole line is 
     * true or false.
     * @param line the line to with all the place holders.
     * @return true if the entire line is true, otherwise false.
     */
    public static boolean getConditionStatement(String line){
        // determine whether it is an "and" or an "or"
        String conditionSeparator = MULTIPLE_CONDITIONS_OR_REGEX;
        boolean orStatement = true;
        if (line.contains(new String(new char[]{MULTIPLE_CONDITIONS_AND}))){
            conditionSeparator = MULTIPLE_CONDITIONS_AND_REGEX;
            orStatement = false;
        }
        
        // check to see how many of the place holders are true
        String[] placeHolders = line.split(conditionSeparator);
        int truePlaceHolders = 0;
        for (String placeHolder : placeHolders){
            if (placeHolder.equals(TRUE_PLACE_HOLDER)){
                truePlaceHolders++;
            }
        }
        
        // if it is an OR separator than all we need is one true place holder, but if it is 
        // and AND separator then we need as many true place holders as there are place 
        // holders
        return ((orStatement && truePlaceHolders>0) || (!orStatement && truePlaceHolders==placeHolders.length));
    }
    
    /**
     * turns an array into a string with a delimiter in between each item.
     * @param array array of strings.
     * @param delimiter delimiter in between each item.
     * @return new string with each of the items of the array separated by the delimiter.
     */
    public static String arrayToStringWithDelimiter(String[] array, String delimiter){
        String text = "";
        for (int q = 0; q < array.length; q++){
            String item = array[q];
            text+=item;
            if (q+1<array.length){
                text+=delimiter;
            }
        }
        return text;
    }
    
}
