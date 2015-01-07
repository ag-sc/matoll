package process;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;
import patterns.PatternLibrary;
import patterns.SparqlPattern_EN_6;
import preprocessor.ModelPreprocessor;

public class Process {

	public static void main(String[] args) {
		
		List<String> properties = new ArrayList<String>();
		List<Model> sentences;
		
		ModelPreprocessor preprocessor = new ModelPreprocessor();
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_6());
		
		for (String property: properties)
		{
			sentences = new ArrayList<Model>();
			
			String subjectEntity = "Barack Obama";
			String objectEntity = "Michele Obama";
		
			for (Model model: sentences)
			{
				preprocessor.preprocess(model,subjectEntity, objectEntity);
				
				library.extractLexicalEntries(model, property, lexicon);
			}
		}	
	}
}
