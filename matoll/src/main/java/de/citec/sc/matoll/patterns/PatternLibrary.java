package de.citec.sc.matoll.patterns;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.utils.Lemmatizer;

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
	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon)
	{
		for (SparqlPattern pattern: Patterns)
		{
			pattern.extractLexicalEntries(model, lexicon);
		}
		
	}
	
	
}
