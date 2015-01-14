package patterns;

import utils.Lemmatizer;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public abstract class SparqlPattern {
	
	Lemmatizer Lemmatizer;
	
	public abstract String getID();
	
	public abstract void extractLexicalEntries(Model model,String reference, LexiconWithFeatures lexicon); 

	public void setLemmatizer(Lemmatizer lemmatizer) {
		
		Lemmatizer = lemmatizer;
		
	}
	
}
