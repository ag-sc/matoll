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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author swalter
 */
public class Stopwords {
    
    private final List<String> stopwords = new ArrayList<>();
    
    
    public Stopwords(){
        loadStopwords("resources/englishST.txt",Language.EN);
        loadStopwords("resources/germanST.txt",Language.DE);
        loadStopwords("resources/spanishST.txt",Language.ES);
    }
    
    
    
    public boolean isStopword(String word,Language language){
        return stopwords.contains(word.toLowerCase()+"_"+language.toString());
    }
    
    private void loadStopwords(String file, Language language){
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            String[] lines = content.split("\n");
            
            for(String line : lines){
                this.stopwords.add(line.toLowerCase()+"_"+language);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Dbnary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
