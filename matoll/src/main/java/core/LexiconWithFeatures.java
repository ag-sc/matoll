package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LexiconWithFeatures extends Lexicon{

	HashMap<LexicalEntry,FeatureVector> map;
	
	public LexiconWithFeatures()
	{
		super();
		map = new HashMap<LexicalEntry,FeatureVector>();
	}
	
	public void add(LexicalEntry entry, FeatureVector features)
	{
		FeatureVector vector;
		
		if (this.contains(entry))
		{	
			if (map.containsKey(entry))
			{
				vector = map.get(entry);
				map.put(entry, features.add(vector));
			}
			else
			{
				map.put(entry, features.add(features));
			}

			
			List<String> sentences = new ArrayList<String>();
			
			for (String sentence: entry.getSentences())
			{
				sentences.add(sentence);
			}
			
			this.getLexicalEntry(entry).addSentences(sentences);
			
			System.out.print("Entry with lemma "+entry.getCanonicalForm() +" is aleady there!\n");
		
	
			// do something to update the features
			
		}
		else
		{
			entry.setURI(baseURI+"LexicalEntry_"+entries.size()+"_"+entry.getCanonicalForm());
			this.addEntry(entry);
			map.put(entry,features);
			
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
