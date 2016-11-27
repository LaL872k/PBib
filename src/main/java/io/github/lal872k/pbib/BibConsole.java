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

import io.github.lal872k.console.Console;
import io.github.lal872k.console.extensions.command.Command;
import io.github.lal872k.console.extensions.command.CommandHandler;
import io.github.lal872k.console.extensions.jconsole.JavaConsole;
import java.awt.Color;

/**
 *
 * @author L. Arthur Lewis II
 */
public class BibConsole {
    
    private static final Console CONSOLE;
    private static final CommandHandler COMMAND_HANDLER;
    private static boolean debugging = true;
    public static final Color DEBUG_TEXT = new Color(67, 233, 242);
    
    static {
        CONSOLE = new Console();
        CONSOLE.setWindowTitle("Console - PBib");
        COMMAND_HANDLER = new CommandHandler();
        COMMAND_HANDLER.addCommand(new Command("debug", (console, arguments) -> {
            if (arguments.length>0){
                if (arguments[0].toLowerCase().equals("true")){
                    debugging = true;
                }
                else {
                    debugging = false;
                }
                console.println("Debugging status: "+Boolean.toString(debugging), DEBUG_TEXT);
            } else {
                console.printlnError("Missing value of true/false");
            }
        }));
        COMMAND_HANDLER.addCommand(new Command("quit", (console, arguments) -> {
            System.exit(0);
        }));
        COMMAND_HANDLER.addToConsole(CONSOLE);
        JavaConsole jc = new JavaConsole();
        jc.addToConsole(CONSOLE);
    }
    
    public static void println(String msg){
        getConsole().println(msg);
    }
    
    public static void println(){
        getConsole().println();
    }
    
    public static void print(String msg){
        getConsole().print(msg);
    }
    
    public static void debugln(String msg){
        if (debugging){
            getConsole().println(msg, DEBUG_TEXT);
        }
    }
    
    public static Console getConsole(){
        return CONSOLE;
    }
    
    public static CommandHandler getCommandHandler(){
        return COMMAND_HANDLER;
    }
    
}
