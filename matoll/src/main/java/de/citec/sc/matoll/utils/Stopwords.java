
package de.citec.sc.matoll.utils;

import de.citec.sc.matoll.core.Language;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        return stopwords.contains(word.trim().toLowerCase()+"_"+language.toString());
    }
    
    private void loadStopwords(String file, Language language){
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            String[] lines = content.split("\n");
            
            for(String line : lines){
                line = line.replace("\n","").trim();
                this.stopwords.add(line.toLowerCase()+"_"+language);
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
