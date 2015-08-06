package de.citec.sc.matoll.vocabularies;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class PROVO {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property associatedWith = defaultModel.createProperty("http://www.w3.org/ns/prov#associatedWith");
	public static Property generatedBy = defaultModel.createProperty("http://www.w3.org/ns/prov#generatedBy");
	public static Property startedAtTime = defaultModel.createProperty("http://www.w3.org/ns/prov#startedAtTime");
	public static Property endedatTime = defaultModel.createProperty("http://www.w3.org/ns/prov#endedAtTime");
        public static Property confidence = defaultModel.createProperty("http://www.w3.org/ns/prov#confidence");
	public static Property frequency = defaultModel.createProperty("http://www.w3.org/ns/prov#frequency");
        public static Property pattern = defaultModel.createProperty("http://www.w3.org/ns/prov#pattern");
        public static Property query = defaultModel.createProperty("http://www.w3.org/ns/prov#query");
        public static Property sentence = defaultModel.createProperty("http://www.w3.org/ns/prov#sentence");

	
	public static Resource Activity = defaultModel.createProperty("http://www.w3.org/ns/prov#Activity");
	public static Resource Entity = defaultModel.createProperty("http://www.w3.org/ns/prov#Entity");
	public static Resource Agent = defaultModel.createProperty("http://www.w3.org/ns/prov#Agent");
    
     
     
       
     
}
