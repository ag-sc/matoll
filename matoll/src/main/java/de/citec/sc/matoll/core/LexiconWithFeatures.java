package de.citec.sc.matoll.core;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.process.Matoll;

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
		logger.info("add entry "+entry.toString()+"\n");
		FeatureVector vector = null;
		FeatureVector updatedVector = null;
		
		this.addEntry(entry);
		
		if (map.containsKey(entry))
		{
			vector = map.get(entry);
			updatedVector = vector.add(vec);
			
			map.put(entry, updatedVector);
			
			logger.info("Entry with lemma "+entry.getCanonicalForm() +" is aleady there!\n");
			logger.info("Updated "+vector+"\n");
			logger.info("to "+updatedVector+"\n");
			logger.info("Sentences: ");
			
		}
		else
		{
			map.put(entry, vec);
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
