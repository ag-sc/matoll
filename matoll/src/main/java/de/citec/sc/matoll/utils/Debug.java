/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
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
    
    /**
     * Returns a human readable version of the parsed text
     * @param model RDF-Model
     */
    public void printDependencys(Model model){
        
        if(isDebug()){
            System.out.println("\n");
            HashMap<Integer,String> hm = new HashMap<Integer,String>();
            String query = "SELECT ?token ?number ?form ?lemma ?cpostag ?head ?deprel ?postag WHERE{"
                    + "?token <own:partOf> ?class. "
                    + "?token <conll:form> ?form. "
                    + "OPTIONAL{?token <conll:lemma> ?lemma}. "
                    + "?token <conll:cpostag> ?cpostag. "
                    + "?token <conll:postag> ?postag. "
                    + "?token <conll:head> ?head. "
                    + "?token <conll:deprel> ?deprel. "
                    + "?token <conll:wordnumber> ?number"
                    + "}";
            QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	 	ResultSet rs = qExec.execSelect() ;
	     
	 	try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 	        	 
	        	 try{
                                String lemma = "";
                                try{
                                    lemma = qs.get("?lemma").toString();
                                }
                                catch(Exception e){
                                    lemma = "_";
                                }
	        		 String line = qs.get("?number").toString()
                                         +"\t"
                                         //+qs.get("?token").toString()
                                         //+"\t"
                                         +qs.get("?form").toString()
                                         +"\t"
                                         +lemma
                                         +"\t"
                                         +qs.get("?cpostag").toString()
                                         +"\t"
                                         +qs.get("?postag").toString()
                                         +"\t"
                                         +qs.get("?head").toString().split("_")[1]
                                         +"\t"
                                         +qs.get("?deprel").toString();
                                 
                                 hm.put(Integer.valueOf(qs.get("?number").toString()),line);
                                 //System.out.println("Line:"+line);
                    		 
                         }
                         catch(Exception e){
	     	    	e.printStackTrace();
	     	    }
                 }
                }
                 catch(Exception e){
	     	    	e.printStackTrace();
	     	    }
                
                SortedSet<Integer> keys = new TreeSet<Integer>(hm.keySet());
                for(Integer key:keys){
                    System.out.println(hm.get(key));
                }
                System.out.println("\n");
                
        }
    }
    
    public void print(String message, String className){
        if(isDebug()){
            //logger.debug(className+":"+message);
            System.out.println(className+":"+message);            
        }
    }
    
    
    
    
    
    
}
