/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import de.citec.sc.matoll.core.Language;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author swalter
 */
public class Uby {
    
    private final HashMap<String,List<DbnaryObject>> uby_list = new HashMap<String,List<DbnaryObject>>();
    private Language language;
    
    public Uby(Language language){
        if(language.equals(Language.EN)) loadUby("resources/uby.en");
        this.language = language;
    }
    
    
    private void loadUby(String file){
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            String[] lines = content.split("\n");
            
            for(String line : lines){
                String[] items = line.split("\t");
                DbnaryObject ubyobject = new DbnaryObject();
                ubyobject.setDbnary_uri(items[0]);
                //ubyobject.setPartOfSpeech(normalise(items[1]));
                ubyobject.setPartOfSpeech(items[1]);
                ubyobject.setLabel(items[2]);
                update(ubyobject);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Uby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public HashSet<String> getURI(String label, String pos){
        HashSet<String> uri = new HashSet<String>();
        pos = pos.replace("commonNoun", "noun");
        label = label.replace("@"+language.toString().toLowerCase(), "");
        if(uby_list.containsKey(label)){
            List<DbnaryObject> value = uby_list.get(label);
            
            for(DbnaryObject ubyobject : value){
                if(ubyobject.getPartOfSpeech().equals(pos.toLowerCase())){
                    uri.add(ubyobject.getDbnary_uri());
                }
            }
            
            
        }
        
        return uri;
        
       
    }

//    private String normalise(String item) {
//        item = item.replace("Substantiv","noun");
//        item = item.replace("Verb", "verb");
//        item = item.replace("Adjektiv","adjective");
//        item = item.replace("sustantivo", "noun");
//        item = item.replace("adjetivo","adjective");
//        item = item.replace("verbo","verb");
//        /*
//        TODO: Check for example Redewendung etc. in dbnary.de 
//        Best: Only load adjective, nouns and verbs
//        */
//        
//        
//        return item.toLowerCase();
//    }

    private void update(DbnaryObject ubyobject) {
        if(uby_list.containsKey(ubyobject.getLabel())){
            List<DbnaryObject> value = uby_list.get(ubyobject.getLabel());
            value .add(ubyobject);
            this.uby_list.put(ubyobject.getLabel(), value);
        }
        else{
            List<DbnaryObject> value = new ArrayList<DbnaryObject>();
            value .add(ubyobject);
            this.uby_list.put(ubyobject.getLabel(),value);
        }
    }
    
}
