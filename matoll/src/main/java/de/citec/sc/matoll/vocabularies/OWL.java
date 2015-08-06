package de.citec.sc.matoll.vocabularies;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class OWL {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	
	
	public static Property onProperty = defaultModel.createProperty("http://www.w3.org/2002/07/owl#onProperty");
	public static Property hasValue = defaultModel.createProperty("http://www.w3.org/2002/07/owl#hasValue");
	public static Property sameAs = defaultModel.createProperty("http://www.w3.org/2002/07/owl#sameAs");
      
}
