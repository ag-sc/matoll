/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import java.io.IOException;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author swalter
 */
public class Debug {
    
    private static Logger logger = null;
    private static boolean debug_on;
    private static boolean wait_on;
    
    public Debug(Logger logger){
        Debug.debug_on = false;
        Debug.wait_on = false;
        Debug.logger = logger;
    }
    
    public void setDebug(boolean input){
        Debug.debug_on = input;
    }
    
    public void setWait(boolean input){
        Debug.wait_on = input;
    }
    
    public boolean isDebug(){
        return debug_on;
    }
    
    public boolean isWait(){
        return wait_on;
    }
    
    public void printWaiter(){
        if(isDebug()){
             System.out.println("\n\n\n");
             System.out.println("Press any key to continue");
                try {
                    System.in.read();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
       
             
    }
    
    public void print(String message, String className){
        if(isDebug()){
            //logger.debug(className+":"+message);
            System.out.println(className+":"+message);            
        }
    }
    
    
    
    
    
    
}
