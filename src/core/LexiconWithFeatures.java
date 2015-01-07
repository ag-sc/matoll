package core;

import java.util.HashMap;

public class LexiconWithFeatures extends Lexicon{

	HashMap<LexicalEntry,FeatureVector> map;
	
	public LexiconWithFeatures()
	{
		super();
		map = new HashMap<LexicalEntry,FeatureVector>();
	}
	
	public void add(LexicalEntry entry, FeatureVector features)
	{
		if (this.contains(entry))
		{
			entry = this.getLexicalEntry(entry);
			
			map.put(entry, map.get(entry).add(features));
			
		}
		else
		{
			this.addEntry(entry);
			map.put(entry,features);
			
		}
	}
		
	public FeatureVector getFeatureVector(LexicalEntry entry)
	{
		if (map.containsKey(entry)) return map.get(entry);
	
		else return null;
	}
	
	
	
}
