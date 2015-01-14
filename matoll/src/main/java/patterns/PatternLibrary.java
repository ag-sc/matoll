package patterns;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

import utils.Lemmatizer;

public class PatternLibrary {

	ArrayList<SparqlPattern> Patterns;
	
	Lemmatizer Lemmatizer;
	
	public PatternLibrary()
	{
		Patterns = new ArrayList<SparqlPattern>();
		Lemmatizer = null;
	}
	
	public void setLemmatizer(Lemmatizer lemmatizer)
	{
		Lemmatizer = lemmatizer;
	}
	
	public void addPattern(SparqlPattern pattern)
	{
		Patterns.add(pattern);
		
		if (Lemmatizer != null)
			pattern.setLemmatizer(Lemmatizer);
	}
	
	public void extractLexicalEntries(Model model, String property, LexiconWithFeatures lexicon)
	{
		for (SparqlPattern pattern: Patterns)
		{
			pattern.extractLexicalEntries(model, property, lexicon);
		}
		
	}
	
	
}
