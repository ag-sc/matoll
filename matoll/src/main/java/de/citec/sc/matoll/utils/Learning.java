/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.citec.sc.matoll.classifiers.WEKAclassifier;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.io.LexiconSerialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/**
 *
 * @author swalter
 */
public class Learning {
    
    public static void doTraining(Lexicon lexicon,Lexicon gold, HashMap<String,Double> maxima, Language language, WEKAclassifier weka_classifier, int value) throws IOException {

        Map<String,Integer> frequency_lookup = new HashMap<>();
        Set<String> pattern_lookup = new HashSet<>();
        Set<String> pos_lookup = new HashSet<>();
        getInformationAboutLexicon(lexicon,frequency_lookup,pattern_lookup,pos_lookup);
        List<Provenance> provenances = new ArrayList<>();
        List<Provenance> provenances_correct = new ArrayList<>();
        List<Provenance> provenances_wrong = new ArrayList<>();
        lexicon.getEntries().stream().forEach((entry) -> {
            String entry_cannoncical = entry.getCanonicalForm().replace("@"+language.toString().toLowerCase(),"");
            String pos = entry.getPOS();
            int overall_frequency = entry.getOverallFrequency();
            entry.getSenseBehaviours().keySet().stream().forEach((sense) -> {
                Reference ref = sense.getReference();
                Provenance prov = entry.getProvenance(sense);
                prov.setOverallPropertyEntryRatio(prov.getFrequency().doubleValue()/frequency_lookup.get(ref.getURI()));
                if(prov.getFrequency()>=value){
                    prov.setOveralLabelRatio(prov.getFrequency().doubleValue()/overall_frequency);
                    prov.setPOS(pos);
                    if (goldContainsEntry(gold,entry_cannoncical,pos,ref, language)) {
                        prov.setAnnotation(1);
                        provenances_correct.add(prov);
                    }
                    else {
                        prov.setAnnotation(0);
                        provenances_wrong.add(prov);
                    }
                }
                
            });
        });

        if (provenances_correct.size() > 0)
        {
            provenances.addAll(provenances_correct);
            /*
            try to get an even dataset
            */
            if(provenances_correct.size()<provenances_wrong.size()){
                 provenances.addAll(provenances_wrong.subList(0, provenances_correct.size()));
            }
            else{
                provenances.addAll(provenances_wrong);
            }
           
            weka_classifier.train(provenances,pattern_lookup,pos_lookup);
        }

        else
        {
                System.out.println("Can not train classifier as there are no training instances\n");
        }
    }
    
    
    
    private static boolean goldContainsEntry(Lexicon gold, String entry_cannoncical, String pos, Reference reference , Language language) {
        for(LexicalEntry gold_entry : gold.getEntries()){
            String gold_cannoncical = gold_entry.getCanonicalForm().replace("@"+language.toString().toLowerCase(),"");
            for(Reference gold_reference : gold_entry.getReferences()){
                if(entry_cannoncical.equals(gold_cannoncical)&&pos.equals(gold_entry.getPOS()) && reference.equals(gold_reference)) return true;
            }
            
        }
        
        return false;
    }
    
    
    public static void doPrediction(Lexicon lexicon, Lexicon gold, WEKAclassifier classifier, String output, Language language) throws IOException, Exception {
       classifier.loadModel("matoll"+language.toString()+".model");
       
        Map<String,Integer> frequency_lookup = new HashMap<>();
        Set<String> pattern_lookup = new HashSet<>();
        Set<String> pos_lookup = new HashSet<>();
        getInformationAboutLexicon(lexicon,frequency_lookup,pattern_lookup,pos_lookup);
       Lexicon learned_lexicon = new Lexicon();
       for(LexicalEntry entry:lexicon.getEntries()){
           int overall_frequency = entry.getOverallFrequency();
           for(Sense sense : entry.getSenseBehaviours().keySet()){
               Provenance prov = entry.getProvenance(sense);
               prov.setOveralLabelRatio(prov.getFrequency().doubleValue()/overall_frequency);
               prov.setOverallPropertyEntryRatio(prov.getFrequency().doubleValue()/frequency_lookup.get(sense.getReference().getURI()));
               prov.setPOS(entry.getPOS());
               try {
                   HashMap<Integer, Double> result = classifier.predict(prov,pattern_lookup,pos_lookup);
                   for(int key : result.keySet()){
                       if(key==1){
                           addEntryWithSense(learned_lexicon,entry,sense,result.get(key), prov,language);
                       }
                   }
               } catch (Exception ex) {
                   Logger.getLogger(Learning.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
       
       LexiconSerialization serializer = new LexiconSerialization(true);
       Model model = ModelFactory.createDefaultModel();

       serializer.serialize(learned_lexicon, model);

       FileOutputStream out = new FileOutputStream(new File("learned_lexicon"+language.toString()+".ttl"));

       RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;

       out.close();
//
//        Lexicon lexicon = new Lexicon();
//
//        LexiconEvaluation eval = new LexiconEvaluation();
//
//        FileWriter writer = new FileWriter(output);
//
//        for (int i=0; i < entries.size(); i++)
//        {
//                lexicon.addEntry(entries.get(i));
//
//                eval.setReferences(lexicon.getReferences());
//
//                eval.evaluate(lexicon,gold);
//
//                System.out.print("Considering entry "+entries.get(i)+"("+i+")\n");
//
//                writer.write(i+"\t"+eval.getPrecision("lemma")+"\t"+eval.getRecall("lemma")+"\t"+eval.getFMeasure("lemma")+"\t"+eval.getPrecision("syntactic")+"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")+"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")+"\t"+eval.getFMeasure("mapping")+"\n");
//
//                System.out.println(i+"\t"+eval.getPrecision("lemma")+"\t"+eval.getRecall("lemma")+"\t"+eval.getFMeasure("lemma")+"\t"+eval.getPrecision("syntactic")+"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")+"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")+"\t"+eval.getFMeasure("mapping"));
//
//                writer.flush();
//
//        }
//
//        writer.close();



        // System.out.print("Lexicon: "+output.toString()+" written out\n");
    }

    private static void addEntryWithSense(Lexicon learned_lexicon, LexicalEntry old_entry, Sense sense, Double confidence, Provenance prov, Language language) {
        LexicalEntry new_entry = new LexicalEntry(language);
        prov.setConfidence(confidence);
        new_entry.setCanonicalForm(old_entry.getCanonicalForm());
        new_entry.setPOS(old_entry.getPOS());
        new_entry.setURI(old_entry.getURI());
        new_entry.addAllSyntacticBehaviour(old_entry.getSenseBehaviours().get(sense), sense);
        new_entry.addProvenance(prov, sense);
        learned_lexicon.addEntry(new_entry);
        
    }

    private static void getInformationAboutLexicon(Lexicon lexicon,Map<String,Integer> frequency_lookup,
           Set<String> pattern_lookup,Set<String> pos_lookup) {
        lexicon.getEntries().stream().forEach((entry) -> {
            
            String pos = entry.getPOS();
            pos_lookup.add(pos);
            entry.getSenseBehaviours().keySet().stream().forEach((sense) -> {
                int freq = entry.getProvenance(sense).getFrequency();
                String uri = sense.getReference().getURI();
                if(frequency_lookup.containsKey(uri)){
                    int value = frequency_lookup.get(uri);
                    frequency_lookup.put(uri, value+freq);
                }
                else{
                    frequency_lookup.put(uri, freq);
                }
                entry.getProvenance(sense).getPatternset().stream().forEach((p) -> {
                    pattern_lookup.add(p);
                });
                
            });
        });
        

    }

        
    
}
