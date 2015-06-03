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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author swalter
 */
public class Dbnary {
    
    private final HashMap<String,List<DbnaryObject>> dbnaryList = new HashMap<String,List<DbnaryObject>>();
    private Language language;
    
    public Dbnary(Language language){
        if(language.equals(Language.EN)) loadDbnary("resources/dbnary.en");
        if(language.equals(Language.DE)) loadDbnary("resources/dbnary.de");
        if(language.equals(Language.JA)) loadDbnary("resources/dbnary.ja");
        if(language.equals(Language.ES)) loadDbnary("resources/dbnary.es");
        this.language = language;
        
    }
    
    
    private void loadDbnary(String file){
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            String[] lines = content.split("\n");
            
            for(String line : lines){
                String[] items = line.split("\t");
                DbnaryObject dbnaryobject = new DbnaryObject();
                dbnaryobject.setDbnary_uri(items[0]);
                dbnaryobject.setPartOfSpeech(normalise(items[1]));
                dbnaryobject.setLabel(items[2]);
                update(dbnaryobject);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Dbnary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String getURI(String label, String pos){
        String uri = "";
        pos = pos.replace("commonNoun", "noun");
        label = label.replace("@"+language.toString().toLowerCase(), "");
        if(dbnaryList.containsKey(label)){
            List<DbnaryObject> value = dbnaryList.get(label);
            
            for(DbnaryObject dbnaryobject : value){
                if(dbnaryobject.getPartOfSpeech().equals(pos.toLowerCase())){
                    uri = dbnaryobject.getDbnary_uri();
                }
            }
            
            
        }
        
        return uri;
        
       
    }

    private String normalise(String item) {
        item = item.replace("Substantiv","noun");
        item = item.replace("Verb", "verb");
        item = item.replace("Adjektiv","adjective");
        item = item.replace("sustantivo", "noun");
        item = item.replace("adjetivo","adjective");
        item = item.replace("verbo","verb");
        /*
        TODO: Check for example Redewendung etc. in dbnary.de 
        Best: Only load adjective, nouns and verbs
        */
        
        
        return item.toLowerCase();
    }

    private void update(DbnaryObject dbnaryobject) {
        if(dbnaryList.containsKey(dbnaryobject.getLabel())){
            List<DbnaryObject> value = dbnaryList.get(dbnaryobject.getLabel());
            value .add(dbnaryobject);
            this.dbnaryList.put(dbnaryobject.getLabel(), value);
        }
        else{
            List<DbnaryObject> value = new ArrayList<DbnaryObject>();
            value .add(dbnaryobject);
            this.dbnaryList.put(dbnaryobject.getLabel(),value);
        }
    }
    
}
