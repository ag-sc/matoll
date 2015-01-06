package process;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;
import patterns.PatternLibrary;
import patterns.SparqlPattern_EN_6;

public class Process {

	public static void main(String[] args) {
		
		List<String> properties = new ArrayList<String>();
		List<Model> sentences;
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_6());
		
		for (String property: properties)
		{
			sentences = new ArrayList<Model>();
			
			for (Model model: sentences)
			{
				library.extractLexicalEntries(model, lexicon);
			}
		}	
	}
}
