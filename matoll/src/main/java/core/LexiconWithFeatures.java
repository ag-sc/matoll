package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import process.Matoll;

public class LexiconWithFeatures extends Lexicon{

	Logger logger = LogManager.getLogger(Matoll.class.getName());
	
	HashMap<LexicalEntry,FeatureVector> map;
	
	public LexiconWithFeatures()
	{
		super();
		map = new HashMap<LexicalEntry,FeatureVector>();
	}
	
	public void add(LexicalEntry entry, FeatureVector vec)
	{
		FeatureVector vector = null;
		FeatureVector updatedVector = null;
		
		if (this.contains(entry))
		{	
			if (map.containsKey(entry))
			{
				vector = map.get(entry);
				updatedVector = vector.add(vec);
				
				map.put(entry, updatedVector);
			}
			else
			{
				map.put(entry, vec.add(vec));
			}

			
			List<String> sentences = new ArrayList<String>();
			
			for (String sentence: entry.getSentences())
			{
				sentences.add(sentence);
			}
			
			this.getLexicalEntry(entry).addSentences(sentences);
			
			logger.info("Entry with lemma "+entry.getCanonicalForm() +" is aleady there!\n");
			logger.info("Updated "+vector+"\n");
			logger.info("to"+updatedVector+"\n");
		
	
		}
		else
		{
			entry.setURI(baseURI+"LexicalEntry_"+entries.size()+"_"+entry.getCanonicalForm());
			this.addEntry(entry);
			map.put(entry,vec);
			
			logger.info("Entry with lemma "+entry.getCanonicalForm() +" is aleady there!\n");
			logger.info("Updated "+vector+"\n");
			logger.info("to"+updatedVector+"\n");
			
			
		}
	}
		


	public FeatureVector getFeatureVector(LexicalEntry entry)
	{
		if (map.containsKey(entry)) return map.get(entry);
	
		else return null;
	}

	public Lexicon filterFeatureGreater(String string, double value) {
		
		Lexicon lexicon = new Lexicon();
	
		for (LexicalEntry entry: map.keySet())
		{
			if (map.get(entry) != null)
					if (map.get(entry).getValueOfFeature("freq") >= value)
						lexicon.addEntry(entry);
		}
		
		return lexicon;
		
	}
	
	public String toString()
	{
		String string = "";
		
		for (LexicalEntry entry: entries)
		{
			string+= entry.toString() + "\n";
			string+= map.get(entry).toString() +"\n\n";
		}
		
		return string;
	}
	
}
