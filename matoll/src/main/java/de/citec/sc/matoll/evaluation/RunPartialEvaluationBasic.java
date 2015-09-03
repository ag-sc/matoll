/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.evaluation;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.io.LexiconLoader;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author swalter
 */
public class RunPartialEvaluationBasic {
    public static void main(String[] args){
        
        
        String path_automaticlexicon = "/Users/swalter/Downloads/dbpedia2014Full_new_beforeTraining.ttl";

        
        
        LexiconLoader loader = new LexiconLoader();
        Lexicon gold = loader.loadFromFile("../lexica/dbpedia_en.rdf");

        String path = "/Users/swalter/Documents/results_matoll_july_2015/";
        Lexicon set1 = loader.loadFromFile(path+"set1.ttl");
        Lexicon set2 = loader.loadFromFile(path+"set2.ttl");
        Lexicon set3 = loader.loadFromFile(path+"set3.ttl");
        Lexicon set4 = loader.loadFromFile(path+"set4.ttl");
                
        Lexicon automatic = new Lexicon();
        for(LexicalEntry entry : set1.getEntries()) automatic.addEntry(entry);
        for(LexicalEntry entry : set2.getEntries()) automatic.addEntry(entry);
        for(LexicalEntry entry : set3.getEntries()) automatic.addEntry(entry);
        for(LexicalEntry entry : set4.getEntries()) automatic.addEntry(entry);
        System.out.println("Loaded automatic lexicon");
        System.out.println("#entries:"+automatic.size());
        
        HashSet<String> properties_gold = new HashSet<String>();
        HashSet<String> properties_automatic = new HashSet<String>();
        
        for(LexicalEntry entry : gold.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    properties_gold.add(uri);
                }
                else{
                    Restriction reference = (Restriction) ref;
                    //properties_gold.add(reference.getProperty());
                }
            }
         }
        
        for(LexicalEntry entry : automatic.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    properties_automatic.add(uri);
                }
                else{
                    Restriction reference = (Restriction) ref;
                    properties_automatic.add(reference.getProperty());
                }
            }
         }
        
        
        System.out.println("properties_gold.size():"+properties_gold.size());
        System.out.println("properties_automatic.size():"+properties_automatic.size());
        properties_gold.retainAll(properties_automatic);
        System.out.println("#common properties:"+properties_gold.size());
        
        Lexicon new_gold = new Lexicon();
        Lexicon new_automatic = new Lexicon();
        
        for(LexicalEntry entry : gold.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    if(properties_gold.contains(uri)){
                        new_gold.addEntry(entry);
                        break;
                    }
                }
            }
         }
        
        for(LexicalEntry entry : automatic.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    if(properties_gold.contains(uri)){
                        new_automatic.addEntry(entry);
                        break;
                    }
                }
            }
         }
        
        LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
        
        eval.evaluate(new_automatic,new_gold);
                System.out.println("P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")
                    +"\tF:"+eval.getFMeasure("lemma"));
        
        }
    
    

    
    
}
