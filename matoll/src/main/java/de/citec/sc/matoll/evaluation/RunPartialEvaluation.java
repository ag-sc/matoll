/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.evaluation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/**
 *
 * @author swalter
 */
public class RunPartialEvaluation {
    public static void main(String[] args){
        
        
        String path_automaticlexicon = "/Users/swalter/Downloads/dbpedia2014_beforeTraining_localhost.ttl";
        
        List<List<Integer>> areas = new ArrayList<List<Integer>>();
        List<Integer> tmp = new ArrayList<Integer>();
        tmp.add(1);
        tmp.add(5);
        areas.add(tmp);
        
        List<Integer> tmp0 = new ArrayList<Integer>();
        tmp0.add(5);
        tmp0.add(10);
        areas.add(tmp0);
        
        List<Integer> tmp1 = new ArrayList<Integer>();
        tmp1.add(10);
        tmp1.add(100);
        areas.add(tmp1);
        
        List<Integer> tmp2 = new ArrayList<Integer>();
        tmp2.add(100);
        tmp2.add(500);
        areas.add(tmp2);
        
        List<Integer> tmp3 = new ArrayList<Integer>();
        tmp3.add(500);
        tmp3.add(100000);
        areas.add(tmp3);
        

        
        
        LexiconLoader loader = new LexiconLoader();
        Lexicon gold = loader.loadFromFile("../lexica/dbpedia_en.rdf");

        
        Lexicon automatic = loader.loadFromFile(path_automaticlexicon);
        System.out.println("Loaded automatic lexicon");

        
        /*
        reduce gold standard to those entries, which reference is in the development or testing dataset
        */
        Lexicon gold_reduced = new Lexicon();
        List<String> properties;
        Set<String> gold_pos;
        gold_pos = new HashSet<String>();
        properties = loadDataset("resources/iswc2014_train.txt");
        for(LexicalEntry entry:gold.getEntries()){
            gold_pos.add(entry.getPOS());
            Set<Reference> reference = entry.getReferences();
            for(Reference ref : reference){
                try{
                    if (properties.contains(ref.getURI())){
                    //System.out.println(ref.getURI());
                    gold_reduced.addEntry(entry);
                    break;
                    }
                }
                catch(Exception e){
                    //e.printStackTrace();
                }

                
            }
        }
        System.out.println("Reduced gold lexicon to dataset");
        System.out.println(gold_reduced.size());

        try {
            writeLex(gold_reduced,"gold_reduced.lex");
        } catch (Exception ex) {
            //Logger.getLogger(RunPartialEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
        /*
        consider only entries, which reference is also used in the gold standard
        */
        Lexicon automatic_reduced = new Lexicon();
        Set<Reference> references = gold_reduced.getReferences();
        List<String> gold_references = new ArrayList<String>();
        for(Reference ref : references){
            try{
                gold_references.add(ref.getURI());
            }
            catch(Exception e){
                
            }
            
        }
        Set<String> automatic_pos;
        automatic_pos = new HashSet<String>();
        for(LexicalEntry entry : automatic.getEntries()){
            automatic_pos.add(entry.getPOS());
            for(Reference ref: entry.getReferences()){
                if(gold_references.contains(ref.getURI())){
                    automatic_reduced.addEntry(entry);
                    break;
                }
            }
        }
        System.out.println("Reduced gold lexicon to dataset");
        System.out.println();
       
        System.out.println("Automatic lexicon contains "+automatic.size()+" entries");
        System.out.println("Cleaned automatic lexicon contains "+automatic_reduced.size()+" entries");
        System.out.println("Gold lexicon contains "+gold.size()+" entries");
        System.out.println("Cleaned gold lexicon contains "+gold_reduced.size()+" entries");
        
        
        try {
            writeLexicon(automatic_reduced,"automatic_reduced");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RunPartialEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
        
        
        for(List<Integer> area : areas){
            Lexicon lexicon = new Lexicon();
            int min_val = area.get(0);
            int max_val = area.get(1);
            System.err.println("Adapt to new provenance style");
//            for(LexicalEntry entry:automatic_reduced.getEntries()){
//                try{
//                  int frequency = entry.getProvenance().getFrequency();
//                  if(frequency>= min_val && frequency<max_val) lexicon.addEntry(entry);
//                }
//                catch(Exception e){
//                    
//                }
//            }
            System.out.println("min_value:"+min_val);
            System.out.println("max_value:"+max_val);
            System.out.println("New lexicon contains "+lexicon.size()+" entries");
            if(lexicon.size()>0){
                eval.evaluate(lexicon,gold_reduced);
                System.out.println("P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")
                    +"\tF:"+eval.getFMeasure("lemma"));
//                        +"\t"+eval.getPrecision("syntactic")
//                    +"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")
//                    +"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")
//                    +"\t"+eval.getFMeasure("mapping"));
            }
        }
        
        eval.evaluate(automatic_reduced,gold_reduced);
                System.out.println("P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")
                    +"\tF:"+eval.getFMeasure("lemma"));
//                        +"\t"+eval.getPrecision("syntactic")
//                    +"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")
//                    +"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")
//                    +"\t"+eval.getFMeasure("mapping"));
//              
//               
//                
//        eval.evaluate(gold,gold);
//                System.out.println("P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")
//                    +"\tF:"+eval.getFMeasure("lemma"));
////                        +"\t"+eval.getPrecision("syntactic")
////                    +"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")
////                    +"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")
////                    +"\t"+eval.getFMeasure("mapping"));
//        
//        
                
////       System.out.println("Gold pos:");
////       for(String pos:gold_pos){
////           System.out.println(pos);
////       }
////       System.out.println();
////       System.out.println("automatic pos:");
////       for(String pos:automatic_pos){
////           System.out.println(pos);
////       }
        
    }

    
    
    
    private static List<String> loadDataset(String file){
        List<String> properties = new ArrayList<String>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            String[] lines = content.split("\n");
            for(String l : lines) properties.add(l);
            
            
        } catch (IOException ex) {
            System.out.println("Did not find file: "+file);
        }
        return properties;
    }

    private static void writeLexicon(Lexicon lexicon, String name) throws FileNotFoundException {
        LexiconSerialization serializer = new LexiconSerialization(Language.EN);
		
        Model model = ModelFactory.createDefaultModel();

        serializer.serialize(lexicon, model);

        FileOutputStream out = new FileOutputStream(new File(name+".ttl"));

        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
    }
    
    
    
    
    private static void writeLex(Lexicon lexicon, String filename) throws IOException{
        FileWriter writer = new FileWriter(filename);

        for (LexicalEntry entry: lexicon.getEntries())
        {
                writer.write(entry.toString()+"\n");
                writer.flush();
        }

        writer.close();
    }
    
    
}
