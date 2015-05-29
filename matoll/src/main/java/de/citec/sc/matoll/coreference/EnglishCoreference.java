/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.HashMap;

/**
 *
 * @author swalter
 */
public class EnglishCoreference implements Coreference{

    public void computeCoreference(Model model, HashMap<String,String> Resource2Lemma, HashMap<Integer,String> Int2NodeMapping, HashMap<String,Integer> Node2IntMapping, HashMap<String,String> senseArgs) {
		
		String lemma;
		Integer number;
                
		// System.out.print("Computing coreference!!!\n");
		
		for (String resource: Resource2Lemma.keySet())
		{
			lemma = Resource2Lemma.get(resource);
			
			if (lemma.equals("which") || lemma.equals("who"))
			{
				// System.out.print("Contains "+lemma+"\n");
				
				number = Node2IntMapping.get(resource);
				
				// System.out.print("... at position "+number+"\n");
				
				if (number > 1)
				{
					if (Resource2Lemma.get(Int2NodeMapping.get(number -1)).equals(","))
					{
						
						// System.out.print("... previous one is a comma "+(number-1)+"\n");
						if (number > 2 && senseArgs.containsKey(Int2NodeMapping.get(number -2)))
						{
							model.add(model.getResource(resource), model.createProperty("own:senseArg"), model.createResource(senseArgs.get(Int2NodeMapping.get(number -2))));
							// System.out.print("Relative pronoun" + lemma + " resolved to "+(number-2)+"!!!\n");
						}
						
					}
					else
					{
						if (senseArgs.containsKey(Int2NodeMapping.get(number -1)))
						{
							model.add(model.getResource(resource), model.createProperty("own:senseArg"), model.createResource(senseArgs.get(Int2NodeMapping.get(number -1))));
							// System.out.print("Relative pronoun" + lemma + " resolved to "+(number-1)+"!!!\n");
						}
					}
				}
				
				
			}
			
		}
		
		
	}
    
}
