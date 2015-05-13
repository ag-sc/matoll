/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author swalter
 */
public class GetDbnary {
    public static void main(String[] args) throws FileNotFoundException {
        
        String language = "en";
//        Model model = RDFDataMgr.loadModel("/Users/swalter/Downloads/"+language+"_dbnary_lemon.ttl");
        /*
        for English:
        */
        Model model = RDFDataMgr.loadModel("/Users/swalter/Downloads/en_dbnary_lemon_20150321.ttl");
        System.out.println("Loaded RDF");
        
        PrintWriter printWriter = new PrintWriter ("resources/dbnary."+language);

        
        List<List<String>> entries = getValues(model,language);
        
        for(List<String> entry:entries){
            printWriter.write(entry.get(0)+"\t"+entry.get(1)+"\t"+entry.get(2)+"\n");
            /*System.out.println(entry.get(0));
            System.out.println(entry.get(1));
            System.out.println(entry.get(2));
            System.out.println("");*/
        }
        System.out.println(entries.size());
        printWriter.close();
        
        
    }
    
   
    
    
    private static List<List<String>> getValues(Model model,String language){
        String queryString = "PREFIX lexvo: <http://lexvo.org/id/iso639-3/>\n "
                + "PREFIX dbnary: <http://kaiko.getalp.org/dbnary#>\n" +
                    "PREFIX lemon: <http://www.lemon-model.net/lemon#>\n" +
                    "SELECT ?entry ?partofspeech ?label WHERE {\n" +
                    "  ?entry a lemon:LexicalEntry.\n" +
                    "  ?entry dbnary:partOfSpeech ?partofspeech .\n" +
                    "  ?entry lemon:canonicalForm ?cannoncialform .\n" +
                    "  ?cannoncialform lemon:writtenRep ?label .\n" +
                    "  ?entry lemon:language \""+language+"\" .\n" +
                    "} ";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        List<List<String>> dbnary = new ArrayList<List<String>>();
        QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
        try {
            ResultSet results = qExec.execSelect();
            while ( results.hasNext() ) {
                 QuerySolution qs = results.next();
                 try{
                        List<String> entry = new ArrayList<String>();
                        String uri = qs.get("?entry").toString();
                        String partofspeech = qs.get("?partofspeech").toString();
                        String label = qs.get("?label").toString().replace("@"+language,"");
                        entry.add(uri);
                        entry.add(partofspeech);
                        entry.add(label);
                        dbnary.add(entry);
                 }
                 catch(Exception e){
                        e.printStackTrace();
                                 //ignore those without Frequency TODO:Check Source of Error
                 }
            }
        }
         catch(Exception e){
        e.printStackTrace();
         //ignore those without Frequency TODO:Check Source of Error
         }

        return dbnary;
    }
    
}
