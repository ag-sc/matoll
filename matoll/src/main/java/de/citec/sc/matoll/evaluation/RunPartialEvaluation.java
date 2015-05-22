/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.evaluation;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author swalter
 */
public class RunPartialEvaluation {
    public static void main(String[] args){
        
        
        String path_automaticlexicon = "";
        
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
        
        
        for(List<Integer> i : areas){
            System.out.println(i.get(0));
            System.out.println(i.get(1));
            System.out.println();
        }
        
        
        LexiconLoader loader = new LexiconLoader();
        Lexicon gold = loader.loadFromFile("../lexica/dbpedia_en.rdf");
        System.out.println("Loaded gold");
        
        Lexicon automatic = loader.loadFromFile(path_automaticlexicon);
       
        LexiconEvaluation eval = new LexiconEvaluation();
        
        
        for(List<Integer> area : areas){
            Lexicon lexicon = new Lexicon();
            int min_val = area.get(0);
            int max_val = area.get(1);
            for(LexicalEntry entry:automatic.getEntries()){
                try{
                  int frequency = entry.getProvenance().getFrequency();
                  if(frequency>= min_val || frequency<max_val) lexicon.addEntry(entry);
                }
                catch(Exception e){
                    
                }
                
                System.out.println("min_value:"+min_val);
                System.out.println("max_value:"+max_val);
                System.out.println("New lexicon contains "+lexicon.size()+" entries");
                if(lexicon.size()>0){
                    eval.evaluate(lexicon,gold);
                    System.out.println(eval.getPrecision("lemma")+"\t"+eval.getRecall("lemma")
                        +"\t"+eval.getFMeasure("lemma")+"\t"+eval.getPrecision("syntactic")
                        +"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")
                        +"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")
                        +"\t"+eval.getFMeasure("mapping"));
                }
                
                
            }
            
            
        }
        
        
        
    }
}
