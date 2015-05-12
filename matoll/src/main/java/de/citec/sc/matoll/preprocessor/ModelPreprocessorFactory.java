package de.citec.sc.matoll.preprocessor;

import de.citec.sc.matoll.coreference.EnglishCoreference;
import de.citec.sc.matoll.coreference.GermanCoreference;
import de.citec.sc.matoll.coreference.JapaneseCoreference;
import de.citec.sc.matoll.coreference.SpanishCoreference;
import java.util.HashSet;
import java.util.Set;

public class ModelPreprocessorFactory {
    /**
     * 
     * @param language
     * @return 
     */
	public static ModelPreprocessor getPreprocessor(String language)
	{
		if (language.equals("EN"))
		{
			Set<String >pos = new HashSet<String>();
			pos.add("prep");
			pos.add("appos");
			pos.add("nn");
			pos.add("dobj");
			
			ModelPreprocessor processor = new ModelPreprocessor();
			
                        processor.setCoreference(new EnglishCoreference());
               
			processor.setPOS(pos);
			
			return processor;
			
		}
		
                /*
                TODO: Adapt other languages
                */
		if (language.equals("DE"))
		{
			Set<String >pos = new HashSet<String>();
			pos.add("prep");
			pos.add("appos");
			pos.add("nn");
			pos.add("dobj");
			
			ModelPreprocessor processor = new ModelPreprocessor();
			
                        processor.setCoreference(new GermanCoreference());
                        
			processor.setPOS(pos);
			
			return processor;
		}
		
		if (language.equals("ES"))
		{
			Set<String >pos = new HashSet<String>();
			pos.add("prep");
			pos.add("appos");
			pos.add("nn");
			pos.add("dobj");
			
			ModelPreprocessor processor = new ModelPreprocessor();
                        
                        processor.setCoreference(new SpanishCoreference());
			
			processor.setPOS(pos);
			
			return processor;
		}
		
		return null;
		
	}
	
}
