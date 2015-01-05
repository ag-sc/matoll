package Patterns;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class PatternLibrary {

	ArrayList<SparqlPattern> Patterns;
	
	public PatternLibrary()
	{
		Patterns = new ArrayList<SparqlPattern>();
	}
	
	public void addPattern(SparqlPattern pattern)
	{
		Patterns.add(pattern);
	}
	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon)
	{
		for (SparqlPattern pattern: Patterns)
		{
			pattern.extractLexicalEntries(model, "property", lexicon);
		}
		
	}
	
	
}
