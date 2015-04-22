package de.citec.sc.matoll.utils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
/*import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import java.net.MalformedURLException;
import edu.mit.jwi.item.IWordID;*/
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.WordnetStemmer;
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
			//System.out.println("adjective: "+word.toString());
		}
		catch(Exception e){
			//System.out.println("no adjective: "+word);
			return false;
		}
		
		return true;
		
		
	}
}
