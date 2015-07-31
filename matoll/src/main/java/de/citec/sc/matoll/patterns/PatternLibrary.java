package de.citec.sc.matoll.patterns;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.utils.Lemmatizer;
import java.util.HashMap;
import java.util.Map;

public class PatternLibrary {

	List<SparqlPattern> Patterns;
        Map<String,String> PatternSparqlMapping = new HashMap<>();

	

	Lemmatizer Lemmatizer;
        
	
        /**
         * Initialization of PatternLibrary 
         * @param debugger 
         */
	public PatternLibrary()
	{
		Patterns = new ArrayList<SparqlPattern>();
		Lemmatizer = null;
                
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
		
		if (Lemmatizer != null)
			pattern.setLemmatizer(Lemmatizer);
	}
	
        /**
         * For each Sparql-query this function calls the extractLexicalEntries function, implemented in the pattern 
         * and adds the generated entry to the overall lexicon
         * @param model Model, containing a parsed sentence
         * @param lexicon Lexicon
         */
	public void extractLexicalEntries(Model model, Lexicon lexicon)
	{
		for (SparqlPattern pattern: Patterns)
		{
			if (Lemmatizer != null)
				pattern.setLemmatizer(Lemmatizer);
			pattern.extractLexicalEntries(model, lexicon);
		}
		
	}

	public void setPatterns(List<SparqlPattern> patterns) {
		Patterns = patterns;
                patterns.stream().forEach((p) -> {
                    PatternSparqlMapping.put(p.getID(), p.getQuery());
            });
		
	}
        
        public Map<String, String> getPatternSparqlMapping() {
            return PatternSparqlMapping;
        }
	
	
}
