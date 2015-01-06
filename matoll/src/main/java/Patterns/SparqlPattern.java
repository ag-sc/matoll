package Patterns;

import com.hp.hpl.jena.rdf.model.Model;

import Core.LexiconWithFeatures;

public interface SparqlPattern {
	
	public String getID();
	
	public void extractLexicalEntries(Model model,String reference, LexiconWithFeatures lexicon);
	
}
