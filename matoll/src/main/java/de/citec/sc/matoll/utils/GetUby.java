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
public class GetUby {
    public static void main(String[] args) throws FileNotFoundException {
        
        String language = "en";
//        Model model = RDFDataMgr.loadModel("/Users/swalter/Downloads/"+language+"_dbnary_lemon.ttl");
        /*
        for English:
        */
        Model model = RDFDataMgr.loadModel("/Users/swalter/Downloads/ow_eng.nt");
        System.out.println("Loaded RDF");
        List<List<String>> entries = getValues(model,language);
        System.out.println("Omega::"+entries.size());
        
        model.close();
        model = RDFDataMgr.loadModel("/Users/swalter/Downloads/WktEN.nt");
        System.out.println("Loaded RDF");
        entries.addAll(getValues(model,language));
        System.out.println("with wictionary:"+entries.size());
        
        
        model.close();
        model = RDFDataMgr.loadModel("/Users/swalter/Downloads/wn.nt");
        System.out.println("Loaded RDF");
        entries.addAll(getValues(model,language));
        System.out.println("with Wordnet:"+entries.size());
        
        model.close();
        model = RDFDataMgr.loadModel("/Users/swalter/Downloads/fn.nt");
        System.out.println("Loaded RDF");
        entries.addAll(getValues(model,language));
        System.out.println("with FrameNet:"+entries.size());
        
        
        model.close();
        model = RDFDataMgr.loadModel("/Users/swalter/Downloads/vn.nt");
        System.out.println("Loaded RDF");
        entries.addAll(getValues(model,language));
        System.out.println("with Verbnet:"+entries.size());
        
        PrintWriter printWriter = new PrintWriter ("resources/uby."+language);
        
        for(List<String> entry:entries){
//            System.out.println(entry.get(0));
//            System.out.println(entry.get(1));
//            System.out.println(entry.get(2));
//            System.out.println("");
            printWriter.write(entry.get(0)+"\t"+entry.get(1)+"\t"+entry.get(2)+"\n");
        }
        System.out.println(entries.size());
        
        printWriter.close();
        
        
    }
    
   
    
    
    private static List<List<String>> getValues(Model model,String language){
        String queryString = "PREFIX uby: <http://purl.org/olia/ubyCat.owl#>\n "
                    +"PREFIX lemon: <http://www.lemon-model.net/lemon#>\n" +
                    "SELECT DISTINCT ?label ?partofspeech ?entry WHERE {?entry uby:partOfSpeech ?partofspeech."
                + "?entry <http://lemon-model.net/lemon#canonicalForm> ?canonicalForm. "
                + "?canonicalForm <http://lemon-model.net/lemon#writtenRep> ?label.} ";
        //System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        List<List<String>> uby = new ArrayList<List<String>>();
        QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
        try {
            ResultSet results = qExec.execSelect();
            while ( results.hasNext() ) {
                 QuerySolution qs = results.next();
                 try{
                        List<String> entry = new ArrayList<String>();
                        String partofspeech = qs.get("?partofspeech").toString().replace("http://purl.org/olia/ubyCat.owl#","");
                        String uri = qs.get("?entry").toString();
                        String label = qs.get("?label").toString().replace("@eng", "");
                        entry.add(uri);
                        entry.add(partofspeech);
                        entry.add(label);
                        uby.add(entry);
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

        return uby;
    }
}
