package de.citec.sc.matoll.utils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Wordnet {
    private static IDictionary dict;
    private static WordnetStemmer wnstemmer;
    public Wordnet(String wordnet_home) throws IOException{
            String path = wordnet_home + File.separator + "dict"; URL url = new URL("file", null, path);
            // construct the dictionary object and open it
            Wordnet.dict = new Dictionary(url); 
            Wordnet.dict.open();
            Wordnet.wnstemmer = new WordnetStemmer(dict);
    }


    public void close(){
            Wordnet.dict.close();
    }
	
	
    public boolean checkForAdjective(String word){

        if(word.equals("in")){
                return false;
        }
        try{
                IIndexWord idxWord = Wordnet.dict.getIndexWord(word, POS.ADJECTIVE); 
                IWordID wordID = idxWord.getWordIDs().get(0);
                IWord string_word = dict.getWord(wordID);
        }
        catch(Exception e){
                return false;
        }
        return true;
    }
    
    public Set<String> getAllSynonyms(String input){
        Set<String> synonyms = new HashSet<>();
        List<IIndexWord> idx_words = new ArrayList<>();
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.NOUN));
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.ADVERB));
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.ADJECTIVE));
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.VERB));
        idx_words.stream().forEach((idx) -> {
            try{
                idx.getWordIDs().stream().map((wordID) -> dict.getWord(wordID)).map((word) -> word.getSynset()).forEach((isynset) -> {
                    isynset.getWords().stream().forEach((x) -> {
                        synonyms.add(x.getLemma());
                    });
                });
            }
            catch (Exception e){}
        });
        return synonyms;
    }
    
    public Set<String> getNounSynonyms(String input){
        Set<String> synonyms = new HashSet<>();
        List<IIndexWord> idx_words = new ArrayList<>();
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.NOUN));
        idx_words.stream().forEach((idx) -> {
            try{
                idx.getWordIDs().stream().map((wordID) -> dict.getWord(wordID)).map((word) -> word.getSynset()).forEach((isynset) -> {
                    isynset.getWords().stream().forEach((x) -> {
                        synonyms.add(x.getLemma());
                    });
                });
            }
            catch (Exception e){}
        });
        return synonyms;
    }
    
    public Set<String> getAdverbSynonyms(String input){
        Set<String> synonyms = new HashSet<>();
        List<IIndexWord> idx_words = new ArrayList<>();
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.ADVERB));
        idx_words.stream().forEach((idx) -> {
            try{
                idx.getWordIDs().stream().map((wordID) -> dict.getWord(wordID)).map((word) -> word.getSynset()).forEach((isynset) -> {
                    isynset.getWords().stream().forEach((x) -> {
                        synonyms.add(x.getLemma());
                    });
                });
            }
            catch (Exception e){}
        });
        return synonyms;
    }
    
    public Set<String> getAdjectiveSynonyms(String input){
        Set<String> synonyms = new HashSet<>();
        List<IIndexWord> idx_words = new ArrayList<>();
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.ADJECTIVE));
        idx_words.stream().forEach((idx) -> {
            try{
                idx.getWordIDs().stream().map((wordID) -> dict.getWord(wordID)).map((word) -> word.getSynset()).forEach((isynset) -> {
                    isynset.getWords().stream().forEach((x) -> {
                        synonyms.add(x.getLemma());
                    });
                });
            }
            catch (Exception e){}
        });
        return synonyms;
    }
    
    public Set<String> getVerbSynonyms(String input){
        Set<String> synonyms = new HashSet<>();
        List<IIndexWord> idx_words = new ArrayList<>();
        idx_words.add(Wordnet.dict.getIndexWord(input, POS.VERB));
        idx_words.stream().forEach((idx) -> {
            try{
                idx.getWordIDs().stream().map((wordID) -> dict.getWord(wordID)).map((word) -> word.getSynset()).forEach((isynset) -> {
                    isynset.getWords().stream().forEach((x) -> {
                        synonyms.add(x.getLemma());
                    });
                });
            }
            catch (Exception e){}
        });
        return synonyms;
    }
    
}
