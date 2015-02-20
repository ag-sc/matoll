package de.citec.sc.matoll.preprocessor;

import java.util.HashSet;
import java.util.Set;

public class ModelPreprocessorFactory {

	ModelPreprocessor getPreprocessor(String language)
	{
		if (language.equals("EN"))
		{
			Set<String >pos = new HashSet<String>();
			pos.add("prep");
			pos.add("appos");
			pos.add("nn");
			pos.add("dobj");
			
			ModelPreprocessor processor = new ModelPreprocessor();
			
			processor.setPOS(pos);
			
		}
		
		if (language.equals("DE"))
		{
			
		}
		
		if (language.equals("ES"))
		{
			
		}
		
		return null;
		
	}
	
}
