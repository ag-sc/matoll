package de.citec.sc.matoll.patterns;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.utils.Debug;
import de.citec.sc.matoll.utils.Lemmatizer;

public class PatternLibrary {

	List<SparqlPattern> Patterns;
	

	Lemmatizer Lemmatizer;
        
        private static Debug debugger;
	
        /**
         * Initialization of PatternLibrary 
         * @param debugger 
         */
	public PatternLibrary(Debug debugger)
	{
		Patterns = new ArrayList<SparqlPattern>();
		Lemmatizer = null;
                PatternLibrary.debugger=debugger;
                
	}

	
        /**
         * Sets Lemmatizer
         * @param lemmatizer Lemmatizer 
         */
	public void setLemmatizer(Lemmatizer lemmatizer)
	{
		Lemmatizer = lemmatizer;
	}
	
        /**
         * Adds pattern to list of patterns
         * @param pattern  Pattern
         */
	public void addPattern(SparqlPattern pattern)
	{
		Patterns.add(pattern);
                debugger.print("add pattern "+pattern.toString(), PatternLibrary.class.getName());
		
		if (Lemmatizer != null)
			pattern.setLemmatizer(Lemmatizer);
                pattern.setDebugger(debugger);
	}
	
        /**
         * For each Sparql-query this function calls the extractLexicalEntries function, implemented in the pattern 
         * and adds the generated entry to the overall lexicon
         * @param model Model, containing a parsed sentence
         * @param lexicon Lexicon
         */
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon)
	{
		for (SparqlPattern pattern: Patterns)
		{
                        debugger.print("Running: "+pattern.getID(), PatternLibrary.class.getName());
			if (Lemmatizer != null)
				pattern.setLemmatizer(Lemmatizer);
                        pattern.setDebugger(debugger);
			pattern.extractLexicalEntries(model, lexicon);
		}
		
	}

	public void setPatterns(List<SparqlPattern> patterns) {
		Patterns = patterns;
		
	}
	
	
}
